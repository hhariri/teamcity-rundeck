package com.hadihariri.teamcity.plugins.rundeck.agent

import jetbrains.buildServer.util.PropertiesUtil
import java.io.File

/**
 * Created by hadihariri on 24/09/15.
 */

public object RunDeck {
    @JvmStatic public fun main(args: Array<String>) {
        val file = File(args[0])
        val properties = PropertiesUtil.loadProperties(file)
        println("Reading parameters")
        val url = properties.get("runDeckUrl").toString()
        val authToken = properties.get("runDeckAPIToken").toString()
        val includeOutput = properties.get("runDeckIncludeOutput") == "true"
        val failBuild = properties.get("runDeckFailBuild") == "true"
        val wait = properties.get("runDeckWaitForJob").toString() == "true"
        val jobId = properties.get("runDeckJobID").toString()
        val options = properties.get("runDeckJobOptions").toString().replace('\n', ' ')
        val filters = properties.get("runDeckNodeFilter").toString().replace('\n', ' ')
        val rundeckAPI = RunDeckAPI(url, authToken)
        val execution = rundeckAPI.executeJob(jobId, options, filters)
        println("Job $jobId executed with response ${execution.code} - ${execution.id}")
        if (wait) {
            // busy wait to poll
            // includeOutput
            // failBuild
        }
    }
}