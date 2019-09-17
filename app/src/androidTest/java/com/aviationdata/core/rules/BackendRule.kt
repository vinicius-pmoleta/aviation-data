package com.aviationdata.core.rules

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.rules.ExternalResource

class BackendRule : ExternalResource() {

    lateinit var server: MockWebServer
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

    fun check(method: String, queryParameters: Map<String, String>) {
        val request = server.takeRequest()
        assertEquals(method, request.method)
        queryParameters.entries.forEach {
            assertEquals(it.value, request.requestUrl?.queryParameter(it.key))
        }
    }

}