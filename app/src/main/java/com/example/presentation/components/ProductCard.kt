package com.example.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poetrafood.R
import com.example.domain.model.Product
import com.example.ui.theme.PoetraAmber
import com.example.ui.theme.PoetraAmberDark
import com.example.ui.theme.PoetraFoodTheme
import com.example.ui.theme.PoetraRedLight
import com.example.ui.theme.PoetraRedPrimary
import com.example.ui.theme.PoetraTextDark
import com.example.ui.theme.PoetraTextSecondary

@Composable
fun ProductCard(
    product: Product,
    onProductClick: (Product) -> Unit,
    onQuickAdd: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp, pressedElevation = 6.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onProductClick(product) }
            .testTag("product_card_${product.id}")
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Food Image / Visual Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surfaceVariant,
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Determine image/icon representation based on product
                val productImageRes = when (product.imageRes) {
                    "ic_poetra_burger" -> R.drawable.ic_poetra_burger
                    "ic_cheese_burger" -> R.drawable.ic_cheese_burger
                    "ic_crispy_chicken_burger" -> R.drawable.ic_crispy_chicken_burger
                    "ic_double_beef_burger" -> R.drawable.ic_double_beef_burger
                    "ic_crispy_chiken" -> R.drawable.ic_crispy_chiken
                    "ic_spicy_chiken" -> R.drawable.ic_spicy_chiken
                    "ic_chiken_winggs" -> R.drawable.ic_chiken_winggs
                    "ic_nasi_goreng_spesial" -> R.drawable.ic_nasi_goreng_spesial
                    "ic_crispy_chicken_rice" -> R.drawable.ic_crispy_chicken_rice
                    "ic_spicy_chiken_rice" -> R.drawable.ic_spicy_chiken_rice
                    "ic_freenfreice" -> R.drawable.ic_freenfreice
                    "ic_chiken_nuget" -> R.drawable.ic_chiken_nuget
                    "ic_onion_rings" -> R.drawable.ic_onion_rings
                    "ic_cola" -> R.drawable.ic_cola
                    "ic_orange_drink" -> R.drawable.ic_orange_drink
                    "ic_lemon_drink" -> R.drawable.ic_lemon_drink
                    "ic_mineral_water" -> R.drawable.ic_mineral_water
                    else -> when (product.name.lowercase()) {
                        "poetra burger" -> R.drawable.ic_poetra_burger
                        "cheese burger" -> R.drawable.ic_cheese_burger
                        "crispy chicken burger" -> R.drawable.ic_crispy_chicken_burger
                        "double beef burger" -> R.drawable.ic_double_beef_burger
                        "crispy chicken" -> R.drawable.ic_crispy_chiken
                        "spicy chicken" -> R.drawable.ic_spicy_chiken
                        "chicken wings" -> R.drawable.ic_chiken_winggs
                        "nasi goreng spesial", "nasi goreng spacial" -> R.drawable.ic_nasi_goreng_spesial
                        "crispy chicken rice" -> R.drawable.ic_crispy_chicken_rice
                        "spicy chicken rice" -> R.drawable.ic_spicy_chiken_rice
                        "french fries" -> R.drawable.ic_freenfreice
                        "chicken nuggets", "chicken nugget" -> R.drawable.ic_chiken_nuget
                        "onion rings" -> R.drawable.ic_onion_rings
                        "cola" -> R.drawable.ic_cola
                        "orange drink" -> R.drawable.ic_orange_drink
                        "lemon tea", "lemon drink" -> R.drawable.ic_lemon_drink
                        "mineral water" -> R.drawable.ic_mineral_water
                        else -> null
                    }
                }

                val categoryIconRes = when (product.categoryId.lowercase()) {
                    "burgers" -> R.drawable.ic_category_burger
                    "chicken" -> R.drawable.ic_category_chicken
                    "rice" -> R.drawable.ic_category_rice
                    "snacks" -> R.drawable.ic_category_snack
                    "drinks" -> R.drawable.ic_category_drink
                    "combos" -> R.drawable.ic_category_combo
                    else -> R.drawable.ic_food_placeholder
                }

                if (productImageRes != null) {
                    Image(
                        painter = painterResource(id = productImageRes),
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (product.categoryId == "combos") {
                    Image(
                        painter = painterResource(id = R.drawable.poetra_hero_banner),
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(PoetraAmber.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = categoryIconRes),
                            contentDescription = product.name,
                            tint = PoetraRedPrimary,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }

                // Badge top-left
                if (!product.badge.isNullOrBlank()) {
                    Surface(
                        shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 12.dp, topEnd = 0.dp, bottomStart = 0.dp),
                        color = when (product.badge) {
                            "BEST SELLER", "SUPER SAVER" -> PoetraRedPrimary
                            "SPICY", "HOT" -> PoetraRedLight
                            "CHEF'S PICK", "BEST VALUE" -> PoetraAmberDark
                            else -> PoetraAmber
                        },
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = product.badge,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                // Calorie pill top-right
                if (product.calories > 0) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Black.copy(alpha = 0.55f),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = PoetraAmber,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${product.calories} kcal",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Product Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PoetraTextDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = PoetraTextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp,
                    modifier = Modifier.height(34.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "PRICE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = PoetraTextSecondary
                        )
                        Text(
                            text = product.formattedPrice,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = PoetraRedPrimary
                        )
                    }

                    Button(
                        onClick = { onQuickAdd(product) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PoetraRedPrimary,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .height(40.dp)
                            .testTag("quick_add_button_${product.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Add",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    PoetraFoodTheme {
        ProductCard(
            product = Product(
                id = 1,
                name = "Nasi Goreng Spesial",
                categoryId = "rice",
                description = "Nasi goreng lezat dengan telur mata sapi dan ayam suwir khas Poetra Food",
                price = 25000.0,
                imageRes = "ic_product_nasi_goreng",
                badge = "BESTSELLER",
                calories = 520
            ),
            onProductClick = {},
            onQuickAdd = {}
        )
    }
}