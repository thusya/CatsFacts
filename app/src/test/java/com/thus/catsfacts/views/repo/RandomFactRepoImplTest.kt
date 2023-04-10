package com.thus.catsfacts.views.repo

import com.thus.catsfacts.model.CatRandomFact
import com.thus.catsfacts.model.DataMapper
import com.thus.catsfacts.network.ApiService
import com.thus.catsfacts.views.adapter.RandomCatFacts
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RandomFactRepoImplTest {

    val mockApiService: ApiService = mockk(relaxed = true)
    val mockDataMapper: DataMapper = mockk(relaxed = true)
    lateinit var instance: RandomFactRepoImpl
    val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun beforeEachTest() {
        Dispatchers.setMain(testDispatcher)
        instance =
            spyk(RandomFactRepoImpl(mockApiService, mockDataMapper), recordPrivateCalls = true)
    }

    @AfterEach
    fun afterEachTest() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Nested
    @DisplayName("when fetchRandomFact()")
    inner class FetchRandomFact {

        @DisplayName("then invokes apiService.getRandomFact()")
        @Test
        fun test() = runTest {
            val mockCatRandomFact: CatRandomFact = mockk(relaxed = true)
            val mockRandomCatFacts: RandomCatFacts = mockk(relaxed = true)
            every { mockDataMapper.toMap(mockCatRandomFact) } returns mockRandomCatFacts
            coEvery { mockApiService.getRandomFact() } returns mockCatRandomFact

            instance.fetchRandomFact().collectLatest {
                assertEquals(mockRandomCatFacts, it)
            }
            coVerify { mockApiService.getRandomFact() }
        }
    }

    @Nested
    @DisplayName("when delete(catId)")
    inner class Delete {

        @DisplayName("then invokes dataMapper.delete()")
        @Test
        fun test() = runTest {
            val mockCatId = "catId"

            instance.delete(mockCatId).collectLatest {
                assertEquals(mockCatId, it)
            }

            coVerify { mockDataMapper.delete(mockCatId) }
        }
    }

    @Nested
    @DisplayName("when fetchDetails()")
    inner class FetchDetails {

        @DisplayName("then return list of RandomCatFacts")
        @Test
        fun test() = runTest {
            val mockListRandomCatFacts:List<RandomCatFacts> = mockk(relaxed=true)
            coEvery { mockDataMapper.fetchCatDetails() } returns mockListRandomCatFacts

            val result = instance.fetchDetails()

            coVerify { mockDataMapper.fetchCatDetails() }
            assertEquals(mockListRandomCatFacts, result)
        }
    }
}