package com.hadihariri.teamcity.plugins.rundeck.agent

import jetbrains.buildServer.agent.plugins.beans.PluginDescriptor
import jetbrains.buildServer.agent.runner.BuildServiceAdapter
import jetbrains.buildServer.agent.runner.JavaCommandLineBuilder
import jetbrains.buildServer.agent.runner.JavaRunnerUtil
import jetbrains.buildServer.agent.runner.ProgramCommandLine
import jetbrains.buildServer.runner.JavaRunnerConstants
import java.io.File

/**
 * Created by hadihariri on 24/09/15.
 */
public class RunDeckService(val pluginDescriptor: PluginDescriptor): BuildServiceAdapter() {
    override fun beforeProcessStarted() {
        build.buildLogger.progressStarted("Starting RunDeck")
    }

    override fun makeProgramCommandLine(): ProgramCommandLine {
        val commandLine = JavaCommandLineBuilder()
        commandLine.setJavaHome(runnerParameters.get(JavaRunnerConstants.TARGET_JDK_HOME))
        commandLine.setBaseDir(checkoutDirectory.absolutePath)
        commandLine.setWorkingDir(workingDirectory.absolutePath)
        commandLine.setClassPath(getClasspath());
        commandLine.setEnvVariables(runnerContext.buildParameters.environmentVariables)
        commandLine.setJvmAjetbrrgs(JavaRunnerUtil.extractJvmArgs(runnerParameters))
        commandLine.setMainClass("com.hadihariri.teamcity.plugins.rundeck.agent.RunDeck")

        return commandLine.build()
    }

    private fun getClasspath(): String {
        val classpath = StringBuilder();
        val pluginRoot = this.pluginDescriptor.pluginRoot;
        val pluginLibs = File(pluginRoot, "lib");
        val separator = File.pathSeparator;
        val libs = pluginLibs.listFiles();
        if (libs != null) {
            for (lib in libs) {
                classpath.append(lib.absolutePath);
                classpath.append(separator);
            }
        }
        return classpath.toString();

    }
}