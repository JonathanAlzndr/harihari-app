package com.alezandrow.simplecleanarchitecture.presentation.screen.add_task

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class AddTaskViewModel : ViewModel() {

    private var _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri = _imageUri.asStateFlow()

    fun setImageUri(imageUri: Uri) {
        _imageUri.value = imageUri
    }

}