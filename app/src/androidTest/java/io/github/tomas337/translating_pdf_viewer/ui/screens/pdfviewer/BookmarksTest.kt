package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertContentDescriptionContains
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.Espresso
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
    fun addAndRemoveButton() {
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
    fun goTo() {
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

    @OptIn(ExperimentalTestApi::class)
    private fun selectCircle(text: String, curState: Boolean) {
        if (curState) {
            composeTestRule.onNodeWithText(text)
                .assertContentDescriptionContains("Checked circle")
                .performClick()
            composeTestRule.waitUntilExactlyOneExists(hasText(text).and(hasContentDescription("Unchecked circle")), 10000L)
        } else {
            composeTestRule.onNodeWithText(text)
                .assertContentDescriptionContains("Unchecked circle")
                .performClick()
            composeTestRule.waitUntilExactlyOneExists(hasText(text).and(hasContentDescription("Checked circle")), 10000L)
        }
    }

    private fun checkExistenceOfBookmarks(n: Int, exist: Set<Int>) {
        for (i in 1..n) {
            if (i in exist) {
                composeTestRule.onNodeWithText("Bookmark $i").assertIsDisplayed()
            } else {
                composeTestRule.onNodeWithText("Bookmark $i").assertDoesNotExist()
            }
        }
    }

    @Test
    fun select_delete_rename() {
        val bookmarkCount = 5
        runBlocking {
            fillDbWithBookmarks(bookmarkCount)
        }
        composeTestRule.onNodeWithContentDescription("Bookmarks").performClick()


        // Select and unselect
        composeTestRule.onNodeWithText("Bookmark 1")
            .performTouchInput { longClick() }
            .assertContentDescriptionContains("Checked circle")
        composeTestRule.onNodeWithContentDescription("Delete bookmark").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Rename bookmark").assertIsDisplayed()

        selectCircle("Bookmark 2", false)
        composeTestRule.onNodeWithContentDescription("Delete bookmark").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Rename bookmark").assertDoesNotExist()
        selectCircle("Bookmark 3", false)
        composeTestRule.onNodeWithContentDescription("Delete bookmark").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Rename bookmark").assertDoesNotExist()
        selectCircle("Bookmark 4", false)
        composeTestRule.onNodeWithContentDescription("Delete bookmark").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Rename bookmark").assertDoesNotExist()
        selectCircle("Bookmark 5", false)
        composeTestRule.onNodeWithContentDescription("Delete bookmark").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Rename bookmark").assertDoesNotExist()

        selectCircle("Bookmark 1", true)
        composeTestRule.onNodeWithContentDescription("Delete bookmark").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Rename bookmark").assertDoesNotExist()
        selectCircle("Bookmark 2", true)
        composeTestRule.onNodeWithContentDescription("Delete bookmark").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Rename bookmark").assertDoesNotExist()
        selectCircle("Bookmark 3", true)
        composeTestRule.onNodeWithContentDescription("Delete bookmark").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Rename bookmark").assertDoesNotExist()
        selectCircle("Bookmark 4", true)
        composeTestRule.onNodeWithContentDescription("Delete bookmark").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Rename bookmark").assertIsDisplayed()
        selectCircle("Bookmark 5", true)
        composeTestRule.onNodeWithContentDescription("Delete bookmark").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Rename bookmark").assertDoesNotExist()


        // Selection mode state should reset when turned off and on
        composeTestRule.onNodeWithText("Bookmark 1").performClick()
        composeTestRule.onNodeWithText("Bookmark 2").performClick()
        composeTestRule.onNodeWithText("Bookmark 3").performClick()
        composeTestRule.onNodeWithText("Bookmark 4").performClick()
        composeTestRule.onNodeWithText("Bookmark 5").performClick()

        Espresso.pressBack()
        composeTestRule.onNodeWithContentDescription("Delete bookmark").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Rename bookmark").assertDoesNotExist()
        composeTestRule.onAllNodesWithContentDescription("Unchecked circle").assertCountEquals(0)
        composeTestRule.onAllNodesWithContentDescription("Checked circle").assertCountEquals(0)

        composeTestRule.onNodeWithText("Bookmark 2")
            .performTouchInput { longClick() }
        composeTestRule.onAllNodesWithContentDescription("Checked circle").assertCountEquals(1)
        composeTestRule.onAllNodesWithContentDescription("Unchecked circle").assertCountEquals(4)


        // Delete
        composeTestRule.onNodeWithText("Bookmark 1").performClick()
        composeTestRule.onNodeWithText("Bookmark 3").performClick()
        composeTestRule.onNodeWithContentDescription("Delete bookmark").performClick()
        checkExistenceOfBookmarks(bookmarkCount, setOf(4, 5))


        // Rename
        composeTestRule.onNodeWithText("Bookmark 5")
            .performTouchInput { longClick() }
            .assertContentDescriptionContains("Checked circle")

        composeTestRule.onNodeWithText("Renamed bookmark").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Rename bookmark").performClick()
        composeTestRule.onNodeWithContentDescription("Bookmark name text field")
            .performTextReplacement("Renamed bookmark")
        composeTestRule.onNodeWithText("CANCEL").performClick()
        composeTestRule.onNodeWithText("Renamed bookmark").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Rename bookmark").performClick()
        composeTestRule.onNodeWithContentDescription("Bookmark name text field")
            .performTextReplacement("Renamed bookmark")
        composeTestRule.onNodeWithText("SAVE").performClick()

        composeTestRule.onNodeWithText("Renamed bookmark").assertExists()
        composeTestRule.onNodeWithText("Bookmark 4").assertIsDisplayed()
    }

    @Test
    fun contents() {
        fail("unimplemented")
    }
}