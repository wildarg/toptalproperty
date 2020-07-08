package com.homesoftwaretools.toptalproperty.core.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.homesoftwaretools.toptalproperty.logd

interface Toaster {
    fun toast(msg: String?)
    fun showError(cause: Throwable)
}

class AndroidToaster(private val context: Context) : Toaster {

    override fun toast(msg: String?) {
        logd { "CONTEXT: $context" }
        msg ?: return
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showError(cause: Throwable) {
        toast(cause.message)
    }
}