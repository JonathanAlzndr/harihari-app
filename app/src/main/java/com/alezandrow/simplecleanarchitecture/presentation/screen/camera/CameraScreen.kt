package com.alezandrow.simplecleanarchitecture.presentation.screen.camera

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.alezandrow.simplecleanarchitecture.presentation.component.camera
import java.io.File

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var photoUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var hasPermission by remember {
        mutableStateOf(false)
    }

    val imageCapture = remember {
        ImageCapture.Builder().build()
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            hasPermission = granted
        }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            hasPermission = true
        } else {
            permissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        }
    }

    if (hasPermission) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->

                    val previewView = PreviewView(ctx)

                    val cameraProviderFuture =
                        ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({

                        val cameraProvider =
                            cameraProviderFuture.get()

                        val preview =
                            Preview.Builder()
                                .build()

                        preview.surfaceProvider =
                            previewView.surfaceProvider

                        val cameraSelector =
                            CameraSelector.DEFAULT_BACK_CAMERA

                        cameraProvider.unbindAll()

                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageCapture
                        )

                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                }
            )

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                onClick = {
                    val photoFile = File(
                        context.cacheDir,
                        "${System.currentTimeMillis()}.jpg"
                    )

                    val outputOptions = ImageCapture.OutputFileOptions
                        .Builder(photoFile)
                        .build()

                    imageCapture.takePicture(
                        outputOptions,
                        ContextCompat.getMainExecutor(context),

                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(p0: ImageCapture.OutputFileResults) {
                                photoUri = photoFile.toUri()
                            }

                            override fun onError(p0: ImageCaptureException) {
                                p0.printStackTrace()
                            }
                        }
                    )
                }
            ) {
                Icon(
                    imageVector = camera,
                    contentDescription = "Take Picture"
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Camera permission required")
        }
    }
}