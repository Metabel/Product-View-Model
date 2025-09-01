
package com.Met.myapplication.ui.theme.screens.product

import android.R
import android.R.attr.onClick
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.Met.myapplication.ui.theme.data.ProductViewModel
import com.Met.myapplication.ui.theme.models.Product
import com.Met.myapplication.ui.theme.navigation.ROUTE_ADDPRODUCT
import com.Met.myapplication.ui.theme.navigation.ROUTE_HOME
import com.Met.myapplication.ui.theme.navigation.ROUTE_LISTPRODUCT
import com.Met.myapplication.ui.theme.navigation.ROUTE_UPDATEPRODUCT
import com.Met.myapplication.ui.theme.screens.home.BottomNavigationBar
import com.google.android.play.core.integrity.p

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListProductsScreen(navController: NavHostController) {
    val product = remember { mutableStateOf(Product(0, "", "", 0.0, "")) }
    val products = remember { mutableStateListOf<Product>() }
    val context = LocalContext.current
    val productViewModel = ProductViewModel(navController, context)

    LaunchedEffect(Unit) {
        productViewModel.allProducts(product,products)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            items(products) { prod ->
                ProductItem(
                    product = prod,
                    onEdit = {
                        navController.navigate("$ROUTE_UPDATEPRODUCT/${prod.id}")
                        // Navigate to your EditProductScreen with the product ID

                    },
                    onDelete = {
                        // productViewModel.deleteProduct(prod.id ?: 0)
                       productViewModel.deleteProduct(prod.id!!,products)
                    }
                )
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column (
            modifier = Modifier.padding(16.dp)){
            //Product Image
            Image(
                painter = rememberAsyncImagePainter(product.image_url),
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(bottom = 8.dp),
                contentScale = ContentScale.Crop
            )
            //Product info
            Text(product.name?:"", style = MaterialTheme.typography.titleMedium)
            Text(product.description?:"", style = MaterialTheme.typography.bodyMedium)
            Text("ksh${product.price?: 0.0}", style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray)
            Spacer(modifier = Modifier.height(12.dp))
            //Buttons row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "Edit",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable{onEdit()}
                )
                Text(
                    text="Delete",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable{onDelete()}
                )
            }
        }
            }
        }

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        containerColor = Color(0xFF2196F3),
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = {navController.navigate(ROUTE_HOME)}
            )
        NavigationBarItem(
            icon = {Icon(Icons.Default.Add, contentDescription = "Add")},
            label = {Text("Add")},
            selected = false,
            onClick = {navController.navigate(ROUTE_ADDPRODUCT)}
        )
        NavigationBarItem(
            icon = {Icon(Icons.Default.List, contentDescription = "List")},
            label = {Text("List")},
            selected = false,
            onClick = {navController.navigate(ROUTE_LISTPRODUCT)}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ListPreview() {
    ListProductsScreen(rememberNavController())
}
