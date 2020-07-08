package com.homesoftwaretools.toptalproperty.core.utils

import java.util.*

interface DateProvider {
    fun fromMs(ms: Long?): Date?
    fun toMs(date: Date): Long
    fun now(): Date
}


class DateProviderImpl : DateProvider {

    override fun fromMs(ms: Long?): Date? {
        return ms?.let(::Date)
    }

    override fun toMs(date: Date): Long {
        return date.time
    }

    override fun now(): Date {
        return Date()
    }

}