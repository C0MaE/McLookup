import dev.comae.McLookup.McAccount
import dev.comae.McLookup.methods.HttpRequests
import java.awt.image.BufferedImage
import java.net.URL
import java.util.UUID
import javax.imageio.ImageIO

fun main() {
    // URL zum Minecraft-Texturbild
    val url = URL("https://textures.minecraft.net/texture/f9cd2983c7a1c76a5ec0ee39093df2dcc32ac15db07485ac3fbcfa213dd26cc8")

    // Bild laden
    val image: BufferedImage = ImageIO.read(url)

    // Beispiel: Ausschnitt bei x=16, y=16, Größe 32x32 Pixel
    val cropped: BufferedImage = image.getSubimage(8, 8, 8, 8)

    // Optional: als Datei speichern
    ImageIO.write(cropped, "png", java.io.File("cropped.png"))

    println("Ausschnitt gespeichert als 'cropped.png'")
}