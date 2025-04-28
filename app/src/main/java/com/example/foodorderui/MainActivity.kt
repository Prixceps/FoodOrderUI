package com.example.foodorderui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodorderui.ui.theme.FoodOrderUITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodOrderUITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FoodOrderScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodOrderScreen() {
    val context = LocalContext.current
    val categories = listOf("All", "Fast Food", "Italian", "Desserts")
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var expanded by remember { mutableStateOf(false) }

    // Food items with their category
    val foodItems = listOf(
        FoodItem("Pizza", "Italian"),
        FoodItem("Burger", "Fast Food"),
        FoodItem("Fries", "Fast Food"),
        FoodItem("Pasta", "Italian"),
        FoodItem("Ice Cream", "Desserts"),
        FoodItem("Cake", "Desserts")
    )

    // Track selected food items
    val selectedItems = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Food Order App",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Category Dropdown (Spinner)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Category") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            selectedCategory = category
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Food Items Checkboxes
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(foodItems.filter { it.category == selectedCategory || selectedCategory == "All" }) { food ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedItems.contains(food.name),
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                selectedItems.add(food.name)
                            } else {
                                selectedItems.remove(food.name)
                            }
                        }
                    )
                    Text(
                        text = food.name,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        // Selected Items Display
        Text(
            text = if (selectedItems.isEmpty()) {
                "No items selected"
            } else {
                "Selected: ${selectedItems.joinToString(", ")}"
            },
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Place Order Button
        Button(
            onClick = {
                val orderSummary = if (selectedItems.isEmpty()) {
                    "No items selected"
                } else {
                    "Order placed for: ${selectedItems.joinToString(", ")}"
                }
                Toast.makeText(context, orderSummary, Toast.LENGTH_LONG).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Place Order")
        }
    }
}

data class FoodItem(val name: String, val category: String)