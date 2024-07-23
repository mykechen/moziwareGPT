package com.example.moziwaregpt

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class Screens(){
    HOME,
    LOGIN
}

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.HOME.name) {
        composable(route = Screens.LOGIN.name){
            LoginScreen(navController = navController)
        }
        composable(route = Screens.HOME.name) {
            HomeScreen(navController = navController)
        }
    }
}