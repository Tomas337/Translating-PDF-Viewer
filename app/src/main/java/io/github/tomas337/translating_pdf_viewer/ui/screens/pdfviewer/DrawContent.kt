package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.tomas337.translating_pdf_viewer.utils.Image
import io.github.tomas337.translating_pdf_viewer.utils.PageContent
import io.github.tomas337.translating_pdf_viewer.utils.TextBlock
import io.github.tomas337.translating_pdf_viewer.utils.TextStyle

@Composable
fun DrawContent(
    content: PageContent,
    pageIndex: Int,
    intToTextStyleMap: Map<Int, TextStyle>,
) {
    if (content is Image) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(content.path)
                .size(content.width, content.height)
                .build(),
            contentDescription = "Image on page ${pageIndex + 1}",
        )
    } else if (content is TextBlock) {
        val fontSizeScale = 1.5
        val lineSpacing = 4

        val lineHeight =
            (intToTextStyleMap[content.styles[0]]!!.fontSize * fontSizeScale + lineSpacing).sp

        Text(
            buildAnnotatedString {
                content.texts.forEachIndexed { i, text ->
                    val styleIndex = content.styles[i]
                    val style = intToTextStyleMap[styleIndex]

                    withStyle(
                        style = SpanStyle(
                            fontSize = style!!.fontSize.sp * fontSizeScale,
                            fontWeight = FontWeight(style.fontWeight),
                            fontStyle = if (style.isItalic) FontStyle.Italic else FontStyle.Normal,
                        )
                    ) {
                        append(text)
                    }
                }
            },
            lineHeight = lineHeight,
            modifier = Modifier.rotate(content.rotation),
            style = androidx.compose.ui.text.TextStyle.Default.copy(
                // TODO don't split headings
                hyphens = Hyphens.Auto,
                lineBreak = LineBreak.Paragraph.copy(
                    strategy = LineBreak.Strategy.HighQuality,
                )
            )
        )
    }
}