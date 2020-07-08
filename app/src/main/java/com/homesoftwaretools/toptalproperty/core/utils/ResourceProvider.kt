package com.homesoftwaretools.toptalproperty.core.utils

import android.content.Context
import androidx.annotation.StringRes

interface ResourceProvider {
    fun string(@StringRes id: Int): String
}

class AndroidResourceProvider(private val app: Context) : ResourceProvider {

    override fun string(id: Int): String {
        return app.getString(id)
    }

}