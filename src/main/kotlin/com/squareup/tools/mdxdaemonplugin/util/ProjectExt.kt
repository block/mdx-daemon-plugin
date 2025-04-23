package com.squareup.tools.mdxdaemonplugin.util

import com.intellij.openapi.project.Project
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Returns the root path of the project.
 *
 * @return the root path of the project, or null if the base path is not set.
 */
fun Project.projectRootPath(): Path? {
    return basePath?.let { Paths.get(it) }
}
