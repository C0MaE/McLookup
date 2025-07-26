package dev.comae.McLookup

import dev.comae.McLookup.exceptions.UnknownPlayerException
import dev.comae.McLookup.methods.HttpRequests
import org.json.JSONObject
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
        }

        return this
    }

    fun getPlayerSkinURL(): String {
        val decodedTexturesString = String(Base64.getDecoder().decode(textureBase64))

        val jsonTextures = JSONObject(decodedTexturesString)

        val texturesURLs = jsonTextures.getJSONObject("textures")

        val skinObject = texturesURLs.getJSONObject("SKIN")

        val skinUrl = skinObject.getString("url")

        return skinUrl
    }

    fun playerGotCape(): Boolean {
        val decodedTexturesString = String(Base64.getDecoder().decode(textureBase64))

        val jsonTextures = JSONObject(decodedTexturesString)

        val texturesURLs = jsonTextures.getJSONObject("textures")

        return texturesURLs.has("CAPE")
    }

    fun getPlayerCapeURL(): String {
        val decodedTexturesString = String(Base64.getDecoder().decode(textureBase64))

        val jsonTextures = JSONObject(decodedTexturesString)

        val texturesURLs = jsonTextures.getJSONObject("textures")

        if(texturesURLs.has("CAPE")) {
            val skinObject = texturesURLs.getJSONObject("CAPE")

            val skinUrl = skinObject.getString("url")

            return skinUrl
        }
        return ""
    }

    fun getPlayerSkinImage(): BufferedImage {
        val uri = URI(getPlayerSkinURL())
        val url = uri.toURL()

        val image: BufferedImage = ImageIO.read(url)

        return image
    }

    fun getPlayerSkinDataUrl(): String {
        val skinImage = getPlayerSkinImage()

        val outputStream = ByteArrayOutputStream()
        ImageIO.write(skinImage, "png", outputStream)
        val imageBytes = outputStream.toByteArray()

        val base64String = Base64.getEncoder().encodeToString(imageBytes)

        val dataUrl = "data:image/png;base64,$base64String"

        return dataUrl
    }

    fun getPlayerHeadImage(): BufferedImage {
        val skinImage = getPlayerSkinImage()

        val headImage: BufferedImage = skinImage.getSubimage(8, 8, 8, 8)

        return headImage
    }

    fun getPlayerHeadDataUrl(): String {
        val headImage = getPlayerHeadImage()

        val outputStream = ByteArrayOutputStream()
        ImageIO.write(headImage, "png", outputStream)
        val imageBytes = outputStream.toByteArray()

        val base64String = Base64.getEncoder().encodeToString(imageBytes)

        val dataUrl = "data:image/png;base64,$base64String"

        return dataUrl
    }
}