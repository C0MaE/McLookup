package dev.comae.McLookup.methods

import java.awt.image.BufferedImage

fun cropImage(image: BufferedImage, x: Int, y: Int, width: Int, height: Int): BufferedImage {
    return image.getSubimage(x, y, width, height)
}