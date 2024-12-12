package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAncestors
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.onSiblings
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.swipeUp
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.tomas337.translating_pdf_viewer.TestUtils
import io.github.tomas337.translating_pdf_viewer.di.MyApp
import io.github.tomas337.translating_pdf_viewer.ui.main.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsTest {

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
    fun setScreenAndOpenSettings() {
        TestUtils.initIntent()
        composeTestRule.onNodeWithContentDescription("Add file button").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasContentDescription("Edit file info"), 5000L)
        composeTestRule.onNodeWithContentDescription("File: test").performClick()
        composeTestRule.onNodeWithContentDescription("Return to home screen").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Display settings").performClick()
        composeTestRule.onNodeWithContentDescription("Settings sheet").assertIsDisplayed()
    }

    @After
    fun afterTestCleanup() {
        Intents.release()
        TestUtils.deletePreviousFileItem()
    }

    // TODO: settings test
    // Clicking a button changes the displayed content instantly.
    // You can input number into text field.
    // Test keyboard focus management.

    // When keyboard is displayed, click should hide it, unless it is on another text field,
    // in that case change focus to that text field.

    // Cancelling keyboard doesn't change the value
    // Text fields have a format.
    // Reset button.

    @Test
    fun sheet_expand_hide() {
        // Expand
        composeTestRule.onNodeWithContentDescription("Handle bar")
            .performTouchInput {
                down(center)
                moveBy(Offset(0f, y = -20000f), 0)
                advanceEventTime(1000)
                up()
            }

        val pageTop = composeTestRule.onNodeWithContentDescription("Page 0")
            .fetchSemanticsNode()
            .boundsInWindow
            .top
        val sheetTop = composeTestRule.onNodeWithContentDescription("Handle bar")
            .fetchSemanticsNode()
            .boundsInWindow
            .top
        val isContentHidden = pageTop == sheetTop

        assert(isContentHidden) {
            "Expected the top y coordinate of the sheet and the content to be equal."
        }
        composeTestRule.onNodeWithContentDescription("PdfViewer screen top bar").assertIsDisplayed()


        // Hide
        composeTestRule.onNodeWithContentDescription("Handle bar")
            .performTouchInput {
                down(center)
                moveBy(Offset(0f, y = 20000f), 0)
                advanceEventTime(1000)
                up()
            }
        composeTestRule.onNodeWithContentDescription("Settings sheet").assertDoesNotExist()
    }

    @Test
    fun bothContentAndSheetScrollable() {
        // assert that scroll on root changes the scroll state of each
        composeTestRule.onNodeWithContentDescription("Page 0").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Page 1").assertIsNotDisplayed()
        composeTestRule.onRoot().performTouchInput {
            swipeUp(startY = centerY, endY = top)
            advanceEventTime(1000)
        }
        composeTestRule.onNodeWithContentDescription("Page 0").assertIsNotDisplayed()
        composeTestRule.onNodeWithContentDescription("Page 1").assertIsDisplayed()

        val sheetCenter = composeTestRule.onNodeWithContentDescription("Settings sheet")
            .fetchSemanticsNode()
            .boundsInWindow
            .center

        composeTestRule.onNodeWithContentDescription("Reset to default settings button")
            .assertIsNotDisplayed()
        composeTestRule.onRoot().performTouchInput {
            down(sheetCenter)
            moveBy(Offset(0f, y = -20000f), 0)
            advanceEventTime(1000)
            up()
        }
        composeTestRule.onNodeWithContentDescription("Reset to default settings button")
            .assertIsDisplayed()
    }

    @Test
    fun hideSheetByTap() {
        composeTestRule.onNodeWithContentDescription("Page 0").performClick()
        composeTestRule.onNodeWithContentDescription("Settings sheet").assertIsNotDisplayed()
    }

    @Test
    fun incrementButtons() {
        // the number in text field (and in preferences storage) changes
        // and the content get recomposed with the new settings
//
//        // store the initial text field value and check it matches the one in content
//        var initialValue: Float
//        runBlocking {
//            MyApp.appModule.preferencesRepository.readFontSizeScale().first {
//                initialValue = it
//                return@first true
//            }
//        }
//
//        // assert text field value matches repository values
//        composeTestRule.onNodeWithText("font size scale").assertTextEquals(initialValue.toString())
//
//        composeTestRule.onNodeWithContentDescription("font size scale row")
//            .onChildren()
//            .filterToOne(hasText("-"))
//
//        var newValue: Float
//        runBlocking {
//            MyApp.appModule.preferencesRepository.readFontSizeScale().first {
//                newValue = it
//                return@first true
//            }
//        }
        // ?????

        // assert that the value in text field matches the one content is displayed with, and the content is recomposed after each update
        composeTestRule.onNodeWithContentDescription("Page 0")
            .assert(SemanticsMatcher.expectValue(FontSizeScaleKey, 1.5f))
            .assert(SemanticsMatcher.expectValue(LineSpacingKey, 4))
            .assert(SemanticsMatcher.expectValue(PagePaddingKey, 25f))
            .assert(SemanticsMatcher.expectValue(PageSpacingKey, 30f))
            .assert(SemanticsMatcher.expectValue(ParagraphSpacingKey, 10f))
    }

}