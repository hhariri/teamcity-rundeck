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


    private fun createRequestUrl(action: String, query: String): String {
        val apiVersion = 13
        return "$host/api/$apiVersion/$action?$query&authtoken=$authToken"
    }

    fun  executeJob(jobId: String, jobOptions: String = "", filters: String = ""): RunDeckExecuteJobResponse {
        var query = ""
        if (jobOptions != "" && jobOptions != "null") {
            query = "argString=${URLEncoder.encode(jobOptions, "utf-8")}"
        }
        // TODO: not sure why getting a potential null back...check parsing of options
        if (filters != "" && filters != "null") {
            query += "&filter=${URLEncoder.encode(filters, "utf-8")}"
        }
        val client = OkHttpClient()
        val request = Request.Builder()
                .url(createRequestUrl("job/$jobId/run", query))
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
                .url(createRequestUrl("execution/$executionId/output", ""))
                .build()
        val response = client.newCall(request).execute()
        when (response.code()) {
            200 -> {
                val data = response.body().string()
                val element = FileUtil.parseDocument(StringReader(data), false)
                return RunDeckJobStatusResponse(200,
                        element.getChild("completed").text == "true",
                        element.getChild("execCompleted").text == "true",
                        element.getChild("hasFailedNodes").text == "true",
                        element.getChild("execState").text,
                        element.getChild("execDuration").text.toLong(),
                        data
                        )
            }
            else -> {
                return RunDeckJobStatusResponse(response.code(), rawData = response.body().string())
            }
        }
    }
}