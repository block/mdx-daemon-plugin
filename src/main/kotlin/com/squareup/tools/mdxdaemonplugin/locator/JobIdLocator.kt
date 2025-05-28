package com.squareup.tools.mdxdaemonplugin.locator

import com.intellij.openapi.project.Project
import java.io.File
import java.util.Properties

/**
 * Locates the job ID for the MdxDaemon by checking the gradle.properties file in the project
 */
class JobIdLocator {
    fun locateJobId(project: Project): String? {
        val gradlePropertiesFile = File(project.basePath ?: return null, "gradle.properties")
        val userPropertiesFile = File(System.getProperty("user.home"), ".gradle/gradle.properties")

        val properties = Properties()
        if (gradlePropertiesFile.exists()) {
            gradlePropertiesFile.inputStream().use { properties.load(it) }
        }
        if (userPropertiesFile.exists()) {
            userPropertiesFile.inputStream().use { properties.load(it) }
        }

        return properties.getProperty("mdxdaemon.jobId")?.trim()
    }
}
