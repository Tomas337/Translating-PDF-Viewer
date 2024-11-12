package io.github.tomas337.translating_pdf_viewer

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import io.github.tomas337.translating_pdf_viewer.di.MyTestApp

class TestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, MyTestApp::class.java.name, context)
    }
}