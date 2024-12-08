package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.tomas337.translating_pdf_viewer.TestUtils
import io.github.tomas337.translating_pdf_viewer.data.local.bookmarks.BookmarkEntity
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
class BookmarksTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    companion object {
        @JvmStatic
        @BeforeClass
        fun setup() {
            TestUtils.copyTestPdfFileToExternalStorage()
        }

        @JvmStatic
        @AfterClass
        fun afterClassCleanup() {
            TestUtils.deleteTestPdfFileFromExternalStorage()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Before
    fun setScreen() {
        TestUtils.initIntent()
        composeTestRule.onNodeWithContentDescription("Add file button").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasContentDescription("Edit file info"), 5000L)
        composeTestRule.onNodeWithContentDescription("File: test").performClick()
        composeTestRule.onNodeWithContentDescription("Return to home screen").assertIsDisplayed()
    }

    @After
    fun afterTestCleanup() {
        Intents.release()
        TestUtils.deletePreviousFileItem()
    }

    private suspend fun fillDbWithBookmarks(n: Int) {
        for (i in 1..n) {
            val fileId = MyApp.db.fileInfoDao().run {
                getLastInsertedFileId()
            }
            MyApp.db.bookmarksDao().run {
                addBookmark(
                    BookmarkEntity(
                        fileId = fileId,
                        pageIndex = i,
                        text = "Bookmark $i"
                    )
                )
            }
        }
    }

    @Test
    fun bookmarks_addAndRemoveButton() {
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
        runBlocking {
            fillDbWithBookmarks(1)
        }
        composeTestRule.onNodeWithContentDescription("Page 0").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Bookmarks").performClick()
        composeTestRule.onNodeWithContentDescription("Bookmark list")
            .onChildren()
            .filterToOne(hasText("Bookmark 1"))
            .performClick()
        composeTestRule.onNodeWithContentDescription("Page 1").assertIsDisplayed()
    }

    @Test
    fun bookmarks_select_rename_delete() {
        runBlocking {
            // TODO fix: causes test to fail, because it isn't built to handle multiple bookmarks
            fillDbWithBookmarks(2)
        }
        composeTestRule.onNodeWithContentDescription("Bookmarks").performClick()

        // TODO: prefill the db with bookmarks to isolate tests from the button
        composeTestRule.onNodeWithContentDescription("Bookmark add/remove button").performClick()
        composeTestRule.onNodeWithContentDescription("Bookmark list")
            .onChildren()
            .filterToOne(hasText("Bookmark").and(hasText("1")))
            .performTouchInput { longClick() }

        // TODO: refactor code using "robots"
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

        //  Check that when the current page bookmark is removed using "Delete bookmark" add/remove button state changes
        composeTestRule.onNodeWithText("Add current page to bookmarks").assertIsDisplayed()

        // TODO: add tests where there are multiple bookmarks in the list,
        //  check that the correct one is checked, that you can check multiple,
        //  that bookmark can be renamed only if one is checked, etc.
    }

    @Test
    fun bookmarks_contents() {
        fail("unimplemented")
    }
}