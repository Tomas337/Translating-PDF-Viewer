package io.github.tomas337.translating_pdf_viewer.ui.screens.home

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.tomas337.translating_pdf_viewer.TestUtils
import io.github.tomas337.translating_pdf_viewer.ui.main.MainActivity
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

    @Before
    fun addFileItem() {
        TestUtils.initIntent()
        composeTestRule.onNodeWithContentDescription("Add file button").performClick()
    }

    @After
    fun afterTestCleanup() {
        Intents.release()
        TestUtils.deletePreviousFileItem()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun processFileItem() {
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

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun clickItem_navigateToPdfViewerScreen() {
        composeTestRule.waitUntilExactlyOneExists(hasContentDescription("Edit file info"), 5000L)
        composeTestRule.onNodeWithContentDescription("File: test").performClick()
        composeTestRule.onNodeWithContentDescription("Return to home screen").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun rename_delete_fileItem() {
        composeTestRule.waitUntilExactlyOneExists(hasContentDescription("Edit file info"), 5000L)
        composeTestRule.onNodeWithContentDescription("Edit file info").performClick()

        // 1) confirm that the buttons are displayed
        composeTestRule.onNodeWithText("Edit name").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Edit icon").assertIsDisplayed()
        composeTestRule.onNodeWithText("Delete").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Trash icon").assertIsDisplayed()

        // 2) confirm that the dialog is displayed
        composeTestRule.onNodeWithContentDescription("Edit icon").performClick()
        composeTestRule.onNodeWithText("Update file name").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Item name text field").assertIsDisplayed()
        composeTestRule.onNodeWithText("CANCEL").assertExists()
        composeTestRule.onNodeWithText("SAVE").assertExists()

        // 3) rename dialog behaviour
        composeTestRule.onNodeWithContentDescription("Item name text field").performTextInput("New name")
        composeTestRule.onNodeWithText("CANCEL").performClick()
        composeTestRule.onNodeWithText("New name").assertDoesNotExist()

        composeTestRule.onNodeWithContentDescription("Edit file info").performClick()
        composeTestRule.onNodeWithContentDescription("Edit icon").performClick()
        composeTestRule.onNodeWithContentDescription("Item name text field").performTextReplacement("New name")
        composeTestRule.onNodeWithText("SAVE").performClick()
        composeTestRule.onNodeWithText("New name").assertExists()

        // 4) delete behaviour
        composeTestRule.onNodeWithContentDescription("Edit file info").performClick()
        composeTestRule.onNodeWithContentDescription("Trash icon").performClick()
        composeTestRule.onNodeWithContentDescription("File: test").assertDoesNotExist()
    }
}