package com.squareup.tools.mdxdaemonplugin

import com.intellij.openapi.externalSystem.service.project.manage.ProjectDataImportListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.squareup.tools.mdxdaemonplugin.model.Source

/**
 * MdxProjectDataImportListener handles the completion of project data imports.
 * It starts the background build when IDE sync is fully completed.
 */
class MdxProjectDataImportListener(private val project: Project) : ProjectDataImportListener {
    private val configurationCacheUseCase: MdxDaemon = MdxDaemon.configurationCacheUseCase()

    override fun onImportFinished(projectPath: String?) {
        println("MdxDaemon: Gradle SYNC finished for project ${project.basePath}")
        configurationCacheUseCase.startBackgroundBuild(project, Source.OnSyncCompleteWithSuccess)
    }
}