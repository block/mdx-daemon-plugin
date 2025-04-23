package util

import com.squareup.tools.mdxdaemonplugin.util.FeatureFlagReader
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.writeText

class FeatureFlagReaderTest {
    private lateinit var tempProjectDir: Path
    private lateinit var tempGlobalFile: File
    private val reader = FeatureFlagReader()
    private val jobId = "config-cache"

    @BeforeEach
    fun setUp() {
        tempProjectDir = Files.createTempDirectory("mdx-test")
        tempGlobalFile = File(System.getProperty("user.home"), ".gradle/gradle.properties.bak")

        // Backup existing global file if it exists
        File(System.getProperty("user.home"), ".gradle/gradle.properties").let { currentGlobal ->
            if (currentGlobal.exists()) {
                currentGlobal.copyTo(tempGlobalFile, overwrite = true)
            }
        }
    }

    @AfterEach
    fun tearDown() {
        // Clean up temp project dir
        tempProjectDir.toFile().deleteRecursively()

        // Restore original global file
        val globalFile = File(System.getProperty("user.home"), ".gradle/gradle.properties")
        if (tempGlobalFile.exists()) {
            tempGlobalFile.copyTo(globalFile, overwrite = true)
            tempGlobalFile.delete()
        } else {
            globalFile.delete()
        }
    }

    private fun writeLocalProps(content: String) {
        tempProjectDir.resolve("gradle.properties").writeText(content)
    }

    private fun writeGlobalProps(content: String) {
        val globalFile = File(System.getProperty("user.home"), ".gradle/gradle.properties")
        globalFile.writeText(content)
    }

    @Test
    fun `returns true when global flag is true`() {
        writeGlobalProps("mdxdaemon.$jobId.enabled=true")
        writeLocalProps("mdxdaemon.$jobId.enabled=false") // Should be ignored

        assertTrue(reader.isJobEnabled(jobId, tempProjectDir))
    }

    @Test
    fun `returns false when global flag is false`() {
        writeGlobalProps("mdxdaemon.$jobId.enabled=false")
        writeLocalProps("mdxdaemon.$jobId.enabled=true") // Should be ignored

        assertFalse(reader.isJobEnabled(jobId, tempProjectDir))
    }

    @Test
    fun `returns true when local flag is true and global is unset`() {
        writeLocalProps("mdxdaemon.$jobId.enabled=true")

        assertTrue(reader.isJobEnabled(jobId, tempProjectDir))
    }

    @Test
    fun `returns false when local flag is false and global is unset`() {
        writeLocalProps("mdxdaemon.$jobId.enabled=false")

        assertFalse(reader.isJobEnabled(jobId, tempProjectDir))
    }

    @Test
    fun `returns false when no flags are present`() {
        assertFalse(reader.isJobEnabled(jobId, tempProjectDir))
    }

    @Test
    fun `ignores malformed lines and returns false`() {
        writeLocalProps("mdxdaemon.$jobId.enabled") // malformed, no '='
        assertFalse(reader.isJobEnabled(jobId, tempProjectDir))
    }
}
