package com.aviationdata.core.androidtest.rules

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.rules.ExternalResource

class BackendRule : ExternalResource() {

    private lateinit var server: MockWebServer
    lateinit var baseUrl: String

    override fun before() {
        super.before()

        server = MockWebServer()
        baseUrl = server.url("/").toString()
    }

    override fun after() {
        server.shutdown()
        super.after()
    }

    fun prepare(status: Int, response: String = ""): BackendRule {
        server.enqueue(
            MockResponse().apply {
                setResponseCode(status)
                setBody(response)
            }
        )
        return this
    }

    fun checkRequest(method: String = "GET", path: String = "/", parameters: Map<String, String>) {
        val request = server.takeRequest()
        assertEquals(method, request.method)
        assertTrue(request.path!!.contains(path))
        parameters.entries.forEach {
            assertEquals(it.value, request.requestUrl?.queryParameter(it.key))
        }
    }
}