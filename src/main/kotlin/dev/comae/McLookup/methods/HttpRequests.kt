package dev.comae.McLookup.methods

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URI
import java.util.UUID

class HttpRequests {

    fun getUUIDFromPlayerName(playerName: String): UUID? {
        val uri = URI("https://api.mojang.com/users/profiles/minecraft/$playerName")
        val url = uri.toURL()
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        return try {
            val response = connection.inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(response)
            val uuidString = json.getString("id")
            UUID.fromString(formatToUuid(uuidString))
        } finally {
            connection.disconnect()
        }
    }

    fun getAccountInformation(uuid: UUID): JSONObject {
        val uri = URI("https://sessionserver.mojang.com/session/minecraft/profile/${uuid.toString()}")
        val url = uri.toURL()
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        return try {
            val response = connection.inputStream.bufferedReader().use { it.readText() }
            JSONObject(response)
        } finally {
            connection.disconnect()
        }
    }

}