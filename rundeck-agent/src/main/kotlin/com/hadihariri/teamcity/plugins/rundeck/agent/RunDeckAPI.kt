package com.hadihariri.teamcity.plugins.rundeck.agent

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import jetbrains.buildServer.util.FileUtil
import java.io.StringReader
import java.net.URLEncoder

/**
 * Created by hadihariri on 05/10/15.
 */
public class RunDeckAPI(val host: String, val authToken: String) {

    // TODO: Refactor all this to use lambdas

    private fun createRequestUrl(action: String, query: String): String {
        val apiVersion = 13
        return "$host/api/$apiVersion/$action?$query&authtoken=$authToken"
    }

    fun  executeJob(jobId: String, jobOptions: String = "", filters: String = ""): RunDeckExecuteJobResponse {
        val jobOptionsEncoded = URLEncoder.encode(jobOptions, "utf-8")
        val filtersEncoded = URLEncoder.encode(filters, "utf-8")
        val client = OkHttpClient()
        val request = Request.Builder()
                .url(createRequestUrl("job/$jobId/run", "argString=$jobOptionsEncoded&filter=$filtersEncoded"))
                .build()
        val response = client.newCall(request).execute()
        when (response.code()) {
            200 -> {
                val id = response.body().string()
                val element = FileUtil.parseDocument(StringReader(id), false)
                return RunDeckExecuteJobResponse(200, element.getChild("execution").getAttribute("id").value)
            }
            403 -> {
                return RunDeckExecuteJobResponse(403, "Unauthorized")
            }
            404 -> {
                return RunDeckExecuteJobResponse(404, "Job not found")
            }
            else -> {
                return RunDeckExecuteJobResponse(response.code(), response.body().string())
            }
        }
    }

    // TODO: Need to parse entries
    fun jobStatus(executionId: String): RunDeckJobStatusResponse {
        val client = OkHttpClient()
        val request = Request.Builder()
                .url(createRequestUrl("/execution/$executionId/output", ""))
                .build()
        val response = client.newCall(request).execute()
        when (response.code()) {
            200 -> {
                val id = response.body().string()
                val element = FileUtil.parseDocument(StringReader(id), false)
                return RunDeckJobStatusResponse(200,
                        element.getChild("completed").text == "true",
                        element.getChild("execCompleted").text == "true",
                        element.getChild("hasFailedNodes").text == "true",
                        element.getChild("exectState").text,
                        element.getChild("execDuration").text.toLong(),
                        response.body().string()
                        )
            }
            else -> {
                return RunDeckJobStatusResponse(response.code(), rawData = response.body().string())
            }
        }
    }
}