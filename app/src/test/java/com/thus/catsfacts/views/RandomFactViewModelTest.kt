package com.thus.catsfacts.views

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.thus.catsfacts.invokeMethod
import com.thus.catsfacts.setProperty
import com.thus.catsfacts.util.Connectivity
import com.thus.catsfacts.views.repo.RandomFactRepo
import io.mockk.CapturingSlot
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RandomFactViewModelTest {

    val mockRandomFactRepo: RandomFactRepo = mockk(relaxed = true)
    val mockLiveData: MutableLiveData<State> = mockk(relaxed = true)
    lateinit var instance: RandomFactViewModel
    val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun beforeEachTest() {
        Dispatchers.setMain(testDispatcher)
        instance =
            spyk(RandomFactViewModel(mockRandomFactRepo, testDispatcher), recordPrivateCalls = true)
        instance.setProperty("liveData", mockLiveData)
    }

    @AfterEach
    fun afterEachTest() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Nested
    @DisplayName("when loadFromDb()")
    inner class LoadFromDb {

        @DisplayName("then invokes liveData.postValue(RenderData)")
        @Test
        fun test() = runTest {
            val slot = CapturingSlot<State.RenderData>()
            coEvery { mockRandomFactRepo.fetchDetails() } returns mockk(relaxed = true)

            instance.loadFromDb()

            verify { mockLiveData.postValue(capture(slot)) }
            Assertions.assertEquals(
                State.RenderData::class,
                slot.captured::class
            )
            coVerify { mockRandomFactRepo.fetchDetails() }
        }
    }

    @Nested
    @DisplayName("when getRandomFact()")
    inner class GetRandomFact {

        @DisplayName("then invokes liveData.postValue(Add)")
        @Test
        fun test() {
            val slot = CapturingSlot<State.Add>()
            every { mockRandomFactRepo.fetchRandomFact().catch { } } returns flowOf(mockk())

            instance.invokeMethod("getRandomFact")

            verify { mockLiveData.postValue(capture(slot)) }
            Assertions.assertEquals(
                State.Add::class,
                slot.captured::class
            )
            verify { mockRandomFactRepo.fetchRandomFact() }
        }

        @Nested
        @DisplayName("given randomFactRepo.fetchRandomFact() throw exception")
        inner class Excpetion {
            @DisplayName("then invokes liveData.postValue(Error)")
            @Test
            fun test() {
                val slot = CapturingSlot<State.Error>()
                every {
                    mockRandomFactRepo.fetchRandomFact().catch { }
                } returns flow { throw Throwable() }

                instance.invokeMethod("getRandomFact")

                verify { mockLiveData.postValue(capture(slot)) }
                Assertions.assertEquals(
                    State.Error::class,
                    slot.captured::class
                )
                verify { mockRandomFactRepo.fetchRandomFact() }
            }
        }
    }

    @Nested
    @DisplayName("when setObserver()")
    inner class SetObserver {

        @DisplayName("then invokes liveData.observe(owner, observer)")
        @Test
        fun test() {
            val mockLifecycleOwner: LifecycleOwner = mockk()
            val mockObserverState: Observer<State> = mockk()

            instance.setObserver(mockLifecycleOwner, mockObserverState)

            verify { mockLiveData.observe(mockLifecycleOwner, mockObserverState) }
        }
    }

    @Nested
    @DisplayName("when delete(catId)")
    inner class Delete {

        @DisplayName("then invokes liveData.postValue(Add)")
        @Test
        fun test() {
            val slot = CapturingSlot<State.Delete>()
            val mockCatId = "catId"
            every { mockRandomFactRepo.delete(mockCatId).catch { } } returns flowOf("Test")

            instance.delete(mockCatId)

            verify { mockLiveData.postValue(capture(slot)) }
            Assertions.assertEquals(
                State.Delete::class,
                slot.captured::class
            )
            verify { mockRandomFactRepo.delete(mockCatId) }
        }

        @Nested
        @DisplayName("given randomFactRepo.delete(catId) throw exception")
        inner class Exception {
            @DisplayName("then invokes liveData.postValue(Error)")
            @Test
            fun test() {
                val slot = CapturingSlot<State.Error>()
                val mockCatId = "catId"
                every {
                    mockRandomFactRepo.delete(mockCatId).catch { }
                } returns flow { throw Throwable() }

                instance.delete(mockCatId)

                verify { mockLiveData.postValue(capture(slot)) }
                Assertions.assertEquals(
                    State.Error::class,
                    slot.captured::class
                )
                verify { mockRandomFactRepo.delete(mockCatId) }
            }
        }
    }

    @Nested
    @DisplayName("when loadFacts(context)")
    inner class LoadFacts {
        @Nested
        @DisplayName("given internet connection")
        inner class InternetConnection {
            @DisplayName("then invokes getRandomFact()")
            @Test
            fun test() {
                mockkObject(Connectivity)
                val mockContext: Context = mockk()
                every {
                    Connectivity.isConnected(mockContext)
                } returns true

                instance.loadFacts(mockContext)

                verify { instance.invokeMethod("getRandomFact") }
                verify(exactly = 0) { instance.invokeMethod("loadFromDb") }
                unmockkObject(Connectivity)
            }
        }

        @Nested
        @DisplayName("given without internet connection")
        inner class WithoutInternetConnection {
            @DisplayName("then invokes loadFromDb()")
            @Test
            fun test() {
                mockkObject(Connectivity)
                val mockContext: Context = mockk()
                every {
                    Connectivity.isConnected(mockContext)
                } returns false
                every { mockContext.getString(any()) } returns "test"

                instance.loadFacts(mockContext)

                verify(exactly = 0) { instance.invokeMethod("getRandomFact") }
                verify { instance.invokeMethod("loadFromDb") }
                unmockkObject(Connectivity)
            }
        }
    }
}