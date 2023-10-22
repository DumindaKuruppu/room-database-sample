package com.example.roomdatabasesample

import android.app.Application

class EmployeeApp : Application() {
    val database by lazy {
        EmployeeDatabase.getInstance(this)
    }
}