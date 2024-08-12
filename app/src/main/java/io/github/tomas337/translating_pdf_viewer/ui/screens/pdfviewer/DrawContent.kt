package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import io.github.tomas337.translating_pdf_viewer.utils.Image
import io.github.tomas337.translating_pdf_viewer.utils.PageContent
import io.github.tomas337.translating_pdf_viewer.utils.TextBlock
import io.github.tomas337.translating_pdf_viewer.utils.TextStyle
import kotlin.math.max

@Composable
fun DrawContent(
    content: PageContent,
    pageIndex: Int,
    intToTextStyleMap: Map<Int, TextStyle>
) {
    Log.d("content", content.toString())

    if (content is Image) {
        Image(
            bitmap = content.image.asImageBitmap(),
            contentDescription = "Image on page ${pageIndex + 1}"
        )
    } else if (content is TextBlock) {
        Log.d("texts size", content.texts.size.toString())
        Log.d("styles size", content.styles.size.toString())

        Text(
            buildAnnotatedString {
                content.texts.forEachIndexed { i, text ->
                    val styleIndex = content.styles[i]
                    val style = intToTextStyleMap[styleIndex]

                    withStyle(
                        style = SpanStyle(
                            fontSize = style!!.fontSize.sp,
                            fontWeight = FontWeight(max(style.fontWeight, 1)),
                            fontStyle = if (style.isItalic) FontStyle.Italic else FontStyle.Normal
                        )
                    ) {
                        append(text)
                    }
                }
            }
        )
    }
}