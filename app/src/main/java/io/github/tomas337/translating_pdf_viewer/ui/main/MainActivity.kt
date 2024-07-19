package io.github.tomas337.translating_pdf_viewer.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import io.github.tomas337.translating_pdf_viewer.R
import io.github.tomas337.translating_pdf_viewer.ui.main.viewmodel.MainActivityViewModel
import io.github.tomas337.translating_pdf_viewer.ui.pdfviewer.PdfViewer
import io.github.tomas337.translating_pdf_viewer.ui.theme.TranslatingPDFViewerTheme

class MainActivity : ComponentActivity() {
    private val mainViewmodel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val intent = Intent()
            .setType("application/pdf")
            .setAction(Intent.ACTION_GET_CONTENT)

        val title: String = resources.getString(R.string.chooser_title)
        val chooser: Intent = Intent.createChooser(intent, title)

        val getPdfUri =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.data?.let { uri ->
                        mainViewmodel.setUri(uri)
                    }
                }
            }
        getPdfUri.launch(chooser)

        PDFBoxResourceLoader.init(applicationContext)

        setContent {
            TranslatingPDFViewerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PdfViewer(
                        uri = mainViewmodel.uri.collectAsState().value,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}