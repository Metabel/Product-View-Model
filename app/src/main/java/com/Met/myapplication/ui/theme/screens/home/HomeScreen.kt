package com.Met.myapplication.ui.theme.screens.home

import android.R
import android.accessibilityservice.GestureDescription
import androidx.appcompat.widget.DialogTitle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.Met.myapplication.ui.theme.navigation.ROUTE_ADDPRODUCT
import com.Met.myapplication.ui.theme.navigation.ROUTE_LISTPRODUCT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController){
Scaffold (
    topBar = {
        TopAppBar(
            title = {Text("Dashboard",color= Color.White)},
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color( 0xFF2196F3)
            )
        )
    },
    bottomBar = {
        BottomNavigationBar(navController)
    }
){padding->
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        HomeCard(
            title="Add product",
            description="Upload a new product with image",
            onClick={navController.navigate(ROUTE_ADDPRODUCT)}
        )
        HomeCard(
            title="List Products",
            description="View all products in the store",
           onClick={navController.navigate(ROUTE_LISTPRODUCT)}
        )
    }
}
}
@Composable
fun HomeCard(title: String,description: String, onClick: () -> Unit){
    Card (
        modifier = Modifier
            .fillMaxSize()
            .clickable{onClick()},
        elevation = CardDefaults.cardElevation(6.dp)
    ){
        Column (modifier = Modifier.padding(16.dp)){
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
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
            onClick = { navController.navigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
            label = { Text("Add") },
            selected = false,
            onClick = { navController.navigate("add_product") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "List") },
            label = { Text("List") },
            selected = false,
            onClick = { navController.navigate("list_products") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview(){
    HomeScreen(rememberNavController())
}