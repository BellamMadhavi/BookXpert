package com.example.bookxpert.auth

import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authClient: GoogleAuthUiClient) : ViewModel() {

    private val _user = MutableStateFlow<FirebaseUser?>(authClient.getSignedInUser())
    val user: StateFlow<FirebaseUser?> = _user

    fun signOut() {
        authClient.signOut()
        _user.value = null
    }

    suspend fun beginSignIn(): IntentSender? {
        return authClient.beginSignIn()
    }

    fun handleSignInResult(intent: Intent) {
        viewModelScope.launch {
            val user = authClient.signInWithIntent(intent)
            _user.value = user
        }
    }
}
