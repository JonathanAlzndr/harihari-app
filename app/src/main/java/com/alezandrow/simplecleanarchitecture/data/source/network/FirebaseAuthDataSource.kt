package com.alezandrow.simplecleanarchitecture.data.source.network

import com.alezandrow.simplecleanarchitecture.data.mapper.toAuthUser
import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(
    private val auth: FirebaseAuth,
) {

    suspend fun signIn(
        email: String,
        password: String
    ): AuthUser? {
        return auth.signInWithEmailAndPassword(email, password).await()?.toAuthUser()
    }

    suspend fun signUp(
        email: String,
        password: String,
    ): AuthUser {
        return auth
            .createUserWithEmailAndPassword(email, password)
            .await()
            .toAuthUser()
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.await()
    }

    suspend fun requestPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    suspend fun updatePassword(currentPassword: String, newPassword: String) {
        val user = auth.currentUser!!
        val credential = EmailAuthProvider.getCredential(
            user.email!!, currentPassword
        )
        user.reauthenticate(credential).await()
        auth.currentUser?.updatePassword(newPassword)?.await()
    }

    fun deleteUser() {
        auth.currentUser!!.delete()
    }

    suspend fun signInWithGoogleIdToken(idToken: String): AuthUser {
        val firebaseAuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        return auth.signInWithCredential(firebaseAuthCredential).await().toAuthUser()
    }

}