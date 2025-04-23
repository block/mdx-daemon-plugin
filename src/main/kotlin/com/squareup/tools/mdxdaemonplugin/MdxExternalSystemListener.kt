package com.squareup.tools.mdxdaemonplugin

import com.android.tools.idea.gradle.project.build.BuildContext
import com.android.tools.idea.gradle.project.build.BuildStatus
import com.android.tools.idea.gradle.project.build.GradleBuildListener
import com.android.tools.idea.gradle.project.sync.GradleSyncListenerWithRoot
import com.intellij.openapi.project.Project
import com.squareup.tools.mdxdaemonplugin.model.Source

/**
 * MdxExternalSystemListener is a listener for Gradle build and sync events.
 * It cancels the background build when a Gradle build starts or when a sync starts, and starts the background build
 * when a Gradle sync completes successfully.
 */
class MdxExternalSystemListener : GradleBuildListener, GradleSyncListenerWithRoot {
    private val configurationCacheUseCase: MdxDaemon = MdxDaemon.configurationCacheUseCase()

    override fun syncStarted(
        project: Project,
        rootProjectPath: String,
    ) {
        println("MdxDaemon: Gradle SYNC started for project $rootProjectPath")
        configurationCacheUseCase.cancelBackgroundBuild(project, Source.OnSyncStart)
    }

    override fun syncSucceeded(
        project: Project,
        rootProjectPath: String,
    ) {
        println("MdxDaemon: Gradle SYNC finished for project $rootProjectPath")
        configurationCacheUseCase.startBackgroundBuild(project, Source.OnSyncCompleteWithSuccess)
    }

    override fun buildStarted(context: BuildContext) {
        println("MdxDaemon: Gradle BUILD started")
        configurationCacheUseCase.cancelBackgroundBuild(context.project, Source.OnIdeBuildStart)
    }

    override fun buildFinished(
        status: BuildStatus,
        context: BuildContext,
    ) {
        // No-op
    }
}
