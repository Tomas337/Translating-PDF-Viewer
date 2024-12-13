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
import androidx.compose.ui.test.assertTextContains
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
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.Dp
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

    private fun assertPagerStateAndSettingsTextFieldsMatch(
        fontSizeScale: Float,
        lineSpacing: Int,
        pagePadding: Float,
        pageSpacing: Float,
        paragraphSpacing: Float,
    ) {
        composeTestRule.onNodeWithContentDescription("Page 0")
            .assert(SemanticsMatcher.expectValue(FontSizeScaleKey, fontSizeScale))
            .assert(SemanticsMatcher.expectValue(LineSpacingKey, lineSpacing))
            .assert(SemanticsMatcher.expectValue(PagePaddingKey, pagePadding))
            .assert(SemanticsMatcher.expectValue(PageSpacingKey, pageSpacing))
            .assert(SemanticsMatcher.expectValue(ParagraphSpacingKey, paragraphSpacing))
        composeTestRule.onNodeWithText("font size scale")
            .assertTextContains("%.1f".format(fontSizeScale))
        composeTestRule.onNodeWithText("line spacing")
            .assertTextContains("$lineSpacing")
        composeTestRule.onNodeWithText("page padding")
            .assertTextContains("%.0f".format(pagePadding))
        composeTestRule.onNodeWithText("page spacing")
            .assertTextContains("%.0f".format(pageSpacing))
        composeTestRule.onNodeWithText("paragraph spacing")
            .assertTextContains("%.0f".format(paragraphSpacing))
    }

    private enum class Adjustment {
        INCREMENT,
        DECREMENT
    }

    private fun adjustAllValues(adjustment: Adjustment, n: Int = 1) {
        val buttonText = if (adjustment == Adjustment.INCREMENT) "+" else "-"
        val rows = listOf(
            "font size scale row",
            "line spacing row",
            "page padding row",
            "page spacing row",
            "paragraph spacing row"
        )

        for (i in 0 until n) {
            for (row in rows) {
                composeTestRule.onNodeWithContentDescription(row)
                    .performScrollTo()
                    .onChildren()
                    .filterToOne(hasText(buttonText))
                    .performClick()
            }
        }
    }
    @Test
    fun incrementButtons() {
        assertPagerStateAndSettingsTextFieldsMatch(
            fontSizeScale = 1.5f,
            lineSpacing = 4,
            pagePadding = 25f,
            pageSpacing = 30f,
            paragraphSpacing = 10f,
        )
        adjustAllValues(Adjustment.INCREMENT)
        assertPagerStateAndSettingsTextFieldsMatch(
            fontSizeScale = 1.6f,
            lineSpacing = 5,
            pagePadding = 26f,
            pageSpacing = 31f,
            paragraphSpacing = 11f,
        )
        adjustAllValues(Adjustment.DECREMENT)
        assertPagerStateAndSettingsTextFieldsMatch(
            fontSizeScale = 1.5f,
            lineSpacing = 4,
            pagePadding = 25f,
            pageSpacing = 30f,
            paragraphSpacing = 10f,
        )
        // TODO FIX: if a value hits -1 pdf viewer screen won't be displayed
    }

}