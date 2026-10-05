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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.poetrafood.R
import com.example.domain.model.Product
import com.example.domain.model.formatRupiah
import com.example.ui.theme.PoetraAmber
import com.example.ui.theme.PoetraAmberDark
import com.example.ui.theme.PoetraRedPrimary
import com.example.ui.theme.PoetraTextDark
import com.example.ui.theme.PoetraTextSecondary

data class AddOnItem(
    val id: String,
    val name: String,
    val price: Double
)

@Composable
fun ProductDetailDialog(
    product: Product,
    onAddToCart: (product: Product, quantity: Int, customizations: String, customizationPrice: Double, notes: String) -> Unit,
    onDismiss: () -> Unit
) {
    var quantity by remember { mutableIntStateOf(1) }
    var notes by remember { mutableStateOf("") }

    // Customization state
    val selectedAddOns = remember { mutableStateListOf<AddOnItem>() }
    var spiceLevel by remember { mutableStateOf("Regular") }
    var noChili by remember { mutableStateOf(false) }
    var noPickles by remember { mutableStateOf(false) }
    var noOnions by remember { mutableStateOf(false) }
    var drinkIceLevel by remember { mutableStateOf("Normal Ice") }
    var drinkSugarLevel by remember { mutableStateOf("Normal Sugar") }

    val availableAddOns = remember(product.categoryId) {
        when (product.categoryId.lowercase()) {
            "drinks" -> listOf(
                AddOnItem("pearl", "Grass Jelly / Pearl", 4000.0),
                AddOnItem("pudding", "Egg Pudding", 5000.0)
            )
            "snacks" -> listOf(
                AddOnItem("cheese_dip", "Melted Cheese Dip", 4000.0),
                AddOnItem("bbq_dip", "Smoky BBQ Sauce", 3000.0)
            )
            else -> listOf(
                AddOnItem("extra_cheese", "Extra Melted Cheese", 4000.0),
                AddOnItem("extra_sauce", "Extra Secret Sauce", 2000.0),
                AddOnItem("fried_egg", "Sunny Side Up Egg", 5000.0)
            )
        }
    }

    val addOnsPrice = selectedAddOns.sumOf { it.price }
    val unitPrice = product.price + addOnsPrice
    val totalPrice = unitPrice * quantity
    val focusManager = LocalFocusManager.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(600.dp)
                .imePadding()
                .testTag("product_detail_dialog")
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                // Left Column: Visual & Overview
                Box(
                    modifier = Modifier
                        .weight(0.42f)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Top bar inside left col
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!product.badge.isNullOrBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = PoetraRedPrimary
                                ) {
                                    Text(
                                        text = product.badge,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.width(1.dp))
                            }

                            if (product.calories > 0) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.Black.copy(alpha = 0.08f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocalFireDepartment,
                                        contentDescription = null,
                                        tint = PoetraAmberDark,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${product.calories} kcal",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        // Food Image Center
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

                        Box(
                            modifier = Modifier
                                .size(180.dp)
                                .clip(CircleShape)
                                .background(PoetraAmber.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
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
                                Icon(
                                    painter = painterResource(id = categoryIconRes),
                                    contentDescription = product.name,
                                    tint = PoetraRedPrimary,
                                    modifier = Modifier.size(90.dp)
                                )
                            }
                        }

                        // Product Title & Price
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = PoetraTextDark
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = formatRupiah(product.price),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = PoetraRedPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = product.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = PoetraTextSecondary,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                // Right Column: Customization & Actions
                Column(
                    modifier = Modifier
                        .weight(0.58f)
                        .fillMaxHeight()
                        .padding(24.dp)
                ) {
                    // Header with close button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Customize Your Item",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PoetraTextDark
                        )
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("close_detail_dialog_button")
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Scrollable Customization Body
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // 1. Add-Ons
                        Text(
                            text = "ADD-ONS & EXTRAS",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = PoetraAmberDark,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        availableAddOns.forEach { addOn ->
                            val isChecked = selectedAddOns.any { it.id == addOn.id }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        if (isChecked) {
                                            selectedAddOns.removeAll { it.id == addOn.id }
                                        } else {
                                            selectedAddOns.add(addOn)
                                        }
                                    }
                                    .padding(vertical = 4.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = { checked ->
                                            if (checked) selectedAddOns.add(addOn)
                                            else selectedAddOns.removeAll { it.id == addOn.id }
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = PoetraRedPrimary),
                                        modifier = Modifier.testTag("addon_checkbox_${addOn.id}")
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = addOn.name,
                                        fontWeight = FontWeight.Medium,
                                        color = PoetraTextDark,
                                        fontSize = 14.sp
                                    )
                                }
                                Text(
                                    text = "+${formatRupiah(addOn.price)}",
                                    fontWeight = FontWeight.Bold,
                                    color = PoetraRedPrimary,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 2. Preferences / Options based on category
                        if (product.categoryId.equals("drinks", ignoreCase = true)) {
                            Text(
                                text = "ICE LEVEL",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = PoetraAmberDark,
                                letterSpacing = 1.sp
                            )
                            Row(modifier = Modifier.fillMaxWidth()) {
                                listOf("Normal Ice", "Less Ice", "No Ice").forEach { option ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clickable { drinkIceLevel = option }
                                            .padding(end = 16.dp, top = 4.dp, bottom = 4.dp)
                                    ) {
                                        RadioButton(
                                            selected = drinkIceLevel == option,
                                            onClick = { drinkIceLevel = option },
                                            colors = RadioButtonDefaults.colors(selectedColor = PoetraRedPrimary)
                                        )
                                        Text(text = option, fontSize = 13.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "SWEETNESS",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = PoetraAmberDark,
                                letterSpacing = 1.sp
                            )
                            Row(modifier = Modifier.fillMaxWidth()) {
                                listOf("Normal Sugar", "Less Sugar", "No Sugar").forEach { option ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clickable { drinkSugarLevel = option }
                                            .padding(end = 16.dp, top = 4.dp, bottom = 4.dp)
                                    ) {
                                        RadioButton(
                                            selected = drinkSugarLevel == option,
                                            onClick = { drinkSugarLevel = option },
                                            colors = RadioButtonDefaults.colors(selectedColor = PoetraRedPrimary)
                                        )
                                        Text(text = option, fontSize = 13.sp)
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "SPICE LEVEL",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = PoetraAmberDark,
                                letterSpacing = 1.sp
                            )
                            Row(modifier = Modifier.fillMaxWidth()) {
                                listOf("Mild", "Regular", "Extra Spicy").forEach { level ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clickable { spiceLevel = level }
                                            .padding(end = 16.dp, top = 4.dp, bottom = 4.dp)
                                    ) {
                                        RadioButton(
                                            selected = spiceLevel == level,
                                            onClick = { spiceLevel = level },
                                            colors = RadioButtonDefaults.colors(selectedColor = PoetraRedPrimary)
                                        )
                                        Text(text = level, fontSize = 13.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "SPECIAL PREFERENCES",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = PoetraAmberDark,
                                letterSpacing = 1.sp
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { noChili = !noChili }
                                ) {
                                    Checkbox(
                                        checked = noChili,
                                        onCheckedChange = { noChili = it },
                                        colors = CheckboxDefaults.colors(checkedColor = PoetraRedPrimary)
                                    )
                                    Text("No Chili", fontSize = 13.sp)
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { noPickles = !noPickles }
                                ) {
                                    Checkbox(
                                        checked = noPickles,
                                        onCheckedChange = { noPickles = it },
                                        colors = CheckboxDefaults.colors(checkedColor = PoetraRedPrimary)
                                    )
                                    Text("No Pickles", fontSize = 13.sp)
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { noOnions = !noOnions }
                                ) {
                                    Checkbox(
                                        checked = noOnions,
                                        onCheckedChange = { noOnions = it },
                                        colors = CheckboxDefaults.colors(checkedColor = PoetraRedPrimary)
                                    )
                                    Text("No Onions", fontSize = 13.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Special instructions text field
                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            placeholder = { Text("Special instructions (e.g. sauce on side)...", fontSize = 13.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PoetraRedPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("special_instructions_input")
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Bottom Bar: Quantity Selector & Add Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Quantity selector
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            IconButton(
                                onClick = { if (quantity > 1) quantity-- },
                                enabled = quantity > 1,
                                modifier = Modifier
                                    .size(44.dp)
                                    .testTag("decrease_quantity_button")
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease")
                            }

                            Text(
                                text = "$quantity",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            IconButton(
                                onClick = { quantity++ },
                                modifier = Modifier
                                    .size(44.dp)
                                    .testTag("increase_quantity_button")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase")
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Primary Action Button
                        Button(
                            onClick = {
                                val customParts = mutableListOf<String>()
                                if (selectedAddOns.isNotEmpty()) {
                                    customParts.add(selectedAddOns.joinToString(", ") { it.name })
                                }
                                if (product.categoryId.equals("drinks", ignoreCase = true)) {
                                    customParts.add(drinkIceLevel)
                                    customParts.add(drinkSugarLevel)
                                } else {
                                    if (spiceLevel != "Regular") customParts.add(spiceLevel)
                                    if (noChili) customParts.add("No chili")
                                    if (noPickles) customParts.add("No pickles")
                                    if (noOnions) customParts.add("No onions")
                                }
                                val customSummary = customParts.joinToString(" • ")

                                onAddToCart(
                                    product,
                                    quantity,
                                    customSummary,
                                    addOnsPrice,
                                    notes
                                )
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PoetraRedPrimary,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .testTag("add_to_order_confirm_button")
                        ) {
                            Text(
                                text = "ADD TO ORDER • ${formatRupiah(totalPrice)}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
