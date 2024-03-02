package com.example.ocean

import android.graphics.drawable.Drawable

class MenuItem (
    private val icon:Drawable,
    private val title:String
) {
    val menuIcon:Drawable
        get() = icon

    val menuTitle:String
        get() = title
}