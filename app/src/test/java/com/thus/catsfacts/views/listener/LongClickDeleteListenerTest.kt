package com.thus.catsfacts.views.listener

import android.view.View
import com.thus.catsfacts.views.RandomFactViewModel
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LongClickDeleteListenerTest {

    val mockRandomFactViewModel: RandomFactViewModel = mockk(relaxed = true)
    lateinit var instance: LongClickDeleteListener

    @BeforeEach
    fun beforeEachTest() {
        instance =
            spyk(LongClickDeleteListener(mockRandomFactViewModel), recordPrivateCalls = true)
    }

    @AfterEach
    fun afterEachTest() {
        clearAllMocks()
    }

    @Nested
    @DisplayName("when onLongClick(view)")
    inner class OnLongClick {

        @DisplayName("then invokes viewModel.delete()")
        @Test
        fun test() {
            val mockTag = "mockTag"
            val mockView: View = mockk(relaxed = true)
            every { mockView.tag } returns mockTag

            val result = instance.onLongClick(mockView)

            verify { mockRandomFactViewModel.delete(mockTag) }
            assertTrue(result)
        }
    }
}