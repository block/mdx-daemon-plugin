package util

import com.squareup.tools.mdxdaemonplugin.util.PortChecker
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.net.ServerSocket

class PortCheckerTest {
    @Test
    fun `isPortOpen returns true when port is open`() {
        val socket = ServerSocket(0) // Bind to a random free port
        val port = socket.localPort

        val result = PortChecker.isPortOpen(port = port)
        assertTrue(result)

        socket.close()
    }
}
