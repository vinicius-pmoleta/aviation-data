package com.aviationdata.search

import com.aviationdata.core.structure.Aircraft
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchRemoteRepositoryTest {

    private val service: com.aviationdata.search.data.SearchService = mock()
    private val repository = com.aviationdata.search.data.SearchRemoteRepository(service)

    @Test
    fun `verify service empty raw models converted to empty domain models`() = runBlockingTest {
        val query = "query"
        val page = 1

        val rawResponse = mockSearchRawModels(0)
        mockSearchService(query, page, rawResponse)

        val data = repository.search(query, page)

        assertNotNull(data)
        assertTrue(data.results.isEmpty())
        assertEquals(query, data.query)
        assertEquals(page, data.pagination.page)
        assertEquals(0, data.pagination.totalPages)
    }

    @Test
    fun `verify service results are converted from raw to domain models`() = runBlockingTest {
        val query = "query"
        val page = 1

        val rawResponse = mockSearchRawModels(1)
        mockSearchService(query, page, rawResponse)

        val data = repository.search(query, page)

        assertNotNull(data)
        assertEquals(1, data.results.size)
        assertEquals(query, data.query)
        assertEquals(page, data.pagination.page)
        assertEquals(page, data.pagination.totalPages)
        assertRawToDomainConversion(rawResponse.results.first(), data.results.first())
    }

    private suspend fun mockSearchService(
        query: String,
        page: Int,
        rawResponse: com.aviationdata.search.data.RawSearchResponse
    ) {
        whenever(
            service.search(
                query,
                page,
                com.aviationdata.search.data.DEFAULT_SEARCH_RESULTS_PER_PAGE,
                com.aviationdata.search.data.DEFAULT_SEARCH_SORT_BY_FIELD,
                com.aviationdata.search.data.DEFAULT_SEARCH_SORT_ORDER_FIELD
            )
        ).thenReturn(rawResponse)
    }

    private fun mockSearchRawModels(size: Int = 0): com.aviationdata.search.data.RawSearchResponse {
        val rawResults = mutableListOf<com.aviationdata.search.data.RawSearchResult>()
        repeat(size) {
            rawResults.add(
                com.aviationdata.search.data.RawSearchResult(
                    icao24 = "icao24$it",
                    registration = "registration$it",
                    model = "model$it",
                    operator = "operator$it",
                    country = "country$it"
                )
            )
        }
        return com.aviationdata.search.data.RawSearchResponse(results = rawResults, pages = size)
    }

    private fun assertRawToDomainConversion(raw: com.aviationdata.search.data.RawSearchResult, domain: Aircraft) {
        assertEquals(raw.icao24, domain.identification.icao24)
        assertEquals(raw.registration, domain.identification.registration)
        assertEquals(raw.model, domain.information.model)
        assertEquals(raw.operator, domain.operation.operator)
        assertEquals(raw.country, domain.operation.country)
    }
}