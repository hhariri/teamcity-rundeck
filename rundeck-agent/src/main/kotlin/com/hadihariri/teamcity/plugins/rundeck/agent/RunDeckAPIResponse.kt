package com.hadihariri.teamcity.plugins.rundeck.agent

import jetbrains.buildServer.util.PropertiesUtil
import java.io.File

/**
 * Created by hadihariri on 05/10/15.
 */
data class RunDeckExecuteJobResponse(val code: Int, val result: String)

data class RunDeckJobStatusResponse(val code: Int, val completed: Boolean = false, val execCompleted: Boolean = false, val hasFailedNodes: Boolean = false, val execState: String = "failed", val execDuration: Long = 0, val rawData: String = "")
data class RunDeckOptions(val url: String, val authToken: String, val includeOutput: Boolean, val failBuild: Boolean, val waitFinish: Boolean, val jobId: String, val jobOptions: String, val filters: String)


