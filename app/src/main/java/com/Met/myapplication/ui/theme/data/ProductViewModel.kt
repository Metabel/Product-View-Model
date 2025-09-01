package com.Met.myapplication.ui.theme.data

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import com.Met.myapplication.ui.theme.models.Product
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import supabaseClientProvider.SupabaseClientProvider
import java.util.UUID

class ProductViewModel(
    private val navController: NavHostController,
    private val context: Context
) {
    private val supabase = SupabaseClientProvider.supabase

    fun addProduct(name: String, description: String, price: Double, imageUri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val bytes = context.contentResolver.openInputStream(imageUri)?.readBytes()
                    ?: throw Exception("Unable to read image")

                val fileName = "${System.currentTimeMillis()}.jpg"

                // Upload to Supabase Storage
                supabase.storage.from("Mumbi").upload(fileName, bytes)

                // Get public URL
                val publicImageUrl = supabase.storage.from("Mumbi").publicUrl(fileName)

                // Create Product object
                val product = Product(
                    name = name,
                    description = description,
                    price = price,
                    image_url = publicImageUrl
                )

                // Insert into Supabase table
                supabase.from("Products1").insert(product)

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Product added!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun allProducts(
        product: MutableState<Product>,
        products: SnapshotStateList<Product>
    ): SnapshotStateList<Product> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = supabase.from("Products1").select()

                println("RAW SUPABASE JSON: ${response.data}")

                val result = response.decodeList<Product>()

                withContext(Dispatchers.Main) {
                    products.clear()
                    products.addAll(result)
                    if (result.isNotEmpty()) {
                        product.value = result.first()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Error loading products: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return products
    }

    // delete a product
    fun deleteProduct(productId: Int, products: SnapshotStateList<Product>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                supabase.from("Products1").delete {
                    filter {
                        eq("id", productId)
                    }
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Product deleted", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Delete failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // update a product
    fun updateProduct(
        productId: Int,
        name: String,
        description: String,
        price: Double?,
        imageUri: Uri? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var imageURL: String? = null
                // upload new image if needed
                if (imageUri != null) {
                    val bytes = context.contentResolver.openInputStream(imageUri)?.readBytes()
                        ?: throw Exception("Unable to read image")
                    val fileName = "${UUID.randomUUID()}.jpg"
                    supabase.storage.from("Mumbi").upload(fileName, bytes)
                    imageURL = supabase.storage.from("Mumbi").publicUrl(fileName)
                }

                // fetch existing product to preserve old data
                val existingProduct = getProductById(productId)

                // Build updated product object
                val updatedProduct = Product(
                    id = productId,
                    name = name,
                    description = description,
                    price = price ?: existingProduct?.price ?: 0.0,
                    image_url = imageURL ?: existingProduct?.image_url.orEmpty()
                )

                // update product row
                supabase.from("Products1").update(updatedProduct) {
                    filter {
                        eq("id", productId)
                    }
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Product updated!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Update failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // get a single product
    suspend fun getProductById(productId: Int): Product? {
        return try {
            val response = supabase.from("Products1")
                .select {
                    filter {
                        eq("id", productId)
                    }
                    limit(1)
                }
                .decodeSingleOrNull<Product>() // ✅ returns null if not found

            response
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error loading product: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }
}
