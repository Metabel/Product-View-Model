package com.Met.myapplication.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.Met.myapplication.ui.theme.screens.home.HomeScreen
import com.Met.myapplication.ui.theme.screens.login.LoginScreen
import com.Met.myapplication.ui.theme.screens.product.ProductScreen
import com.Met.myapplication.ui.theme.screens.product.ListProductsScreen
import com.Met.myapplication.ui.theme.screens.product.UpdateProductScreen
import com.Met.myapplication.ui.theme.screens.register.RegisterScreen
import com.Met.myapplication.ui.theme.screens.splash.SplashScreen


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_HOME
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ROUTE_HOME) { HomeScreen(navController) }
        composable(ROUTE_LOGIN) { LoginScreen(navController) }
        composable(ROUTE_REGISTER) { RegisterScreen(navController) }
        composable(ROUTE_ADDPRODUCT) { ProductScreen(navController) }
        composable(ROUTE_LISTPRODUCT) { ListProductsScreen(navController) }
        composable(ROUTE_SPLASH) { SplashScreen(navController) }
       composable (ROUTE_UPDATEPRODUCT+"/{productId}",){
           backStackEntry->
           val productId=backStackEntry.arguments?.getInt("productId")?:0
           UpdateProductScreen(navController, productId)
       }
        }
    }

