package io.github.tomas337.translating_pdf_viewer.ui.screens.home

import android.app.Activity
import android.app.Instrumentation
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.github.tomas337.translating_pdf_viewer.ui.main.MainActivity
import junit.framework.TestCase.fail
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun init() {
        Intents.init()
    }

    @After
    fun teardown() {
        Intents.release()
    }

    companion object {

        lateinit var testPdfUri: Uri
        private const val FILE_NAME = "test.pdf"

        @JvmStatic
        @BeforeClass
        fun copyTestPdfFileToExternalStorage() {
            val context = InstrumentationRegistry.getInstrumentation().context
            val assetManager = context.assets

            val values = ContentValues()

            values.put(MediaStore.MediaColumns.DISPLAY_NAME, FILE_NAME)
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)

            testPdfUri = context.contentResolver
                .insert(MediaStore.Files.getContentUri("external"), values)!!

            assetManager.open(FILE_NAME).use { `in` ->
                context.contentResolver.openOutputStream(testPdfUri).use { out ->
                    val buffer = ByteArray(1024)
                    var read: Int
                    while ((`in`.read(buffer).also { read = it }) != -1) {
                        out!!.write(buffer, 0, read)
                    }
                }
            }
        }
    }

    @Test
    fun clickFab_addItemToList() {

        // Setup the intent result for when an intent is sent to the specified package.
        val stubbedIntent = Intent()
        stubbedIntent.data = testPdfUri
        val stubbedResult = Instrumentation.ActivityResult(Activity.RESULT_OK, stubbedIntent)
        intending(toPackage("com.android.content")).respondWith(stubbedResult)

        composeTestRule.onNodeWithContentDescription("Add file button").performClick()

        fail("Unimplemented")
    }

    @Test
    fun item_showsProgressBar_thenShowVertMenu() {
        fail("Unimplemented")
    }

    @Test
    fun clickItem_navigateToPdfViewerScreen() {
        fail("Unimplemented")
    }

    @Test
    fun clickVertMenu_showContextMenu() {
        fail("Unimplemented")
    }

    @Test
    fun delete_deleteItemFromList() {
        fail("Unimplemented")
    }

    @Test
    fun renameDialog_saveAndCancel() {
        fail("Unimplemented")
    }

}