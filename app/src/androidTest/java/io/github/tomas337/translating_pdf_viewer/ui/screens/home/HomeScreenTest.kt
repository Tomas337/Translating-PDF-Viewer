package io.github.tomas337.translating_pdf_viewer.ui.screens.home

import android.app.Activity
import android.app.Instrumentation
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
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
            val context = InstrumentationRegistry.getInstrumentation().context
            context.contentResolver.delete(testPdfUri, null, null)
        }
    }

    @Before
    fun addFileItem() {
        // Setup the intent result for when an intent is sent to the specified package.
        val stubbedIntent = Intent()
        stubbedIntent.data = testPdfUri
        val stubbedResult = Instrumentation.ActivityResult(Activity.RESULT_OK, stubbedIntent)
        intending(IntentMatchers.hasAction(Intent.ACTION_CHOOSER)).respondWith(stubbedResult)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun processFileItem() {

        // Setup the intent result for when an intent is sent to the specified package.
//        val stubbedIntent = Intent()
//        stubbedIntent.data = testPdfUri
//        val stubbedResult = Instrumentation.ActivityResult(Activity.RESULT_OK, stubbedIntent)
//        intending(IntentMatchers.hasAction(Intent.ACTION_CHOOSER)).respondWith(stubbedResult)

        composeTestRule.onNodeWithContentDescription("Add file button").performClick()

        // 1) confirm that an item was added
        composeTestRule.waitUntilExactlyOneExists(hasContentDescription("File: test"), 5000L)
        composeTestRule.onNodeWithContentDescription("Thumbnail").assertIsDisplayed()
        composeTestRule.onNodeWithText("test").assertIsDisplayed()

        // 2) confirm that the item is being processed
        composeTestRule.onNodeWithContentDescription("File: test").assertIsNotEnabled()
        composeTestRule.onNodeWithContentDescription("File extraction progress bar").assertIsDisplayed()

        // 3) confirm that the item is extracted
        composeTestRule.waitUntilDoesNotExist(hasContentDescription(
            "File extraction progress bar"
        ))
        composeTestRule.onNodeWithContentDescription("File: test").assertIsEnabled()
        composeTestRule.onNodeWithContentDescription("Edit file info").assertIsDisplayed()
    }

    @Test
    fun clickItem_navigateToPdfViewerScreen() {
        fail("Unimplemented")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun rename_delete_fileItem() {
        // TODO add an already processed file to db for tests that don't test processing
        composeTestRule.onNodeWithContentDescription("Add file button").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasContentDescription("Edit file info"))
        composeTestRule.onNodeWithContentDescription("Edit file info").performClick()

        // 1) confirm that the buttons are displayed
        composeTestRule.onNodeWithText("Edit name").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Edit icon").assertIsDisplayed()
        composeTestRule.onNodeWithText("Delete").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Trash icon").assertIsDisplayed()

        // 2) rename dialog behaviour
        composeTestRule.onNodeWithContentDescription("Edit icon").performClick()
        composeTestRule.onNodeWithContentDescription("Item name text field").performTextInput("New name")
        composeTestRule.onNodeWithText("CANCEL").performClick()
        composeTestRule.onNodeWithText("New name").assertDoesNotExist()

        composeTestRule.onNodeWithContentDescription("Edit file info").performClick()
        composeTestRule.onNodeWithContentDescription("Edit icon").performClick()
        composeTestRule.onNodeWithContentDescription("Item name text field").performTextReplacement("New name")
        composeTestRule.onNodeWithText("SAVE").performClick()
        composeTestRule.onNodeWithText("New name").assertExists()

        // 3) delete behaviour
        composeTestRule.onNodeWithContentDescription("Edit file info").performClick()
        composeTestRule.onNodeWithContentDescription("Trash icon").performClick()
        composeTestRule.onNodeWithContentDescription("File: test").assertDoesNotExist()
    }
}