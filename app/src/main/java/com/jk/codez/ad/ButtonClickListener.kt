package com.jk.codez.ad

interface ButtonClickListener {
    fun onSave(dialog: AestheticDialog.Builder)
    fun onCancel(dialog: AestheticDialog.Builder)
    fun onDelete(dialog: AestheticDialog.Builder)
}