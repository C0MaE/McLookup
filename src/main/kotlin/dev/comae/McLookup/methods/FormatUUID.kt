package dev.comae.McLookup.methods

fun formatToUuid(input: String): String {
    require(input.length == 32) { "Input string must be 32 characters long." }
    require(input.matches(Regex("[0-9a-fA-F]+"))) { "Input string must contain only hexadecimal characters." }

    return buildString {
        append(input.substring(0, 8))
        append("-")
        append(input.substring(8, 12))
        append("-")
        append(input.substring(12, 16))
        append("-")
        append(input.substring(16, 20))
        append("-")
        append(input.substring(20, 32))
    }
}