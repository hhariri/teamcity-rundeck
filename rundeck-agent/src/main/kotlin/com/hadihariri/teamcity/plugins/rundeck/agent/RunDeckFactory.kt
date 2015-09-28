package com.hadihariri.teamcity.plugins.rundeck.agent

import RUNDECK_RUNNER_TYPE
import jetbrains.buildServer.agent.AgentBuildRunnerInfo
import jetbrains.buildServer.agent.BuildAgentConfiguration
import jetbrains.buildServer.agent.plugins.beans.PluginDescriptor
import jetbrains.buildServer.agent.runner.CommandLineBuildService
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory

/**
 * Created by hadihariri on 24/09/15.
 */


public class RunDeckFactory(val pluginDescriptor: PluginDescriptor): CommandLineBuildServiceFactory {
    override fun getBuildRunnerInfo(): AgentBuildRunnerInfo {
        return object : AgentBuildRunnerInfo {
            override fun canRun(p0: BuildAgentConfiguration): Boolean {
                return true
            }

            override fun getType(): String {
                return RUNDECK_RUNNER_TYPE
            }

        }
    }

    override fun createService(): CommandLineBuildService {
        return RunDeckService(pluginDescriptor)
    }

}

