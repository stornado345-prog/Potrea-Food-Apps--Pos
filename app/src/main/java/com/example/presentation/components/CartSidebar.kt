package com.example.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.CartItem
import com.example.domain.model.formatRupiah
import com.example.ui.theme.PoetraAmber
import com.example.ui.theme.PoetraAmberDark
import com.example.ui.theme.PoetraFoodTheme
import com.example.ui.theme.PoetraRedPrimary
import com.example.ui.theme.PoetraTextDark
import com.example.ui.theme.PoetraTextSecondary

@Composable
fun CartSidebar(
    cartItems: List<CartItem>,
    onIncreaseQuantity: (CartItem) -> Unit,
    onDecreaseQuantity: (CartItem) -> Unit,
    onRemoveItem: (CartItem) -> Unit,
    onClearCart: () -> Unit,
    onReviewOrder: () -> Unit,
    modifier: Modifier = Modifier
) {
    val subtotal = cartItems.sumOf { it.totalPrice }
    val tax = subtotal * 0.10 // 10% Restaurant Tax
    val grandTotal = subtotal + tax

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        modifier = modifier
            .width(320.dp)
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = PoetraRedPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "POETRA FOOD ORDER",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = PoetraTextDark,
                        letterSpacing = 0.5.sp
                    )
                }

                if (cartItems.isNotEmpty()) {
                    Text(
                        text = "Clear",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PoetraTextSecondary,
                        modifier = Modifier
                            .clickable { onClearCart() }
                            .padding(4.dp)
                            .testTag("clear_cart_text_button")
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // Cart Items or Empty State
            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(PoetraAmber.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.RemoveShoppingCart,
                                contentDescription = null,
                                tint = PoetraAmberDark,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Your order is empty",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = PoetraTextDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Add your favorite food & drinks to get started.",
                            fontSize = 13.sp,
                            color = PoetraTextSecondary,
                            lineHeight = 18.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(cartItems, key = { it.id }) { item ->
                        CartItemRow(
                            item = item,
                            onIncrease = { onIncreaseQuantity(item) },
                            onDecrease = { onDecreaseQuantity(item) },
                            onRemove = { onRemoveItem(item) }
                        )
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // Cost Summary
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Subtotal",
                        color = PoetraTextSecondary,
                        fontSize = 13.sp
                    )
                    Text(
                        text = formatRupiah(subtotal),
                        fontWeight = FontWeight.SemiBold,
                        color = PoetraTextDark,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tax (PB1 10%)",
                        color = PoetraTextSecondary,
                        fontSize = 13.sp
                    )
                    Text(
                        text = formatRupiah(tax),
                        fontWeight = FontWeight.SemiBold,
                        color = PoetraTextDark,
                        fontSize = 13.sp
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TOTAL",
                        fontWeight = FontWeight.Black,
                        color = PoetraTextDark,
                        fontSize = 16.sp
                    )
                    Text(
                        text = formatRupiah(grandTotal),
                        fontWeight = FontWeight.Black,
                        color = PoetraRedPrimary,
                        fontSize = 19.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Primary Review Order Button
            Button(
                onClick = onReviewOrder,
                enabled = cartItems.isNotEmpty(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PoetraRedPrimary,
                    contentColor = Color.White,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.DarkGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("review_order_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "REVIEW ORDER",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("cart_item_${item.id}")
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.productName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = PoetraTextDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (item.customizations.isNotBlank()) {
                        Text(
                            text = item.customizations,
                            fontSize = 11.sp,
                            color = PoetraTextSecondary,
                            lineHeight = 14.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (item.itemNotes.isNotBlank()) {
                        Text(
                            text = "Note: ${item.itemNotes}",
                            fontSize = 11.sp,
                            color = PoetraAmberDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier
                        .size(28.dp)
                        .testTag("remove_cart_item_${item.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Remove",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.formattedTotalPrice,
                    fontWeight = FontWeight.Black,
                    color = PoetraRedPrimary,
                    fontSize = 14.sp
                )

                // Quantity controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    IconButton(
                        onClick = onDecrease,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("decrease_cart_item_${item.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease",
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Text(
                        text = "${item.quantity}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    IconButton(
                        onClick = onIncrease,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("increase_cart_item_${item.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase",
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartSidebarPreview() {
    PoetraFoodTheme {
        CartSidebar(
            cartItems = listOf(
                CartItem(
                    id = 1,
                    productId = 101,
                    productName = "Nasi Goreng Spesial",
                    productPrice = 25000.0,
                    productImage = "",
                    quantity = 2,
                    customizations = "Pedas, Telur Dadar"
                ),
                CartItem(
                    id = 2,
                    productId = 102,
                    productName = "Es Teh Manis",
                    productPrice = 5000.0,
                    productImage = "",
                    quantity = 1
                )
            ),
            onIncreaseQuantity = {},
            onDecreaseQuantity = {},
            onRemoveItem = {},
            onClearCart = {},
            onReviewOrder = {}
        )
    }
}