package com.squareup.tools.mdxdaemonplugin.util

import java.net.InetSocketAddress
import java.net.Socket

/**
 * Utility object to check if a port is open on a given host.
 */
object PortChecker {
    fun isPortOpen(
        host: String = "localhost",
        port: Int,
        timeoutMs: Int = 500,
    ): Boolean {
        return try {
            Socket().use { socket ->
                socket.connect(InetSocketAddress(host, port), timeoutMs)
                true
            }
        } catch (e: Exception) {
            false
        }
    }
}
