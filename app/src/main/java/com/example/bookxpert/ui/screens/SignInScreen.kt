package com.example.bookxpert.ui.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookxpert.auth.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    viewModel: AuthViewModel,
    onSignedIn: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                viewModel.handleSignInResult(data)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome to BookXpert!")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                coroutineScope.launch {
                    val intentSender = viewModel.beginSignIn()
                    if (intentSender != null) {
                        launcher.launch(
                            IntentSenderRequest.Builder(intentSender).build()
                        )
                    }
                }
            }) {
                Text(text = "Sign in with Google")
            }
        }
    }
}
