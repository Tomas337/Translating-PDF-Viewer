package io.github.tomas337.translating_pdf_viewer.ui.main

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun PdfViewer(
    uri: Uri,
    modifier: Modifier = Modifier,
) {
    if (!Uri.EMPTY.equals(uri)) {
        val context = LocalContext.current
        val extractor = PdfExtractor(context, uri)
//        val pages = extractor.extractText()
        extractor.extractText()
    }
}