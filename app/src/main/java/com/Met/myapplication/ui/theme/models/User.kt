package com.Met.myapplication.ui.theme.models

import androidx.compose.ui.text.input.PasswordVisualTransformation
import io.github.jan.supabase.gotrue.admin.AdminUserBuilder

data class User(
    var fullname:String="",
    var email: String="",
    var passwordVisualTransformation:String="",
    var userId:String="",
)