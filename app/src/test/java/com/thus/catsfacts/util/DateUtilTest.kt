package com.thus.catsfacts.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DateUtilTest {

    @DisplayName("when invokes parseDate(date)")
    @Nested
    inner class ParseDate() {
        @DisplayName("then returns correct format date")
        @Test
        fun test() {
            val mockDate = "2023-11-02T02:50:12.208Z"

            val result = DateUtil.parseDate(mockDate)

            Assertions.assertEquals("2023-11-02 02:50:12", result)
        }
    }
}