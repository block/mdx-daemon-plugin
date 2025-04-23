package com.squareup.tools.mdxdaemonplugin

import com.intellij.openapi.project.Project
import com.squareup.tools.mdxdaemonplugin.model.Source
import com.squareup.tools.mdxdaemonplugin.usecase.ConfigurationCacheUseCase

interface MdxDaemon {
    fun startBackgroundBuild(
        project: Project,
        source: Source,
    )

    fun cancelBackgroundBuild(
        project: Project,
        source: Source,
    )

    companion object {
        fun configurationCacheUseCase(): MdxDaemon = ConfigurationCacheUseCase()
    }
}
