package com.example.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.Order
import com.example.domain.model.OrderStatus
import com.example.presentation.components.OrderStatusBadge
import com.example.presentation.viewmodel.KioskViewModel
import com.example.ui.theme.PoetraAmber
import com.example.ui.theme.PoetraAmberDark
import com.example.ui.theme.PoetraBackground
import com.example.ui.theme.PoetraRedPrimary
import com.example.ui.theme.PoetraTextDark
import com.example.ui.theme.PoetraTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrderStatusScreen(
    viewModel: KioskViewModel,
    onNavigateBack: () -> Unit
) {
    BackHandler { onNavigateBack() }

    val orders by viewModel.orders.collectAsStateWithLifecycle()

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier.testTag("order_status_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = PoetraRedPrimary
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "ORDER STATUS & HISTORY",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = PoetraTextDark
                        )
                    }

                    Button(
                        onClick = onNavigateBack,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PoetraRedPrimary),
                        modifier = Modifier.testTag("back_to_menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fastfood,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Back to Menu", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        },
        containerColor = PoetraBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            if (orders.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(PoetraAmber.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                tint = PoetraRedPrimary,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No orders placed yet",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = PoetraTextDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Your placed orders will appear here for status tracking.",
                            fontSize = 14.sp,
                            color = PoetraTextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("orders_list")
                ) {
                    items(orders, key = { it.id }) { order ->
                        OrderItemCard(
                            order = order,
                            onAdvanceStatus = {
                                viewModel.simulateStatusAdvance(order.id, order.status)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderItemCard(
    order: Order,
    onAdvanceStatus: () -> Unit
) {
    val dateFormat = SimpleDateFormat("HH:mm, dd MMM yyyy", Locale.getDefault())
    val formattedTime = dateFormat.format(Date(order.createdAt))

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("order_card_${order.id}")
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = order.orderNumber,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = PoetraRedPrimary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = "• ${order.tableNumber}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = PoetraTextDark
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = formattedTime,
                        fontSize = 13.sp,
                        color = PoetraTextSecondary
                    )
                }

                OrderStatusBadge(status = order.status)
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // Order Status Pipeline / Steps visual indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .padding(vertical = 10.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusStep(
                    stepName = "1. Pending Payment",
                    isActive = order.status == OrderStatus.PENDING_PAYMENT,
                    isDone = order.status != OrderStatus.PENDING_PAYMENT && order.status != OrderStatus.CANCELLED
                )
                Text("→", color = Color.Gray, fontWeight = FontWeight.Bold)
                StatusStep(
                    stepName = "2. Paid",
                    isActive = order.status == OrderStatus.PAID,
                    isDone = order.status == OrderStatus.PREPARING || order.status == OrderStatus.READY || order.status == OrderStatus.COMPLETED
                )
                Text("→", color = Color.Gray, fontWeight = FontWeight.Bold)
                StatusStep(
                    stepName = "3. Preparing",
                    isActive = order.status == OrderStatus.PREPARING,
                    isDone = order.status == OrderStatus.READY || order.status == OrderStatus.COMPLETED
                )
                Text("→", color = Color.Gray, fontWeight = FontWeight.Bold)
                StatusStep(
                    stepName = "4. Ready",
                    isActive = order.status == OrderStatus.READY,
                    isDone = order.status == OrderStatus.COMPLETED
                )
                Text("→", color = Color.Gray, fontWeight = FontWeight.Bold)
                StatusStep(
                    stepName = "5. Completed",
                    isActive = order.status == OrderStatus.COMPLETED,
                    isDone = order.status == OrderStatus.COMPLETED
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Total & Cashier Simulation Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Total: ",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = PoetraTextSecondary
                    )
                    Text(
                        text = order.formattedTotal,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = PoetraRedPrimary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "(${order.paymentStatus.label})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (order.paymentStatus.name == "PAID") Color(0xFF2E7D32) else PoetraAmberDark
                    )
                }

                if (order.status != OrderStatus.COMPLETED) {
                    OutlinedButton(
                        onClick = onAdvanceStatus,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("advance_status_button_${order.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = PoetraRedPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when (order.status) {
                                OrderStatus.PENDING_PAYMENT -> "Simulate: Cashier Confirm Paid"
                                OrderStatus.PAID -> "Simulate: Kitchen Start Cooking"
                                OrderStatus.PREPARING -> "Simulate: Mark Ready for Pickup"
                                OrderStatus.READY -> "Simulate: Mark Completed"
                                else -> "Simulate Next"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PoetraRedPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusStep(
    stepName: String,
    isActive: Boolean,
    isDone: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isActive -> PoetraAmberDark
                        isDone -> Color(0xFF2E7D32)
                        else -> Color.LightGray
                    }
                )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = stepName,
            fontSize = 11.sp,
            fontWeight = if (isActive || isDone) FontWeight.Bold else FontWeight.Normal,
            color = when {
                isActive -> PoetraAmberDark
                isDone -> Color(0xFF2E7D32)
                else -> Color.Gray
            }
        )
    }
}
