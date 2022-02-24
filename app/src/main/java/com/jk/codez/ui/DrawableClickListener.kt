package com.jk.codez.ui

interface DrawableClickListener {
    enum class DrawablePosition {
        TOP, BOTTOM, LEFT, RIGHT
    }

    fun onDrawableClick(target: DrawablePosition?)
}