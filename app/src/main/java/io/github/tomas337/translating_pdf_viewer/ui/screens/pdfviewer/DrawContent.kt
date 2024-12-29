package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import android.util.Log
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.tomas337.translating_pdf_viewer.domain.usecase.search.utils.Highlight
import io.github.tomas337.translating_pdf_viewer.utils.Image
import io.github.tomas337.translating_pdf_viewer.utils.PageContent
import io.github.tomas337.translating_pdf_viewer.utils.TextBlock
import io.github.tomas337.translating_pdf_viewer.utils.TextStyle
import java.util.LinkedList
import java.util.Queue
import kotlin.math.abs

@Composable
fun DrawContent(
    content: PageContent,
    pageIndex: Int,
    intToTextStyleMap: Map<Int, TextStyle>,
    fontSizeScale: Float,
    lineSpacing: Int,
    highlights: List<Highlight>,
    isHighlightSelected: (Highlight) -> Boolean,
    modifier: Modifier = Modifier,
) {
    if (content is Image) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(content.path)
                .size(abs(content.width), abs(content.height))  // TODO fix: abs only used to prevent mirrored images from throwing exception
                .build(),
            contentDescription = "Image on page ${pageIndex + 1}",
            modifier = modifier,
        )
    } else if (content is TextBlock) {
        var lineHeight =
            (intToTextStyleMap[content.styles[0]]!!.fontSize * fontSizeScale + lineSpacing).sp
        LaunchedEffect(fontSizeScale, lineSpacing) {
            lineHeight =
                (intToTextStyleMap[content.styles[0]]!!.fontSize * fontSizeScale + lineSpacing).sp
        }

        val textAlign = when (content.textAlign) {
            "center" -> TextAlign.Center
            "left" -> TextAlign.Left
            "right" -> TextAlign.Right
            else -> TextAlign.Justify
        }

        val textIndent =
            if (content.listPrefix != null) {
                val textStyle = LocalTextStyle.current
                val textMeasurer = rememberTextMeasurer()
                val bulletStringWidth = remember(textStyle, textMeasurer) {
                    textMeasurer.measure(text = content.listPrefix, style = textStyle).size.width
                }
                val bulletOffset = with(LocalDensity.current) { bulletStringWidth.toSp() }
                TextIndent(restLine = bulletOffset)
            } else {
                TextIndent()
            }

        Text(
            buildAnnotatedString {
                if (content.listPrefix != null) {
                    append(content.listPrefix)
                }

                val highlightsQueue: Queue<Highlight> = LinkedList(highlights)
                var startCharIndex = 0

                content.texts.forEachIndexed { i, text ->
                    val styleIndex = content.styles[i]
                    val textStyle = intToTextStyleMap[styleIndex]
                    val style = SpanStyle(
                        fontSize = textStyle!!.fontSize.sp * fontSizeScale,
                        fontWeight = FontWeight(textStyle.fontWeight),
                        fontStyle = if (textStyle.isItalic) FontStyle.Italic else FontStyle.Normal,
                        color =
                            if (textStyle.color.size == 3) {
                                Color(textStyle.color[0], textStyle.color[1], textStyle.color[2])
                            } else {
                                Color(textStyle.color[0], textStyle.color[0], textStyle.color[0])
                            },
                    )

                    // TODO fix: selection currently doesn't work
                    if (highlightsQueue.isNotEmpty() &&
                        highlightsQueue.first().start >= startCharIndex &&
                        highlightsQueue.first().end < startCharIndex + text.length
                    ) {
                        val curHighlight = highlightsQueue.first()

                        val backgroundColor = if (isHighlightSelected(curHighlight)) Color.Magenta else Color.Yellow
                        Log.d("isSelected", isHighlightSelected(curHighlight).toString())

                        text.forEachIndexed { index, char ->
                            val highlightStyle = style.copy(
                                // TODO: change colors
//                                background = if (isHighlightSelected(curHighlight)) Color.Magenta else Color.Yellow
                                background = backgroundColor
                            )

                            // TODO: test that the highlight is correctly displayed if the TextBlock contains more than 1 text
                            withStyle(
                                if (curHighlight.start <= index + startCharIndex &&
                                    curHighlight.end >= index + startCharIndex
                                ) {
                                    highlightStyle
                                } else {
                                    style
                                }
                            ) {
                                append(char)
                            }
                        }
                        highlightsQueue.remove()
                    } else {
                        withStyle(style) {
                            append(text)
                        }
                    }

                    startCharIndex += text.length
                }
            },
            textAlign = textAlign,
            lineHeight = lineHeight,
            style = androidx.compose.ui.text.TextStyle.Default.copy(
                textIndent = textIndent,
                lineBreak = LineBreak.Paragraph.copy(
                    strategy = LineBreak.Strategy.HighQuality,
                )
            ),
            modifier = modifier.rotate(content.rotation)
        )
    }
}