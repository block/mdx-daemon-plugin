package com.squareup.tools.mdxdaemonplugin.util

import com.squareup.tools.mdxdaemonplugin.model.Source
import java.net.HttpURLConnection
import java.net.URL

/**
 * A client for communicating with the MDX Daemon server.
 *
 * @param port The port on which the MDX Daemon server is running. Default is 2964.
 * @param host The host on which the MDX Daemon server is running. Default is "localhost".
 */
class MdxDaemonClient(
    private val port: Int = DEFAULT_PORT,
    private val host: String = "localhost",
) {
    fun post(
        path: String,
        source: Source,
    ) {
        val url = URL("http://$host:$port/$path?source=${source.name}")
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            connectTimeout = 1000
            readTimeout = 1000

            try {
                val code = responseCode
                val message = responseMessage
                println("Request to '$path' completed with code $code and message: $message")
            } catch (e: Exception) {
                System.err.println("Error calling '$path': ${e.message}")
            } finally {
                disconnect()
            }
        }
    }

    companion object {
        const val DEFAULT_PORT = 2964
    }
}
