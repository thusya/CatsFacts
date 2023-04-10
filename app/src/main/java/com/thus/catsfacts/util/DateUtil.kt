package com.thus.catsfacts.util

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtil {

    private const val REQUIRED_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    private const val INPUT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    fun parseDate(date: String): String {
        val temp = SimpleDateFormat(INPUT_DATE_TIME_FORMAT, Locale.ENGLISH).parse(date)
        return SimpleDateFormat(REQUIRED_DATE_FORMAT, Locale.ENGLISH).format(temp)
    }
}
