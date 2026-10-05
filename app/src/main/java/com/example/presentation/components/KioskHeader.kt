package com.example.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poetrafood.R
import com.example.domain.model.RestaurantTable
import com.example.ui.theme.PoetraAmber
import com.example.ui.theme.PoetraFoodTheme
import com.example.ui.theme.PoetraRedPrimary

@Composable
fun KioskHeader(
    selectedTable: RestaurantTable,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onTableClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onCartClick: () -> Unit,
    cartItemCount: Int,
    ordersCount: Int,
    showCartButton: Boolean = false,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Station ID
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(PoetraRedPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.poetra_food_logo),
                        contentDescription = "Poetra Food Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(46.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "POETRA FOOD",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = PoetraRedPrimary,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = "SELF-ORDER KIOSK",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PoetraAmber,
                        letterSpacing = 1.2.sp
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                // Station / Table Identifier Chip
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(PoetraRedPrimary.copy(alpha = 0.08f))
                        .border(1.dp, PoetraRedPrimary.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                        .clickable { onTableClick() }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("table_identifier_chip"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_kiosk_table),
                        contentDescription = null,
                        tint = PoetraRedPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = selectedTable.label.uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = PoetraRedPrimary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Change station",
                        tint = PoetraRedPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                placeholder = {
                    Text(
                        text = "Search burgers, crispy chicken, drinks...",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = PoetraRedPrimary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchQueryChanged("") },
                            modifier = Modifier.testTag("clear_search_button")
                        ) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PoetraRedPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                modifier = Modifier
                    .width(360.dp)
                    .height(50.dp)
                    .testTag("search_menu_input")
            )

            // Right Actions: Orders Status & Cart
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Order Status / History Button
                Surface(
                    shape = RoundedCornerShape(20      .dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier
                        .clickable { onOrdersClick() }
                        .testTag("order_status_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BadgedBox(
                            badge = {
                                if (ordersCount > 0) {
                                    Badge(
                                        containerColor = PoetraRedPrimary,
                                        contentColor = Color.White
                                    ) {
                                        Text("$ordersCount")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = "Orders",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Orders / Status",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                if (showCartButton) {
                    Spacer(modifier = Modifier.width(15.dp))
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = PoetraRedPrimary,
                        modifier = Modifier
                            .clickable { onCartClick() }
                            .testTag("open_cart_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BadgedBox(
                                badge = {
                                    if (cartItemCount > 0) {
                                        Badge(
                                            containerColor = PoetraAmber,
                                            contentColor = Color.Black
                                        ) {
                                            Text("$cartItemCount")
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Cart",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Cart",
                                fontWeight = FontWeight.Bold,
                                fontSize = 10   .sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true, widthDp = 1024)
@Composable
fun KioskHeaderPreview() {
    PoetraFoodTheme {
        KioskHeader(
            selectedTable = RestaurantTable(id = "T-05", label = "Table 05", isSelected = true),
            searchQuery = "",
            onSearchQueryChanged = {},
            onTableClick = {},
            onOrdersClick = {},
            onCartClick = {},
            cartItemCount = 3,
            ordersCount = 1,
            showCartButton = true
        )
    }
}
