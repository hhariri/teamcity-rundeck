package com.hadihariri.teamcity.plugins.rundeck.agent

/**
 * Created by hadihariri on 05/10/15.
 */
data class RunDeckExecuteJobResponse(val code: Int, val id: String)
data class RunDeckJobStatusResponse(val code: Int, val completed: Boolean = false, val execCompleted: Boolean = false, val hasFailedNodes: Boolean = false, val execState: String = "failed", val execDuration: Long = 0, val rawData: String = "")
