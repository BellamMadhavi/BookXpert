package com.example.bookxpert.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AuthViewModelFactory(private val authClient: GoogleAuthUiClient) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authClient) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
