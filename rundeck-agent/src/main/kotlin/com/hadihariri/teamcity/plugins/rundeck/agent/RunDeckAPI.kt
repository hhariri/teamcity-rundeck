package com.hadihariri.teamcity.plugins.rundeck.agent

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import jetbrains.buildServer.util.FileUtil
import org.jdom.Element
import java.io.StringReader
import java.net.URLEncoder
import java.util.*

/**
 * Created by hadihariri on 05/10/15.
 */
public class RunDeckAPI(val host: String, val authToken: String) {


    private fun createRequestUrl(action: String, query: String): String {
        val apiVersion = 13
        return "$host/api/$apiVersion/$action?$query&authtoken=$authToken"
    }

    fun executeJob(jobId: String, jobOptions: String = "", filters: String = ""): RunDeckExecuteJobResponse {
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

    fun jobStatus(executionId: String, offset: Long = 0, lastModified: Long = 0): RunDeckJobStatusResponse {
        val client = OkHttpClient()
        val request = Request.Builder()
                .url(createRequestUrl("execution/$executionId/output", "offset=$offset&lastmod=$lastModified"))
                .build()
        val response = client.newCall(request).execute()
        when (response.code()) {
            200 -> {
                val data = response.body().string()
                val element = FileUtil.parseDocument(StringReader(data), false)

                val entries = ArrayList<String>()
                for (entry in element.getChild("entries").children) {
                    val elt = entry as Element
                    val node = elt.getAttribute("node").value
                    val time = elt.getAttribute("time").value
                    val level = elt.getAttribute("level").value
                    val log = elt.getAttribute("log").value
                    entries.add("%s [%s] %-8s %s".format(time, node, level, log))
                }

                return RunDeckJobStatusResponse(200,
                        element.getChild("completed").text == "true",
                        element.getChild("execCompleted").text == "true",
                        element.getChild("hasFailedNodes").text == "true",
                        element.getChild("execState").text,
                        element.getChild("execDuration").text.toLong(),
                        element.getChild("offset")?.text?.toLong() ?: 0L,
                        element.getChild("lastModified")?.text?.toLong() ?: 0L,
                        entries,
                        data
                )
            }
            else -> {
                return RunDeckJobStatusResponse(response.code(), rawData = response.body().string())
            }
        }
    }
}