package io.github.tomas337.translating_pdf_viewer.ui.main.navigation

sealed class NavRoute(val route: String) {
    object Home : NavRoute("home")
    object PdfViewer : NavRoute("pdfviewer/{bookId}") {
        fun createRoute(bookId: Int) = "pdfviewer/$bookId"
    }
}