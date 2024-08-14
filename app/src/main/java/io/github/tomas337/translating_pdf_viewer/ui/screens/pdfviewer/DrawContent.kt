package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
    intToTextStyleMap: Map<Int, TextStyle>
) {
    Log.d("content", content.toString())

    // TODO: fix "Error, cannot access an invalid/free'd bitmap here!"
    // TODO: fix " java.lang.RuntimeException: Canvas: trying to draw too large(1257637270bytes) bitmap."
    if (content is Image) {
//        Image(
//            bitmap = content.image.asImageBitmap(),
//            contentDescription = "Image on page ${pageIndex + 1}"
//        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(content.image)
                .build(),
            contentDescription = "Image on page ${pageIndex + 1}",
        )
    } else if (content is TextBlock) {

//        for (text in content.texts) {
//            Log.d("text", text)
//        }

        Text(
            buildAnnotatedString {
                content.texts.forEachIndexed { i, text ->
                    val styleIndex = content.styles[i]
                    val style = intToTextStyleMap[styleIndex]

                    withStyle(
                        style = SpanStyle(
                            fontSize = style!!.fontSize.sp,
                            fontWeight = FontWeight(style.fontWeight),
                            fontStyle = if (style.isItalic) FontStyle.Italic else FontStyle.Normal,
                        )
                    ) {
                        append(text)
                    }
                }
            },
            modifier = Modifier.rotate(content.rotation),
        )
    }
}