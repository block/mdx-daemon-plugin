package com.squareup.tools.mdxdaemonplugin

import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskId
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationListener
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskType
import com.squareup.tools.mdxdaemonplugin.model.Source
import org.jetbrains.plugins.gradle.util.GradleConstants

/**
 * MdxExternalSystemListener is a listener for Gradle build and sync events.
 * It cancels the background build when a Gradle build starts or when a sync starts, and starts the background build
 * when a Gradle sync completes successfully.
 */
class MdxExternalSystemListener : ExternalSystemTaskNotificationListener {
    private val configurationCacheUseCase: MdxDaemon = MdxDaemon.configurationCacheUseCase()

    override fun onStart(projectPath: String, id: ExternalSystemTaskId) {
        val project = id.findProject() ?: return
        val source = if (id.isGradleResolveProjectTask) Source.OnSyncStart else Source.OnIdeBuildStart
        println("MdxDaemon: Gradle SYNC/BUILD started for project ${project.basePath} - source: $source")
        configurationCacheUseCase.cancelBackgroundBuild(project, source)
    }

    override fun onSuccess(projectPath: String, id: ExternalSystemTaskId) {
        if (!id.isGradleResolveProjectTask) return
        val project = id.findProject() ?: return
        println("MdxDaemon: Gradle SYNC finished for project ${project.basePath}")
        configurationCacheUseCase.startBackgroundBuild(project, Source.OnSyncCompleteWithSuccess)
    }
}

internal val ExternalSystemTaskId.isGradleResolveProjectTask: Boolean
    get() = projectSystemId == GradleConstants.SYSTEM_ID && type == ExternalSystemTaskType.RESOLVE_PROJECT