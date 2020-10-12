package com.acaproject.reminderapp

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog

class DialogManager(val context: Context) {

    fun chooseCategoryDialog(): String? {
        val spinner = Spinner(context)

        val categories = arrayOf(
            context.getText(R.string.work),
            context.getText(R.string.hobby),
            context.getText(R.string.education),
            context.getText(R.string.sport),
            context.getText(R.string.pet),
            context.getText(R.string.family),
            context.getText(R.string.other)
        )
        var returnValue: String? = null
        val arrayAdapter =
            ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, categories)
        spinner.adapter = arrayAdapter
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Choose category to add")
            .setPositiveButton("Ok", null)
            .setNegativeButton("Cancel", null)
            .setView(spinner)
            .create()
        alertDialog.show()
        alertDialog.setOnShowListener {
            spinner.onItemClickListener = object : AdapterView.OnItemClickListener {
                override fun onItemClick(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                    returnValue = categories[pos] as String;
                }
            }
        }
        return returnValue;
    }
}