package com.example.mybracketapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SLApplication: Application() {

    companion object {

        @SuppressLint("StaticFieldLeak")
        @JvmStatic private lateinit var context: Context

        fun setContext(con: Context) {
            context=con
        }

        @JvmStatic fun getContext(): Context {
            return context
        }
    }
}