package com.hadihariri.teamcity.plugins.rundeck.agent

import jetbrains.buildServer.agent.ClasspathUtil
import jetbrains.buildServer.agent.plugins.beans.PluginDescriptor
import jetbrains.buildServer.agent.runner.BuildServiceAdapter
import jetbrains.buildServer.agent.runner.JavaCommandLineBuilder
import jetbrains.buildServer.agent.runner.JavaRunnerUtil
import jetbrains.buildServer.agent.runner.ProgramCommandLine
import jetbrains.buildServer.runner.JavaRunnerConstants
import jetbrains.buildServer.util.FileUtil
import jetbrains.buildServer.util.PropertiesUtil
import org.jdom.Element
import java.io.File
import java.util.*

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
        commandLine.setJvmArgs(JavaRunnerUtil.extractJvmArgs(runnerParameters))
        commandLine.setMainClass("com.hadihariri.teamcity.plugins.rundeck.agent.RunDeck")
        commandLine.setProgramArgs(listOf(buildParameters.systemProperties["teamcity.runner.properties.file"]))
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
        classpath.append(ClasspathUtil.composeClasspath(arrayOf(PropertiesUtil::class.java,
                Element::class.java, FileUtil::class.java,
                com.intellij.openapi.util.io.FileUtil::class.java,
                org.apache.log4j.Logger::class.java),null, null))
        return classpath.toString();

    }
}