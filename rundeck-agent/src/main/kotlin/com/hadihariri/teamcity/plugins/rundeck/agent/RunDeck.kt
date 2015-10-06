package com.hadihariri.teamcity.plugins.rundeck.agent

import jetbrains.buildServer.messages.serviceMessages.ServiceMessage
import jetbrains.buildServer.serverSide.BasePropertiesModel
import jetbrains.buildServer.serverSide.TeamCityProperties
import jetbrains.buildServer.util.PropertiesUtil
import java.io.File

/**
 * Created by hadihariri on 24/09/15.
 */

public object RunDeck {

    // TODO: Clean-up

    val RUNDECK_SUCCEEDED = 0
    val RUNDECK_FAILED = 1

    @JvmStatic public fun main(args: Array<String>) {
        initInternalProperties()
        System.exit(run(parseRunDeckOptions(args[0])))
    }

    private fun parseRunDeckOptions(propertiesFilename: String): RunDeckOptions {
        val file = File(propertiesFilename)
        val properties = PropertiesUtil.loadProperties(file)
        return RunDeckOptions(
                url = properties.get("runDeckUrl").toString(),
                authToken = properties.get("runDeckAPIToken").toString(),
                includeOutput = properties.get("runDeckIncludeOutput") == "true",
                failBuild = properties.get("runDeckFailBuild") == "true",
                waitFinish = properties.get("runDeckWaitForJob").toString() == "true",
                jobId = properties.get("runDeckJobID").toString(),
                jobOptions = properties.get("runDeckJobOptions").toString().replace('\n', ' '),
                filters = properties.get("runDeckNodeFilter").toString().replace('\n', ' ')
        )
    }

    private fun initInternalProperties() {
        object : TeamCityProperties() {
            init {
                TeamCityProperties.setModel(object : BasePropertiesModel() {
                    override fun getSystemProperty(key: String): String? {
                        return System.getProperty(key)
                    }

                    override fun getSystemProperties(): Map<String, String> {
                        return BasePropertiesModel.propertiesToMap(System.getProperties())
                    }
                })
            }
        }
    }

    private fun run(rundeckOptions: RunDeckOptions): Int {
        val rundeckAPI = RunDeckAPI(rundeckOptions.url, rundeckOptions.authToken)
        val execution = rundeckAPI.executeJob(rundeckOptions.jobId, rundeckOptions.jobOptions, rundeckOptions.filters)
        println(ServiceMessage.asString("RUNDECK", "Starting RunDeck Job ${rundeckOptions.jobId}"))
        var counter = 0
        var increment: Long = 1
        if (execution.code == 200) {
            println(ServiceMessage.asString("RUNDECK", "Job ${rundeckOptions.jobId} launched successfully with id ${execution.result}"))
            if (rundeckOptions.waitFinish) {
                while (counter < 100){
                    val status = rundeckAPI.jobStatus(execution.result)
                    if (status.code == 200 && status.execCompleted) {
                        println(ServiceMessage.asString("RUNDECK", "RunDeck Job completed with status ${status.execState}"))
                        if (status.execState != "succeeded") {
                            return RUNDECK_FAILED
                        } else {
                            return RUNDECK_SUCCEEDED
                        }
                    }
                    Thread.sleep(5000 * increment)
                    counter += 1
                    increment += 1
                }
            } else {
                println(ServiceMessage.asString("RUNDECK", "Not waiting for job to finish"))
                return RUNDECK_SUCCEEDED
            }
        }
        println(ServiceMessage.asString("RUNDECK", "Job launch failed with error ${execution.code}:${execution.result}"))
        return RUNDECK_FAILED
    }


}