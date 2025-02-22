package com.ohuo.application

import android.app.Application
import com.ohuo.application.database.Db
import com.ohuo.application.database.DriverFactory
import com.ohuo.application.database.createDatabase


class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Db.instance = createDatabase(DriverFactory(this))
    }
}