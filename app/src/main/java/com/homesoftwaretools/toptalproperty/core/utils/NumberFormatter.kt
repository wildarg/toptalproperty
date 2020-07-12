package com.homesoftwaretools.toptalproperty.core.utils

import com.homesoftwaretools.toptalproperty.R
import java.text.NumberFormat
import java.util.*

interface NumberFormatter {
    fun formatNum(num: Double): String
    fun formatInt(num: Int): String
    fun formatArea(num: Double): String
    fun formatRooms(num: Int): String
    fun formatCurrency(num: Double): String
    fun parseNum(src: String?): Double
    fun parseInt(src: String?): Int
    fun parseNumOrNull(src: String?): Double?
    fun parseIntOrNull(src: String?): Int?
}


class NumberFormatterImpl(private val rp: ResourceProvider) : NumberFormatter {

    private val formatter: NumberFormat by lazy { NumberFormat.getNumberInstance(Locale.getDefault()) }

    override fun formatNum(num: Double): String {
        return formatter.format(num)
    }

    override fun formatInt(num: Int): String {
        return formatter.format(num)
    }

    override fun formatArea(num: Double): String {
        return rp.string(R.string.area_format_string).format(formatNum(num))
    }

    override fun formatRooms(num: Int): String {
        return rp.string(R.string.rooms_format_string).format(formatInt(num))
    }

    override fun formatCurrency(num: Double): String {
        return rp.string(R.string.price_format_string).format(formatNum(num))
    }

    override fun parseNum(src: String?): Double {
        return formatter.parse(src.orEmpty())?.toDouble() ?: 0.0
    }

    override fun parseInt(src: String?): Int {
        return formatter.parse(src.orEmpty())?.toInt() ?: 0
    }

    override fun parseNumOrNull(src: String?): Double? {
        return try {
            src?.takeIf { it.isNotBlank() }?.let(this::parseNum)
        } catch(e: Throwable) {
            null
        }
    }

    override fun parseIntOrNull(src: String?): Int? {
        return try {
            src?.takeIf { it.isNotBlank() }?.let(this::parseInt)
        } catch (e: Throwable) {
            null
        }
    }
}