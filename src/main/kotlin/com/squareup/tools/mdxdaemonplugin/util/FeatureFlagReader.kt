package com.squareup.tools.mdxdaemonplugin.util

import java.io.File
import java.nio.file.Path

/**
 * Reads feature flags from the local and global gradle.properties files.
 *
 * The local file is located at <project-root>/gradle.properties and the global file is located at
 * ~/.gradle/gradle.properties.
 *
 * The feature flag is expected to be in the format "mdxdaemon.<jobId>.enabled=true|false".
 */
class FeatureFlagReader {
    fun isJobEnabled(
        jobId: String,
        projectRoot: Path,
    ): Boolean {
        val localFile = projectRoot.resolve("gradle.properties").toFile()
        val globalFile = File(System.getProperty("user.home"), ".gradle/gradle.properties")

        val localValue = readFlagFromFile(localFile, jobId)
        val globalValue = readFlagFromFile(globalFile, jobId)

        return when {
            globalValue == "true" -> true
            globalValue == "false" -> false
            localValue == "true" -> true
            localValue == "false" -> false
            else -> false
        }
    }

    private fun readFlagFromFile(
        file: File,
        jobId: String,
    ): String? {
        if (!file.exists()) return null
        val flagKey = "mdxdaemon.$jobId.enabled"
        return file.useLines { lines ->
            lines.firstOrNull { it.trim().startsWith(flagKey) }
                ?.split("=")
                ?.getOrNull(1)
                ?.trim()
        }
    }
}
