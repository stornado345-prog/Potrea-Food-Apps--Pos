package com.example.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poetrafood.R
import com.example.domain.model.Category
import com.example.ui.theme.PoetraAmber
import com.example.ui.theme.PoetraFoodTheme
import com.example.ui.theme.PoetraRedPrimary

@Composable
fun CategorySidebar(
    categories: List<Category>,
    selectedCategoryId: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = modifier
            .width(220.dp)
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 16.dp, horizontal = 12.dp)
        ) {
            Text(
                text = "CATEGORIES",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories, key = { it.id }) { category ->
                    val isSelected = category.id.equals(selectedCategoryId, ignoreCase = true)

                    val bgColor by animateColorAsState(
                        targetValue = if (isSelected) PoetraRedPrimary else Color.Transparent,
                        animationSpec = tween(durationMillis = 200),
                        label = "category_bg"
                    )
                    val contentColor by animateColorAsState(
                        targetValue = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                        animationSpec = tween(durationMillis = 200),
                        label = "category_text"
                    )

                    val iconRes = when (category.id.lowercase()) {
                        "burgers" -> R.drawable.ic_category_burger
                        "chicken" -> R.drawable.ic_category_chicken
                        "rice" -> R.drawable.ic_category_rice
                        "snacks" -> R.drawable.ic_category_snack
                        "drinks" -> R.drawable.ic_category_drink
                        "combos" -> R.drawable.ic_category_combo
                        else -> R.drawable.ic_category_all
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(bgColor)
                            .clickable { onCategorySelected(category.id) }
                            .padding(horizontal = 16.dp)
                            .testTag("category_tab_${category.id}"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) PoetraAmber else MaterialTheme.colorScheme.surfaceVariant
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = null,
                                tint = if (isSelected) Color.Black else PoetraRedPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = category.name,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 15.sp,
                            color = contentColor
                        )
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun CategorySidebarPreview() {
    PoetraFoodTheme {
        CategorySidebar(
            categories = listOf(
                Category("all", "All Menu", "ic_category_all", 1),
                Category("burgers", "Burgers", "ic_category_burger", 2),
                Category("chicken", "Chicken", "ic_category_chicken", 3),
                Category("drinks", "Drinks", "ic_category_drink", 4)
            ),
            selectedCategoryId = "burgers",
            onCategorySelected = {}
        )
    }
}