import com.hadihariri.teamcity.plugins.rundeck.agent.RunDeckAPI
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import jetbrains.buildServer.util.FileUtil
import org.junit.Test
import org.xml.sax.InputSource
import org.yaml.snakeyaml.reader.StreamReader
import java.io.InputStream
import java.io.StringReader
import javax.xml.parsers.SAXParserFactory
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

/**
 * Created by hadihariri on 05/10/15.
 */
public class RunDeckAPITests {

    // These tests need a local instance of RunDeck running and the corresponding job ID/token
    // TODO: Extra these into a separate file
    val host = "http://localhost:4440"
    val token = "mHQ5M054XM3XSiZoQ9MfOwb6KVECSNy5"
    val jobId = "ce8c3513-c950-4fed-a855-d974395bd8f8"

    @Test public fun executeJobShouldReturnExecutionIdWithValidParameters() {

        val runDeckAPI = RunDeckAPI(host, token)
        val response = runDeckAPI.executeJob(jobId)
        assertEquals(200, response.code)
        assertNotEquals("", response.id)
    }

    @Test public fun executeJobShouldReturnErrorInJobWithInvalidJob() {

        val runDeckAPI = RunDeckAPI(host, token)
        val response = runDeckAPI.executeJob("wrongjob-7dc2-43a3-88cf-571d4941112a")
        assertEquals(404, response.code)
        assertNotEquals("", response.id)
    }

    @Test public fun executeJobShouldReturn404WithInvalidAuthentication() {

        val runDeckAPI = RunDeckAPI(host, token)
        val response = runDeckAPI.executeJob(jobId)
        assertEquals(200, response.code)
        assertNotEquals("", response.id)
    }

}