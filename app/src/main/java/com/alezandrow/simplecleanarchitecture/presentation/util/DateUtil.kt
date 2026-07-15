package com.alezandrow.simplecleanarchitecture.presentation.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long?.toFormattedDate(): String {
    return this?.let {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(this))
    } ?: "Set due date"
}