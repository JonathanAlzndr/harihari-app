package com.alezandrow.simplecleanarchitecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alezandrow.simplecleanarchitecture.presentation.state.SessionState
import com.alezandrow.simplecleanarchitecture.presentation.theme.SimpleCleanArchitectureTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val sessionViewModel: SessionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            sessionViewModel.sessionState.value is SessionState.Loading
        }
        enableEdgeToEdge()
        setContent {
            SimpleCleanArchitectureTheme {
                RootAppNavigation(sessionViewModel)
            }
        }
    }
}