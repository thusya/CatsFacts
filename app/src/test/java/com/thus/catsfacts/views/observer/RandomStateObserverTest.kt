package com.thus.catsfacts.views.observer

import com.thus.catsfacts.views.RandomFactView
import com.thus.catsfacts.views.State
import com.thus.catsfacts.views.adapter.RandomCatFacts
import io.mockk.CapturingSlot
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RandomStateObserverTest {

    val mockView: RandomFactView = mockk(relaxed = true)
    lateinit var instance: RandomStateObserver

    @BeforeEach
    fun beforeEachTest() {
        instance = spyk(RandomStateObserver(mockView), recordPrivateCalls = true)
    }

    @AfterEach
    fun afterEachTest() {
        clearAllMocks()
    }

    @Nested
    @DisplayName("when onChanged()")
    inner class OnChanged {

        @Nested
        @DisplayName("given state is State.RenderData")
        inner class RenderData {

            @DisplayName("then invokes view.changeState(RenderData)")
            @Test
            fun test() {
                val randomFacts = mockk<ArrayList<RandomCatFacts>>()
                val renderDataState = State.RenderData(randomFacts)
                val slot = CapturingSlot<RandomFactView.State>()

                instance.onChanged(renderDataState)

                verify { mockView.changeState(capture(slot)) }
                Assertions.assertEquals(
                    RandomFactView.State.RenderData::class,
                    slot.captured::class
                )
            }
        }

        @Nested
        @DisplayName("given state is State.Error")
        inner class Error {

            @DisplayName("then invokes view.changeState(Error)")
            @Test
            fun test() {
                val error = mockk<Throwable>()
                val errorState = State.Error(error)
                val slot = CapturingSlot<RandomFactView.State>()

                instance.onChanged(errorState)

                verify { mockView.changeState(capture(slot)) }
                Assertions.assertEquals(
                    RandomFactView.State.Error::class,
                    slot.captured::class
                )
            }
        }

        @Nested
        @DisplayName("given state is State.ShowToast")
        inner class ShowToast {

            @DisplayName("then invokes view.changeState(ShowToast)")
            @Test
            fun test() {
                val message = "Toast message"
                val showToastState = State.ShowToast(message)
                val slot = CapturingSlot<RandomFactView.State>()

                instance.onChanged(showToastState)

                verify { mockView.changeState(capture(slot)) }
                Assertions.assertEquals(
                    RandomFactView.State.ShowToast::class,
                    slot.captured::class
                )
            }
        }

        @Nested
        @DisplayName("given state is State.Add")
        inner class Add {

            @DisplayName("then invokes view.changeState(Add)")
            @Test
            fun test() {
                val fact = mockk<RandomCatFacts>()
                val addState = State.Add(fact)
                val slot = CapturingSlot<RandomFactView.State>()

                instance.onChanged(addState)

                verify { mockView.changeState(capture(slot)) }
                Assertions.assertEquals(
                    RandomFactView.State.Add::class,
                    slot.captured::class
                )
            }
        }

        @Nested
        @DisplayName("given state is State.Delete")
        inner class Delete {

            @DisplayName("then invokes view.changeState(Delete)")
            @Test
            fun test() {
                // GIVEN
                val catId = "123"
                val deleteState = State.Delete(catId)
                val slot = CapturingSlot<RandomFactView.State>()

                instance.onChanged(deleteState)

                verify { mockView.changeState(capture(slot)) }
                Assertions.assertEquals(
                    RandomFactView.State.Delete::class,
                    slot.captured::class
                )
            }
        }
    }
}