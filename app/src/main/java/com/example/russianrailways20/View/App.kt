package com.example.russianrailways20.View

import android.app.Application
import com.example.russianrailways20.Model.DB.MainDB

class App : Application() {
    val DB by lazy {
        MainDB.createDB(this)
    }
}