package io.github.tomas337.translating_pdf_viewer.ui.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.tomas337.translating_pdf_viewer.ui.screens.home.HomeScreen
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.PdfViewerScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = NavRoute.Home.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavRoute.Home.route) {
            HomeScreen(navController)
        }
        composable(NavRoute.PdfViewer.route) { navBackStackEntry ->
            val bookId = navBackStackEntry.arguments?.getString("bookId")?.toInt()
            bookId?.let { bookId ->
                PdfViewerScreen(navController, bookId)
            }
        }
    }
}