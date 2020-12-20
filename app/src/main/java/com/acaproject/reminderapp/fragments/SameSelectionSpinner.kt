package com.acaproject.reminderapp.fragments

import android.content.Context
import android.util.AttributeSet
import android.widget.Spinner

class SameSelectionSpinner : androidx.appcompat.widget.AppCompatSpinner {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle)

    override fun setSelection(position: Int) {
        if(position == selectedItemPosition){
            onItemSelectedListener?.onItemSelected(this, selectedView, position, selectedItemId)
        }
        super.setSelection(position)
    }

    override fun setSelection(position: Int, animate: Boolean) {
        if(position == selectedItemPosition){
            onItemSelectedListener?.onItemSelected(this, selectedView, position, selectedItemId)
        }
        super.setSelection(position, animate)
    }
}