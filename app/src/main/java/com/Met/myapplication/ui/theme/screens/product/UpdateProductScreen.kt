package com.Met.myapplication.ui.theme.screens.product

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.Met.myapplication.ui.theme.data.ProductViewModel
import com.Met.myapplication.ui.theme.models.Product

@Composable
fun UpdateProductScreen(
    navController: NavHostController,
    productId: Int,
) {
    val context = LocalContext.current
    val productViewModel = ProductViewModel(navController, context)

    // fetch product from viewmodel
    val product by produceState<Product?>(initialValue = null, productId) {
        value = productViewModel.getProductById(productId)
    }

    if (product == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Loading product...")
        }
    } else {
        var name by remember { mutableStateOf(product!!.name) }
        var description by remember { mutableStateOf(product!!.description) }
        var price by remember { mutableStateOf(product!!.price.toString()) }
        var imageUri by remember { mutableStateOf<Uri?>(null) }

        val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            imageUri = uri
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Update Product", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            val imageSource: Any? = imageUri ?: product?.image_url
            imageSource?.let { source ->
                Image(
                    painter = rememberAsyncImagePainter(model = source),
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = {
                imagePickerLauncher.launch("image/*")
            }) {
                Text("Change Image")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val priceDouble = price.toDoubleOrNull()
                if (priceDouble == null) {
                    Toast.makeText(context, "Invalid price", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                productViewModel.updateProduct(
                    productId = productId,
                    name = name,
                    description = description,
                    price = priceDouble,
                    imageUri = imageUri
                )
            }) {
                Text("Update Product")
            }
        }
    }
}

