package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.github.tomas337.translating_pdf_viewer.data.utils.PdfExtractor

@Composable
fun PdfViewer(
    uri: Uri,
    modifier: Modifier = Modifier,
) {
    // TODO move this out of view layer
    if (!Uri.EMPTY.equals(uri)) {
        val context = LocalContext.current
        val extractor =
            PdfExtractor(
                context,
                uri
            )
        val doc = extractor.extractDocument();
        Log.d("doc", doc.toString())
        Log.d("pages", doc.pages.toString())
        Log.d("font map", doc.intToTextStyleMap.toString())
        Log.d("name", doc.name)
    }
}