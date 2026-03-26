package dev.comae.McLookup.methods

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URI
import java.util.UUID

class HttpRequests {

    fun getUUIDFromPlayerName(playerName: String): UUID? {
        val uri = URI("https://api.mojang.com/users/profiles/minecraft/$playerName")
        val connection = uri.toURL().openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        return try {
            if (connection.responseCode != 200) return null
            val response = connection.inputStream.bufferedReader().use { it.readText() }
            UUID.fromString(formatToUuid(JSONObject(response).getString("id")))
        } finally {
            connection.disconnect()
        }
    }

    fun getAccountInformation(uuid: UUID): JSONObject {
        val uri = URI("https://sessionserver.mojang.com/session/minecraft/profile/$uuid")
        val connection = uri.toURL().openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        return try {
            val response = connection.inputStream.bufferedReader().use { it.readText() }
            JSONObject(response)
        } finally {
            connection.disconnect()
        }
    }

}