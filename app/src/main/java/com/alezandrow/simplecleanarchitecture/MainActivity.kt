package com.alezandrow.simplecleanarchitecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alezandrow.simplecleanarchitecture.presentation.HomeScreen
import com.alezandrow.simplecleanarchitecture.presentation.theme.SimpleCleanArchitectureTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleCleanArchitectureTheme {
                HomeScreen()
            }
        }
    }
}