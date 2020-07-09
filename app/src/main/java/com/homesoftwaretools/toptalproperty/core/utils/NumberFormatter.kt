package com.homesoftwaretools.toptalproperty.core.utils

import java.text.NumberFormat
import java.util.*

interface NumberFormatter {
    fun formatNum(num: Double): String
    fun formatInt(num: Int): String
    fun formatCurrency(num: Double): String
    fun parseNum(src: String?): Double
    fun parseInt(src: String?): Int
}


class NumberFormatterImpl : NumberFormatter {

    private val formatter: NumberFormat by lazy { NumberFormat.getNumberInstance(Locale.getDefault()) }

    override fun formatNum(num: Double): String {
        return formatter.format(num)
    }

    override fun formatInt(num: Int): String {
        return formatter.format(num)
    }

    override fun formatCurrency(num: Double): String {
        return formatter.format(num)
    }

    override fun parseNum(src: String?): Double {
        return formatter.parse(src.orEmpty())?.toDouble() ?: 0.0
    }

    override fun parseInt(src: String?): Int {
        return formatter.parse(src.orEmpty())?.toInt() ?: 0
    }
}