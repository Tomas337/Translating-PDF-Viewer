package io.github.tomas337.translating_pdf_viewer.ui.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.tomas337.translating_pdf_viewer.ui.main.MainScreen
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.PdfViewerScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = Screen.Main.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Main.route) {
            MainScreen(navController)
        }
        composable(Screen.PdfViewer.route) { navBackStackEntry ->
            val bookId = navBackStackEntry.arguments?.getString("bookId")?.toInt()
            bookId?.let { bookId ->
                PdfViewerScreen(navController, bookId)
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object PdfViewer : Screen("pdfviewer/{bookId}") {
        fun createRoute(bookId: Int) = "pdfviewer/$bookId"
    }
}