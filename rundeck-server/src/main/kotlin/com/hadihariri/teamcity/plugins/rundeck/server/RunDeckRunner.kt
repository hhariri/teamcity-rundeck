package com.hadihariri.teamcity.plugins.rundeck.server

import RUNDECK_RUNNER_JSP_FILE
import RUNDECK_RUNNER_TYPE
import jetbrains.buildServer.serverSide.PropertiesProcessor
import jetbrains.buildServer.serverSide.RunType
import jetbrains.buildServer.serverSide.RunTypeRegistry
import jetbrains.buildServer.web.openapi.PluginDescriptor


public class RunDeckRunner(val runTypeRegistry: RunTypeRegistry, val pluginDescriptor: PluginDescriptor) : RunType() {
    init {
        runTypeRegistry.registerRunType(this)
    }

    override fun getViewRunnerParamsJspFilePath(): String? {
        return null
    }

    override fun getEditRunnerParamsJspFilePath(): String? {
        return pluginDescriptor.getPluginResourcesPath(RUNDECK_RUNNER_JSP_FILE)
    }

    override fun getRunnerPropertiesProcessor(): PropertiesProcessor? {
        return null
    }

    override fun getDefaultRunnerProperties(): MutableMap<String, String>? {
        return null
    }

    override fun getType(): String {
        return RUNDECK_RUNNER_TYPE
    }

    override fun getDisplayName(): String {
        return "RunDeck"
    }

    override fun getDescription(): String {
        return "RunDeck Executor"
    }

}