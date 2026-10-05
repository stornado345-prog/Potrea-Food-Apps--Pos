package com.example.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.OrderStatus
import com.example.domain.model.formatRupiah
import com.example.presentation.components.OrderStatusBadge
import com.example.presentation.viewmodel.KioskViewModel
import com.example.ui.theme.PoetraAmber
import com.example.ui.theme.PoetraAmberDark
import com.example.ui.theme.PoetraBackground
import com.example.ui.theme.PoetraRedPrimary
import com.example.ui.theme.PoetraStatusPendingBg
import com.example.ui.theme.PoetraTextDark
import com.example.ui.theme.PoetraTextSecondary

@Composable
fun OrderConfirmationScreen(
    orderId: Long,
    viewModel: KioskViewModel,
    onNavigateToMenu: () -> Unit,
    onNavigateToStatusList: () -> Unit
) {
    BackHandler { onNavigateToMenu() }

    val orderFlow = viewModel.getOrderFlow(orderId)
    val order by orderFlow.collectAsStateWithLifecycle(initialValue = null)

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = PoetraBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (order == null) {
                Text("Loading order details...", fontSize = 16.sp)
            } else {
                val currentOrder = order!!
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Left Column: Order Ticket & Large Number
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier
                            .weight(0.55f)
                            .fillMaxHeight()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Green check badge
                                Box(
                                    modifier = Modifier
                                        .size(68.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE8F5E9)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color(0xFF2E7D32),
                                        modifier = Modifier.size(38.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = "ORDER RECEIVED",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                    color = PoetraTextDark
                                )

                                Text(
                                    text = "Your order has been recorded in the system.",
                                    fontSize = 14.sp,
                                    color = PoetraTextSecondary
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                // Big Order Number Box
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = PoetraRedPrimary.copy(alpha = 0.08f),
                                    border = androidx.compose.foundation.BorderStroke(2.dp, PoetraRedPrimary.copy(alpha = 0.4f)),
                                    modifier = Modifier
                                        .fillMaxWidth(0.85f)
                                        .testTag("order_number_box")
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 18.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "YOUR ORDER NUMBER",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = PoetraRedPrimary,
                                            letterSpacing = 1.5.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = currentOrder.orderNumber,
                                            fontSize = 44.sp,
                                            fontWeight = FontWeight.Black,
                                            color = PoetraRedPrimary,
                                            letterSpacing = 2.sp
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        OrderStatusBadge(status = currentOrder.status)
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                // Instruction message
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = PoetraStatusPendingBg,
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Please pay at the cashier.",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = PoetraTextDark
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Show this order number (${currentOrder.orderNumber}) at the register. Once paid, the kitchen will prepare your food.",
                                            fontSize = 12.sp,
                                            color = PoetraTextSecondary,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }

                            // Simulation Toolbar (Allows changing status to test whole lifecycle!)
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Demo Cashier / Kitchen Control",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = PoetraTextDark
                                        )
                                        Text(
                                            text = "Advance order lifecycle",
                                            fontSize = 11.sp,
                                            color = PoetraTextSecondary
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            viewModel.simulateStatusAdvance(currentOrder.id, currentOrder.status)
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = PoetraAmberDark,
                                            contentColor = Color.White
                                        ),
                                        modifier = Modifier.testTag("simulate_advance_status_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = when (currentOrder.status) {
                                                OrderStatus.PENDING_PAYMENT -> "Mark Paid"
                                                OrderStatus.PAID -> "Start Cooking"
                                                OrderStatus.PREPARING -> "Order Ready"
                                                OrderStatus.READY -> "Complete"
                                                OrderStatus.COMPLETED -> "Finished"
                                                OrderStatus.CANCELLED -> "Restart"
                                            },
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Right Column: Order Details & Action Buttons
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier
                            .weight(0.45f)
                            .fillMaxHeight()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "ORDER DETAILS",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = PoetraTextDark
                                    )
                                    Text(
                                        text = currentOrder.tableNumber,
                                        fontWeight = FontWeight.Bold,
                                        color = PoetraRedPrimary,
                                        fontSize = 14.sp
                                    )
                                }

                                Divider(modifier = Modifier.padding(vertical = 12.dp))

                                LazyColumn(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(currentOrder.items, key = { it.id }) { item ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    text = "${item.quantity}x",
                                                    fontWeight = FontWeight.Bold,
                                                    color = PoetraRedPrimary,
                                                    fontSize = 13.sp
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Column {
                                                    Text(
                                                        text = item.productName,
                                                        fontWeight = FontWeight.Medium,
                                                        color = PoetraTextDark,
                                                        fontSize = 13.sp
                                                    )
                                                    if (item.customizations.isNotBlank()) {
                                                        Text(
                                                            text = item.customizations,
                                                            fontSize = 11.sp,
                                                            color = PoetraTextSecondary
                                                        )
                                                    }
                                                }
                                            }

                                            Text(
                                                text = item.formattedTotalPrice,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 13.sp,
                                                color = PoetraTextDark
                                            )
                                        }
                                    }
                                }

                                Divider(modifier = Modifier.padding(vertical = 10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Subtotal", color = PoetraTextSecondary, fontSize = 13.sp)
                                    Text(currentOrder.formattedSubtotal, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Tax (PB1 10%)", color = PoetraTextSecondary, fontSize = 13.sp)
                                    Text(currentOrder.formattedTax, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("TOTAL", fontWeight = FontWeight.Black, fontSize = 16.sp)
                                    Text(currentOrder.formattedTotal, fontWeight = FontWeight.Black, fontSize = 20.sp, color = PoetraRedPrimary)
                                }
                            }

                            // Action buttons
                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Button(
                                    onClick = onNavigateToStatusList,
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PoetraAmber,
                                        contentColor = Color.Black
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .testTag("track_order_status_button")
                                    ) {
                                    Icon(
                                        imageVector = Icons.Default.ListAlt,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "TRACK ALL ORDERS",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }

                                Button(
                                    onClick = onNavigateToMenu,
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PoetraRedPrimary,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .testTag("new_order_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Fastfood,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "START NEW ORDER",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
