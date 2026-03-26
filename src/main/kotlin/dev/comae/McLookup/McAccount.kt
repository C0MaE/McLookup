package dev.comae.McLookup

import dev.comae.McLookup.exceptions.UnknownPlayerException
import dev.comae.McLookup.methods.HttpRequests
import org.json.JSONObject
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.net.URI
import java.util.Base64
import java.util.UUID
import javax.imageio.ImageIO

class McAccount {
    lateinit var uuid: UUID

    var playerName: String? = null
    var textureBase64: String? = null

    // Cached decoded textures object — avoids redundant Base64 decode + JSON parse on every call
    private var _texturesJson: JSONObject? = null
    private val texturesJson: JSONObject
        get() = _texturesJson ?: run {
            val decoded = String(Base64.getDecoder().decode(textureBase64))
            JSONObject(decoded).getJSONObject("textures").also { _texturesJson = it }
        }

    // Cached skin image — avoids re-downloading from network on every call
    private var _skinImage: BufferedImage? = null

    constructor(uuid: UUID) {
        this.uuid = uuid
    }

    constructor(playername: String) {
        val uuid = HttpRequests().getUUIDFromPlayerName(playername)
        if(uuid != null) {
            this.uuid = uuid
        } else {
            throw UnknownPlayerException("Player Not Found $playername")
        }
    }

    fun load(): McAccount {
        val helper = HttpRequests()

        val json = helper.getAccountInformation(uuid)

        playerName = json.getString("name")

        val properties = json.getJSONArray("properties")

        val textures = properties.getJSONObject(0)

        if(textures.getString("name") == "textures") {
            textureBase64 = textures.getString("value")
            _texturesJson = null  // invalidate cache on reload
            _skinImage = null
        }

        return this
    }

    fun getPlayerSkinURL(): String {
        return texturesJson.getJSONObject("SKIN").getString("url")
    }

    fun playerGotCape(): Boolean {
        return texturesJson.has("CAPE")
    }

    fun getPlayerCapeURL(): String {
        if(texturesJson.has("CAPE")) {
            return texturesJson.getJSONObject("CAPE").getString("url")
        }
        return ""
    }

    fun getPlayerSkinImage(): BufferedImage {
        return _skinImage ?: ImageIO.read(URI(getPlayerSkinURL()).toURL()).also { _skinImage = it }
    }

    fun getPlayerSkinDataUrl(): String {
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(getPlayerSkinImage(), "png", outputStream)
        val base64String = Base64.getEncoder().encodeToString(outputStream.toByteArray())
        return "data:image/png;base64,$base64String"
    }

    fun getPlayerHeadImage(): BufferedImage {
        return getPlayerSkinImage().getSubimage(8, 8, 8, 8)
    }

    fun getPlayerHeadDataUrl(scale: Int = 1): String {
        val headImage = getPlayerHeadImage()

        val scaledWidth = headImage.width * scale
        val scaledHeight = headImage.height * scale

        val scaledImage = BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB)
        val graphics = scaledImage.createGraphics()

        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR)
        graphics.drawImage(headImage, 0, 0, scaledWidth, scaledHeight, null)
        graphics.dispose()

        val outputStream = ByteArrayOutputStream()
        ImageIO.write(scaledImage, "png", outputStream)
        val base64String = Base64.getEncoder().encodeToString(outputStream.toByteArray())

        return "data:image/png;base64,$base64String"
    }
}