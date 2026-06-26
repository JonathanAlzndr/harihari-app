package com.alezandrow.simplecleanarchitecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alezandrow.simplecleanarchitecture.presentation.navigation.AppNavViewModel
import com.alezandrow.simplecleanarchitecture.presentation.state.SessionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val appNavViewModel: AppNavViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            appNavViewModel.sessionState.value is SessionState.Loading
        }
        enableEdgeToEdge()
        setContent {
            App(appNavViewModel)
        }
    }
}