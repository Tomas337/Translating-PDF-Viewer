package io.github.tomas337.translating_pdf_viewer.di

import android.app.Application
import io.github.tomas337.translating_pdf_viewer.data.local.FileDatabase
import io.github.tomas337.translating_pdf_viewer.data.local.TestFileDatabase

class MyApp : Application() {

    companion object {
        lateinit var db: FileDatabase
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        db = TestFileDatabase.getFileDatabase(this)
        appModule = AppModuleImpl(this, db)
    }
}
