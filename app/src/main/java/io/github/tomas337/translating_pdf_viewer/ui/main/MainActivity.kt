package io.github.tomas337.translating_pdf_viewer.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import io.github.tomas337.translating_pdf_viewer.ui.main.navigation.NavGraph
import io.github.tomas337.translating_pdf_viewer.ui.theme.TranslatingPDFViewerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        PDFBoxResourceLoader.init(applicationContext)

        setContent {
            TranslatingPDFViewerTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
                    NavGraph(
                        navController = rememberNavController(),
                    )
                }
            }
        }
    }
}