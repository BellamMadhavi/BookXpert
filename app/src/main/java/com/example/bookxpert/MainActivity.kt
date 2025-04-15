package com.example.bookxpert

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookxpert.auth.AuthViewModel
import com.example.bookxpert.auth.AuthViewModelFactory
import com.example.bookxpert.auth.GoogleAuthUiClient
import com.example.bookxpert.firebasemessage.RequestNotificationPermission
import com.example.bookxpert.navigation.AppNavigation
import com.example.bookxpert.repository.ObjectRepository
import com.example.bookxpert.retrofit.RetrofitClient
import com.example.bookxpert.roomdb.AppDatabase
import com.example.bookxpert.ui.screens.SignInScreen
import com.example.bookxpert.viewmodel.ObjectViewModelFactory
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {

    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(GoogleAuthUiClient(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = AppDatabase.getDatabase(applicationContext)
        val dao = database.objectDao()
        val api = RetrofitClient.api
        val objectRepository = ObjectRepository(api, dao)
        val objectViewModelFactory = ObjectViewModelFactory(objectRepository)
        setContent {
            RequestNotificationPermission()
            FirebaseMessaging.getInstance().token
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("FCM_TOKEN", "Fetching FCM registration token failed", task.exception)
                        return@addOnCompleteListener
                    }

                    // Get FCM token
                    val token = task.result
                    Log.d("FCM_TOKEN", "Token: $token")
                }

            val user = viewModel.user.collectAsState().value

            if (user != null) {
                AppNavigation (
                    authViewModel = viewModel,
                    objectViewModel = viewModel (factory = objectViewModelFactory),
                    onSignOut = {
                    }
                )
            } else {
                SignInScreen(viewModel = viewModel) {
                }
            }
        }
    }
}
