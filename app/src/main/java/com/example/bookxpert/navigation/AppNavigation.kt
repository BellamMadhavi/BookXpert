package com.example.bookxpert.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookxpert.viewmodel.ObjectViewModel
import com.example.bookxpert.auth.AuthViewModel
import com.example.bookxpert.ui.screens.HomeScreen
import com.example.bookxpert.ui.screens.ObjectListScreen

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    objectViewModel: ObjectViewModel,
    onSignOut: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = authViewModel,
                onSignOut = onSignOut,
                onNavigateToObjectList = {
                    navController.navigate("objectList")
                }
            )
        }
        composable("objectList") {
            ObjectListScreen(viewModel = objectViewModel)
        }
    }
}
