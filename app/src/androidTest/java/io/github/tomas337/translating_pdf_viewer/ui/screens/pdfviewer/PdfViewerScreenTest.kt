package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import android.app.Activity
import android.app.Instrumentation
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.github.tomas337.translating_pdf_viewer.di.MyApp
import io.github.tomas337.translating_pdf_viewer.ui.main.MainActivity
import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PdfViewerScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

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

    @OptIn(ExperimentalTestApi::class)
    @Before
    fun setScreen() {
        Intents.init()
        val stubbedIntent = Intent()
        stubbedIntent.data = testPdfUri
        val stubbedResult = Instrumentation.ActivityResult(Activity.RESULT_OK, stubbedIntent)
        intending(IntentMatchers.hasAction(Intent.ACTION_CHOOSER)).respondWith(stubbedResult)
        composeTestRule.onNodeWithContentDescription("Add file button").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasContentDescription("Edit file info"), 5000L)
        composeTestRule.onNodeWithContentDescription("File: test").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasContentDescription("Return to home screen"), 5000L)
        // TODO fix: the screen doesn't get displayed consistently
        composeTestRule.onNodeWithContentDescription("Return to home screen").assertIsDisplayed()
    }

    @After
    fun teardown() {
        Intents.release()
    }

    @After
    fun deletePreviousFileItem() {
        runBlocking {
            MyApp.db.fileInfoDao().run {
                deleteFile(getLastInsertedFileId())
            }
        }
    }

    // TODO: don't test content displaying yet since it may change
//    @Test
//    fun pageSliderBehaviour() {
//        fail("unimplemented")
//    }
//
//    @Test
//    fun scrollBehaviour() {
//        fail("unimplemented")
//    }

    @Test
    fun settingsBehaviour() {
        fail("unimplemented")
    }

    @Test
    fun bookmarks_add_remove() {
        composeTestRule.onNodeWithContentDescription("Bookmarks").performClick()

        composeTestRule.onNodeWithContentDescription("Bookmark add/remove button").assertHasClickAction()
        composeTestRule.onNodeWithText("Add current page to bookmarks").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Bookmark list")
            .onChildren()
            .filterToOne(hasText("Bookmark").and(hasText("1")))
            .assertDoesNotExist()

        composeTestRule.onNodeWithContentDescription("Bookmark add/remove button").performClick()
        composeTestRule.onNodeWithText("Remove current page from bookmarks").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Bookmark list")
            .onChildren()
            .filterToOne(hasText("Bookmark").and(hasText("1")))
            .assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Bookmark add/remove button").performClick()
        composeTestRule.onNodeWithText("Add current page to bookmarks").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Bookmark list")
            .onChildren()
            .filterToOne(hasText("Bookmark").and(hasText("1")))
            .assertDoesNotExist()
    }

    @Test
    fun bookmarks_goTo() {
        fail("unimplemented")
    }

    @Test
    fun bookmarks_select_rename_delete() {
        composeTestRule.onNodeWithContentDescription("Bookmarks").performClick()

        composeTestRule.onNodeWithContentDescription("Bookmark add/remove button").performClick()
        composeTestRule.onNodeWithContentDescription("Bookmark list")
            .onChildren()
            .filterToOne(hasText("Bookmark").and(hasText("1")))
            .performTouchInput { longClick() }

        composeTestRule.onNodeWithContentDescription("Checked circle").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Delete bookmark").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Rename bookmark").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Checked circle").performClick()
        composeTestRule.onNodeWithContentDescription("Unchecked circle").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Delete bookmark").assertIsNotDisplayed()
        composeTestRule.onNodeWithContentDescription("Rename bookmark").assertIsNotDisplayed()

        composeTestRule.onNodeWithContentDescription("Unchecked circle").performClick()
        composeTestRule.onNodeWithContentDescription("Checked circle").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Delete bookmark").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Rename bookmark").assertIsDisplayed()

        composeTestRule.onNodeWithText("Renamed bookmark").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Rename bookmark").performClick()
        composeTestRule.onNodeWithContentDescription("Bookmark name text field").performTextReplacement("Renamed bookmark")
        composeTestRule.onNodeWithText("CANCEL").performClick()
        composeTestRule.onNodeWithText("Renamed bookmark").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Rename bookmark").performClick()
        composeTestRule.onNodeWithContentDescription("Bookmark name text field").performTextReplacement("Renamed bookmark")
        composeTestRule.onNodeWithText("SAVE").performClick()
        composeTestRule.onNodeWithText("Renamed bookmark").assertExists()

        composeTestRule.onNodeWithContentDescription("Delete bookmark").performClick()
        composeTestRule.onNodeWithText("Renamed bookmark").assertDoesNotExist()
        // TODO: check that when the current page bookmark is removed using "Delete bookmark" add/remove button state changes

        // TODO: add tests where there are multiple bookmarks in the list,
        //  check that the correct one is checked, that you can check multiple,
        //  that bookmark can be renamed only if one is checked, etc.
    }

    @Test
    fun bookmarks_contents() {
//        composeTestRule.onNodeWithContentDescription("Bookmarks").performClick()
//        composeTestRule.onNodeWithText("Contents").assertDoesNotExist()
        fail("unimplemented")
    }

    @Test
    fun searchBehaviour() {
        fail("unimplemented")
    }
}
