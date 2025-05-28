package locator

import com.intellij.openapi.project.Project
import com.squareup.tools.mdxdaemonplugin.locator.JobIdLocator
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import java.nio.file.Files

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JobIdLocatorTest {
    private lateinit var tempProjectDir: File
    private lateinit var gradleProperties: File
    private lateinit var jobIdLocator: JobIdLocator

    @BeforeAll
    fun setup() {
        tempProjectDir = Files.createTempDirectory("fake-project").toFile()
        gradleProperties = File(tempProjectDir, "gradle.properties")
        jobIdLocator = JobIdLocator()
    }

    @AfterEach
    fun cleanup() {
        gradleProperties.delete()
    }

    @AfterAll
    fun tearDown() {
        tempProjectDir.deleteRecursively()
    }

    @Test
    fun `returns jobId from project gradle properties if present`() {
        gradleProperties.writeText("mdxdaemon.jobId=cash-android")

        val project = mockProject(tempProjectDir.path)
        val result = jobIdLocator.locateJobId(project)

        assertEquals("cash-android", result)
    }

    @Test
    fun `returns trimmed jobId from project gradle properties if present`() {
        gradleProperties.writeText("mdxdaemon.jobId=cash-android ")

        val project = mockProject(tempProjectDir.path)
        val result = jobIdLocator.locateJobId(project)

        assertEquals("cash-android", result)
    }

    @Test
    fun `returns jobId from global gradle properties if project file is missing`() {
        val globalPropsFile = File(System.getProperty("user.home"), ".gradle/gradle.properties")
        val backup = if (globalPropsFile.exists()) globalPropsFile.readText() else null

        try {
            globalPropsFile.parentFile.mkdirs()
            globalPropsFile.writeText("mdxdaemon.jobId=global-job")

            val project = mockProject("/non/existent/project")
            val result = jobIdLocator.locateJobId(project)

            assertEquals("global-job", result)
        } finally {
            if (backup != null) {
                globalPropsFile.writeText(backup)
            } else {
                globalPropsFile.delete()
            }
        }
    }

    @Test
    fun `returns null when no jobId is found in any properties file`() {
        val project = mockProject(tempProjectDir.path)
        val result = jobIdLocator.locateJobId(project)

        assertNull(result)
    }

    private fun mockProject(path: String): Project = FakeProject(path)
}
