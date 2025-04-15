package com.example.bookxpert.auth

import android.content.Context
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleAuthUiClient(
    private val context: Context
) {
    private val oneTapClient: SignInClient = Identity.getSignInClient(context)
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("855621108591-vp2bsu4ask0p1r8j8l7pv7ndshiup588.apps.googleusercontent.com")
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    suspend fun beginSignIn(): IntentSender? {
        return try {
            val result = oneTapClient.beginSignIn(signInRequest).await()
            result.pendingIntent.intentSender
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun signInWithIntent(intent: android.content.Intent): FirebaseUser? {
        try {
            val credential = oneTapClient.getSignInCredentialFromIntent(intent)
            val idToken = credential.googleIdToken
            val googleCredential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(googleCredential).await()
            return result.user
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun getSignedInUser(): FirebaseUser? = auth.currentUser

    fun signOut() {
        auth.signOut()
    }
}

