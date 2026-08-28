package com.alezandrow.simplecleanarchitecture.data.mapper

import com.alezandrow.simplecleanarchitecture.common.AppError
import com.google.firebase.firestore.FirebaseFirestoreException

object FirestoreErrorMapper {
    fun map(e: Exception): AppError {
        return when (e) {
            is FirebaseFirestoreException -> {
                when (e.code) {
                    FirebaseFirestoreException.Code.PERMISSION_DENIED ->
                        AppError.PermissionDenied

                    FirebaseFirestoreException.Code.NOT_FOUND ->
                        AppError.NotFound

                    FirebaseFirestoreException.Code.ALREADY_EXISTS ->
                        AppError.AlreadyExists

                    FirebaseFirestoreException.Code.UNAVAILABLE ->
                        AppError.Network

                    FirebaseFirestoreException.Code.DEADLINE_EXCEEDED ->
                        AppError.Timeout

                    FirebaseFirestoreException.Code.CANCELLED ->
                        AppError.Cancelled

                    FirebaseFirestoreException.Code.ABORTED ->
                        AppError.OperationAborted

                    FirebaseFirestoreException.Code.UNAUTHENTICATED ->
                        AppError.Unauthenticated

                    else ->
                        AppError.Unknown(e.message ?: "Unknown Firestore error")
                }
            }
            else -> AppError.Unknown(e.message ?: "Unknown Firestore error")
        }
    }
}