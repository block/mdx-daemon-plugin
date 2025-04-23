package com.squareup.tools.mdxdaemonplugin.usecase

import com.intellij.openapi.project.Project
import com.squareup.tools.mdxdaemonplugin.MdxDaemon
import com.squareup.tools.mdxdaemonplugin.locator.JobIdLocator
import com.squareup.tools.mdxdaemonplugin.model.Source
import com.squareup.tools.mdxdaemonplugin.util.FeatureFlagReader
import com.squareup.tools.mdxdaemonplugin.util.MdxDaemonClient
import com.squareup.tools.mdxdaemonplugin.util.PortChecker
import com.squareup.tools.mdxdaemonplugin.util.projectRootPath
import java.nio.file.Path

/**
 * This class is responsible for managing the configuration cache use case supported by the MdxDaemon.
 */
class ConfigurationCacheUseCase(
    private val client: MdxDaemonClient = MdxDaemonClient(),
    private val flagReader: FeatureFlagReader = FeatureFlagReader(),
    private val jobIdLocator: JobIdLocator = JobIdLocator(),
) : MdxDaemon {
    override fun startBackgroundBuild(
        project: Project,
        source: Source,
    ) {
        val projectRoot = project.projectRootPath() ?: return
        val jobId = jobIdLocator.locateJobId(project) ?: return

        println("MdxDaemon: Starting background build for job-id: $jobId")

        if (shouldTriggerDaemon(projectRoot, jobId)) {
            client.post("execute/$jobId", source)
        }
    }

    override fun cancelBackgroundBuild(
        project: Project,
        source: Source,
    ) {
        val projectRoot = project.projectRootPath() ?: return
        val jobId = jobIdLocator.locateJobId(project) ?: return

        println("MdxDaemon: Cancelling background build for job-id: $jobId")

        if (shouldTriggerDaemon(projectRoot, jobId)) {
            client.post("cancel/$jobId", source)
        }
    }

    private fun shouldTriggerDaemon(
        projectRoot: Path,
        jobId: String,
    ): Boolean {
        val port = System.getenv("MDX_DAEMON_PORT")?.toIntOrNull() ?: MdxDaemonClient.DEFAULT_PORT
        return flagReader.isJobEnabled(jobId, projectRoot) &&
            PortChecker.isPortOpen(port = port) &&
            System.getenv("KOCHIKU_ENV").isNullOrEmpty()
    }
}
