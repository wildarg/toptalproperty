package com.homesoftwaretools.toptalproperty.repo.prefs

import android.content.Context
import androidx.preference.PreferenceManager

interface PrefsRepo {
    fun getNum(key: String): Float?
    fun setNum(key: String, value: Float)
}


class AndroidPrefsRepo(private val context: Context) : PrefsRepo {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    override fun getNum(key: String): Float? {
        val value = prefs.getFloat(key, 0f)
        return if (value == 0f) null else value
    }

    override fun setNum(key: String, value: Float) {
        prefs.edit()
            .putFloat(key, value)
            .apply()
    }

}