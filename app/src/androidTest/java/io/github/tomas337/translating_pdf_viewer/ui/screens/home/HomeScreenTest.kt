package io.github.tomas337.translating_pdf_viewer.ui.screens.home

import android.app.Activity
import android.app.Instrumentation
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.github.tomas337.translating_pdf_viewer.ui.main.MainActivity
import junit.framework.TestCase.fail
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

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

        @JvmStatic
        @AfterClass
        fun deleteTestPdfFileFromExternalStorage() {
            // TODO: implement
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun clickFab_addItemToList() {

        // TODO: the result doesn't get stubbed, the app actually opens file selection
        // Setup the intent result for when an intent is sent to the specified package.
        val stubbedIntent = Intent()
        stubbedIntent.data = testPdfUri
        val stubbedResult = Instrumentation.ActivityResult(Activity.RESULT_OK, stubbedIntent)
        intending(toPackage("com.android.content")).respondWith(stubbedResult)

        composeTestRule.onNodeWithContentDescription("Add file button").performClick()

//        // 1) confirm that an item was added
//        composeTestRule.onNodeWithContentDescription("File: test").assertExists()
//
//        // 2) confirm that the item is being processed
//        // There will be a collision if there are more items with the same content description
//        composeTestRule.onNodeWithContentDescription("File: test").assertHasNoClickAction()
//        composeTestRule.onNodeWithContentDescription("File extraction progress bar").assertExists()
        // TODO assert color change of text and thumbnail

        // TODO somehow wait till the item is extracted
        // 3) confirm that the item is extracted
//        composeTestRule.onNodeWithContentDescription("File: test").assertHasClickAction()
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