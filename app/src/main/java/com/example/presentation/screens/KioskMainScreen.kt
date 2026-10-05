package com.example.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.Product
import com.example.presentation.components.CartSidebar
import com.example.presentation.components.CategorySidebar
import com.example.presentation.components.KioskHeader
import com.example.presentation.components.ProductCard
import com.example.presentation.components.ProductDetailDialog
import com.example.presentation.components.TableSelectorDialog
import com.example.presentation.viewmodel.KioskViewModel
import com.example.ui.theme.PoetraAmber
import com.example.ui.theme.PoetraBackground
import com.example.ui.theme.PoetraRedPrimary
import com.example.ui.theme.PoetraTextDark
import com.example.ui.theme.PoetraTextSecondary

@Composable
fun KioskMainScreen(
    viewModel: KioskViewModel,
    onNavigateToReviewOrder: () -> Unit,
    onNavigateToOrderStatus: () -> Unit
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val products by viewModel.filteredProducts.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val selectedTable by viewModel.selectedTable.collectAsStateWithLifecycle()
    val allTables by viewModel.allTables.collectAsStateWithLifecycle()
    val selectedProductForDetail by viewModel.selectedProductForDetail.collectAsStateWithLifecycle()
    val isTableDialogOpen by viewModel.isTableDialogOpen.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    containerColor = PoetraTextDark,
                    contentColor = Color.White,
                    snackbarData = data
                )
            }
        },
        topBar = {
            KioskHeader(
                selectedTable = selectedTable,
                searchQuery = searchQuery,
                onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                onTableClick = { viewModel.setTableDialogOpen(true) },
                onOrdersClick = onNavigateToOrderStatus,
                onCartClick = { /* Already visible in landscape */ },
                cartItemCount = cartItems.sumOf { it.quantity },
                ordersCount = orders.count { it.status.name != "COMPLETED" && it.status.name != "CANCELLED" },
                showCartButton = false
            )
        },
        containerColor = PoetraBackground
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            val availableWidth = maxWidth
            // In tablet landscape (typically >= 840dp or >= 600dp), show 3-pane layout
            val isExpandedLandscape = availableWidth >= 800.dp
            val gridColumns = if (availableWidth >= 1100.dp) 3 else 2

            Row(modifier = Modifier.fillMaxSize()) {
                // 1. Left Category Navigation
                CategorySidebar(
                    categories = categories,
                    selectedCategoryId = selectedCategoryId,
                    onCategorySelected = { viewModel.selectCategory(it) }
                )

                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                // 2. Center Food Menu Grid
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(PoetraBackground)
                        .padding(horizontal = 16.dp)
                ) {
                    if (products.isEmpty()) {
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
                                        imageVector = Icons.Default.SearchOff,
                                        contentDescription = null,
                                        tint = PoetraRedPrimary,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No menu items found",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = PoetraTextDark
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Try another search keyword or select a different category.",
                                    fontSize = 14.sp,
                                    color = PoetraTextSecondary
                                )
                            }
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(gridColumns),
                            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("product_grid")
                        ) {
                            items(products, key = { it.id }) { product ->
                                ProductCard(
                                    product = product,
                                    onProductClick = { viewModel.openProductDetail(product) },
                                    onQuickAdd = {
                                        viewModel.addToCart(
                                            product = product,
                                            quantity = 1
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                // 3. Right Cart / Order Summary
                CartSidebar(
                    cartItems = cartItems,
                    onIncreaseQuantity = { viewModel.updateCartQuantity(it.id, it.quantity + 1) },
                    onDecreaseQuantity = { viewModel.updateCartQuantity(it.id, it.quantity - 1) },
                    onRemoveItem = { viewModel.removeCartItem(it.id) },
                    onClearCart = { viewModel.clearCart() },
                    onReviewOrder = onNavigateToReviewOrder
                )
            }
        }
    }

    // Product Detail Dialog
    selectedProductForDetail?.let { product ->
        ProductDetailDialog(
            product = product,
            onAddToCart = { prod, qty, customizations, customPrice, notes ->
                viewModel.addToCart(
                    product = prod,
                    quantity = qty,
                    customizations = customizations,
                    customizationPrice = customPrice,
                    itemNotes = notes
                )
                viewModel.closeProductDetail()
            },
            onDismiss = { viewModel.closeProductDetail() }
        )
    }

    // Table Selector Dialog
    if (isTableDialogOpen) {
        TableSelectorDialog(
            tables = allTables,
            selectedTable = selectedTable,
            onTableSelected = { viewModel.selectTable(it) },
            onDismiss = { viewModel.setTableDialogOpen(false) }
        )
    }
}
