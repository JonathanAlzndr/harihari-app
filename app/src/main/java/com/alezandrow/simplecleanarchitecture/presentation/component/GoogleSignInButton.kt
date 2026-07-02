package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alezandrow.simplecleanarchitecture.R
import com.alezandrow.simplecleanarchitecture.presentation.theme.SimpleCleanArchitectureTheme

@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent,
        shadowElevation = 0.dp
    ) {
        Image(
            painter = painterResource(R.drawable.android_neutral_rd_ctn),
            contentDescription = "Continue with Google",
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun GoogleSignInButtonPreview() {
    SimpleCleanArchitectureTheme {
        GoogleSignInButton {  }
    }
}