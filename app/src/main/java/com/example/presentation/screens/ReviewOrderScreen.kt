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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ReceiptLong
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
import com.example.domain.model.formatRupiah
import com.example.presentation.viewmodel.KioskViewModel
import com.example.ui.theme.PoetraAmber
import com.example.ui.theme.PoetraBackground
import com.example.ui.theme.PoetraRedPrimary
import com.example.ui.theme.PoetraStatusPending
import com.example.ui.theme.PoetraStatusPendingBg
import com.example.ui.theme.PoetraTextDark
import com.example.ui.theme.PoetraTextSecondary

@Composable
fun ReviewOrderScreen(
    viewModel: KioskViewModel,
    onNavigateBack: () -> Unit,
    onProceedToPayment: () -> Unit
) {
    BackHandler { onNavigateBack() }

    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val selectedTable by viewModel.selectedTable.collectAsStateWithLifecycle()

    val subtotal = cartItems.sumOf { it.totalPrice }
    val tax = subtotal * 0.10
    val total = subtotal + tax

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                shadowElevation = 1.dp
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
                            modifier = Modifier.testTag("review_order_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = PoetraRedPrimary
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "ORDER SUMMARY",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = PoetraTextDark
                        )
                    }

                    // Table Badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(PoetraRedPrimary.copy(alpha = 0.1f))
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = PoetraRedPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = selectedTable.label.uppercase(),
                            fontWeight = FontWeight.Bold,
                            color = PoetraRedPrimary,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        },
        containerColor = PoetraBackground
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Left: Items List
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Selected Items (${cartItems.sumOf { it.quantity }})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PoetraTextDark
                        )
                        OutlinedButton(
                            onClick = onNavigateBack,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("modify_order_button")
                        ) {
                            Text(
                                text = "Modify Order",
                                color = PoetraRedPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(cartItems, key = { it.id }) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(PoetraRedPrimary.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${item.quantity}x",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 13.sp,
                                            color = PoetraRedPrimary
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Column {
                                        Text(
                                            text = item.productName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = PoetraTextDark
                                        )
                                        if (item.customizations.isNotBlank()) {
                                            Text(
                                                text = item.customizations,
                                                fontSize = 12.sp,
                                                color = PoetraTextSecondary
                                            )
                                        }
                                        if (item.itemNotes.isNotBlank()) {
                                            Text(
                                                text = "Note: ${item.itemNotes}",
                                                fontSize = 11.sp,
                                                color = PoetraAmber
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = item.formattedTotalPrice,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = PoetraRedPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Right: Breakdown & Confirmation
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Payment & Pricing",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PoetraTextDark
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Notice Card: Payment will be made at the cashier
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = PoetraStatusPendingBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, PoetraStatusPending.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = PoetraStatusPending,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Payment Notice",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = PoetraStatusPending
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Payment will be made at the cashier. You will receive an Order Number to show to the cashier.",
                                        fontSize = 12.sp,
                                        color = PoetraTextDark,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Cost breakdown
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Subtotal", color = PoetraTextSecondary, fontSize = 14.sp)
                            Text(text = formatRupiah(subtotal), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Tax (PB1 10%)", color = PoetraTextSecondary, fontSize = 14.sp)
                            Text(text = formatRupiah(tax), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }

                        Divider(modifier = Modifier.padding(vertical = 14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TOTAL",
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = PoetraTextDark
                            )
                            Text(
                                text = formatRupiah(total),
                                fontWeight = FontWeight.Black,
                                fontSize = 24.sp,
                                color = PoetraRedPrimary
                            )
                        }
                    }

                    // Action Button: Confirm Order
                    Button(
                        onClick = onProceedToPayment,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PoetraRedPrimary,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("confirm_order_button")
                    ) {
                        Text(
                            text = "CONFIRM ORDER",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}
