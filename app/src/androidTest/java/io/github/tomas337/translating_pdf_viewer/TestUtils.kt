package io.github.tomas337.translating_pdf_viewer

import android.app.Activity
import android.app.Instrumentation
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.platform.app.InstrumentationRegistry
import io.github.tomas337.translating_pdf_viewer.di.MyApp
import kotlinx.coroutines.runBlocking

object TestUtils {

    lateinit var testPdfUri: Uri
    private const val FILE_NAME = "test.pdf"

    @JvmStatic
    fun copyTestPdfFileToExternalStorage() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val assetManager = context.assets

        val values = ContentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, FILE_NAME)
        values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)

        testPdfUri = context.contentResolver
            .insert(MediaStore.Files.getContentUri("external"), values)!!

        assetManager.open(FILE_NAME).use { input ->
            context.contentResolver.openOutputStream(testPdfUri).use { output ->
                val buffer = ByteArray(1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output!!.write(buffer, 0, read)
                }
            }
        }
    }

    @JvmStatic
    fun deleteTestPdfFileFromExternalStorage() {
        val context = InstrumentationRegistry.getInstrumentation().context
        context.contentResolver.delete(testPdfUri, null, null)
    }

    fun initIntent() {
        Intents.init()
        val stubbedIntent = Intent()
        stubbedIntent.data = testPdfUri
        val stubbedResult = Instrumentation.ActivityResult(Activity.RESULT_OK, stubbedIntent)
        intending(IntentMatchers.hasAction(Intent.ACTION_CHOOSER)).respondWith(stubbedResult)
    }

    fun deletePreviousFileItem() {
        runBlocking {
            MyApp.db.fileInfoDao().run {
                deleteFile(getLastInsertedFileId())
            }
        }
    }
}