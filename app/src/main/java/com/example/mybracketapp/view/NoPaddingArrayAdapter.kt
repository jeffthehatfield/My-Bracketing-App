package com.example.mybracketapp.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

open class NoPaddingArrayAdapter<T>(context: Context, layoutId: Int, items: List<T>) : ArrayAdapter<T>(context, layoutId, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        view.setPadding(0,view.paddingTop/2,view.paddingRight,view.paddingBottom)
        return view
    }
}