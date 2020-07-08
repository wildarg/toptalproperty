package com.homesoftwaretools.toptalproperty.core.ui

import android.view.View

fun View.onClick(listener: (View) -> Unit) {
    this.setOnClickListener(listener)
}