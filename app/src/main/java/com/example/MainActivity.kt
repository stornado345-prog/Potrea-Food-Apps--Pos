package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.presentation.navigation.KioskScreen
import com.example.presentation.screens.KioskMainScreen
import com.example.presentation.screens.OrderConfirmationScreen
import com.example.presentation.screens.OrderStatusScreen
import com.example.presentation.screens.PaymentScreen
import com.example.presentation.screens.ReviewOrderScreen
import com.example.presentation.viewmodel.KioskViewModel
import com.example.ui.theme.PoetraBackground
import com.example.ui.theme.PoetraFoodTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PoetraFoodTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = PoetraBackground
                ) {
                    PoetraKioskApp()
                }
            }
        }
    }
}

@Composable
fun PoetraKioskApp(
    viewModel: KioskViewModel = viewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = KioskScreen.Menu.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(KioskScreen.Menu.route) {
            KioskMainScreen(
                viewModel = viewModel,
                onNavigateToReviewOrder = {
                    navController.navigate(KioskScreen.ReviewOrder.route)
                },
                onNavigateToOrderStatus = {
                    navController.navigate(KioskScreen.OrderStatus.route)
                }
            )
        }

        composable(KioskScreen.ReviewOrder.route) {
            ReviewOrderScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onProceedToPayment = {
                    navController.navigate(KioskScreen.Payment.route)
                }
            )
        }

        composable(KioskScreen.Payment.route) {
            PaymentScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onOrderPlaced = { orderId ->
                    navController.navigate(KioskScreen.OrderConfirmation.createRoute(orderId)) {
                        popUpTo(KioskScreen.Menu.route) { inclusive = false }
                    }
                }
            )
        }

        composable(
            route = KioskScreen.OrderConfirmation.route,
            arguments = listOf(
                navArgument("orderId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getLong("orderId") ?: 0L
            OrderConfirmationScreen(
                orderId = orderId,
                viewModel = viewModel,
                onNavigateToMenu = {
                    navController.navigate(KioskScreen.Menu.route) {
                        popUpTo(KioskScreen.Menu.route) { inclusive = true }
                    }
                },
                onNavigateToStatusList = {
                    navController.navigate(KioskScreen.OrderStatus.route)
                }
            )
        }

        composable(KioskScreen.OrderStatus.route) {
            OrderStatusScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
