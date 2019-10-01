package com.aviationdata.features.search

import com.aviationdata.search.business.Pagination
import com.aviationdata.search.business.SearchBusiness
import com.aviationdata.search.business.SearchData
import com.aviationdata.search.data.SearchRemoteRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchBusinessTest {

    private val remoteRepository: com.aviationdata.search.data.SearchRemoteRepository = mock()
    private val business =
        com.aviationdata.search.business.SearchBusiness(remoteRepository)

    @Test
    fun `verify search is performed on remote repository`() = runBlockingTest {
        val query = "query"

        val data = com.aviationdata.search.business.SearchData(
            results = listOf(mock()),
            pagination = com.aviationdata.search.business.Pagination()
        )
        whenever(remoteRepository.search(query, 1)).thenReturn(data)

        val results = business.search(query)

        assertNotNull(results)
        assertEquals(data, results)
    }
}