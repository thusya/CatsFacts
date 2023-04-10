package com.thus.catsfacts.model

import com.thus.catsfacts.db.CatDetailDao
import com.thus.catsfacts.util.DateUtil
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class DataMapperTest {

    val mockCatDetailDao: CatDetailDao = mockk(relaxed = true)
    lateinit var instance: DataMapper
    val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun beforeEachTest() {
        Dispatchers.setMain(testDispatcher)
        instance =
            spyk(DataMapper(mockCatDetailDao), recordPrivateCalls = true)
    }

    @AfterEach
    fun afterEachTest() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Nested
    @DisplayName("when toMap(CatRandomFact)")
    inner class ToMap {

        @DisplayName("then invokes dao.insertCatDetails and return RandomCatFacts")
        @Test
        fun test() = runTest {
            mockkObject(DateUtil)
            every { DateUtil.parseDate(any()) } returns "date"
            val mockCatRandomFact: CatRandomFact = mockk(relaxed = true)

            instance.toMap(mockCatRandomFact)

            coVerify { mockCatDetailDao.insertCatDetails(any()) }
            mockkObject(DateUtil)
        }
    }

    @Nested
    @DisplayName("when fetchCatDetails()")
    inner class FetchCatDetails {

        @DisplayName("then invokes dao.fetchCatDetails and return list of RandomCatFacts")
        @Test
        fun test() = runTest {
            instance.fetchCatDetails()

            coVerify { mockCatDetailDao.fetchCatDetails() }
        }
    }

    @Nested
    @DisplayName("when delete(catId)")
    inner class Delete {

        @DisplayName("then invokes dao.deleteCatDetails(catId)")
        @Test
        fun test() = runTest {
            val mockCatId = "catId"

            instance.delete(mockCatId)

            coVerify { mockCatDetailDao.deleteCatDetails(mockCatId) }
        }
    }
}