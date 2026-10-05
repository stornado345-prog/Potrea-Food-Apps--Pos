package com.example.presentation.navigation

sealed class KioskScreen(val route: String) {
    object Menu : KioskScreen("menu")
    object ReviewOrder : KioskScreen("review_order")
    object Payment : KioskScreen("payment")
    object OrderConfirmation : KioskScreen("order_confirmation/{orderId}") {
        fun createRoute(orderId: Long) = "order_confirmation/$orderId"
    }
    object OrderStatus : KioskScreen("order_status")
}
