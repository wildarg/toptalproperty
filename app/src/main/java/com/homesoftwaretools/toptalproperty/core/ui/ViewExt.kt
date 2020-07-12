package com.homesoftwaretools.toptalproperty.core.ui

import android.view.View

fun View.onClick(listener: (View) -> Unit) {
    this.setOnClickListener(listener)
}

var View.visible: Boolean
    get() = this.visibility == View.VISIBLE
    set(value) { this.visibility = if (value) View.VISIBLE else View.GONE }