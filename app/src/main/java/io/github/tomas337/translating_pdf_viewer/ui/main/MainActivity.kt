package io.github.tomas337.translating_pdf_viewer.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import io.github.tomas337.translating_pdf_viewer.R
import io.github.tomas337.translating_pdf_viewer.ui.main.viewmodel.MainActivityViewModel

class MainActivity : ComponentActivity() {
    private val viewmodel: MainActivityViewModel by viewModels()

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
                        viewmodel.setUri(uri)
                    }
                }
            }
        getPdfUri.launch(chooser)

        setContent {
//            TranslatingPDFViewerTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    // PdfViewer()
//                }
//            }
        }
    }
}