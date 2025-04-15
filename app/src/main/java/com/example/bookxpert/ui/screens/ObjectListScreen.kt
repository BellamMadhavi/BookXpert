package com.example.bookxpert.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.bookxpert.viewmodel.ObjectViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items




@Composable
fun ObjectListScreen(viewModel: ObjectViewModel) {
    val objects by viewModel.objectList.observeAsState(emptyList())
    val context = LocalContext.current
    val error by viewModel.error.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchObjects()
    }

    error?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        viewModel.clearError()
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(objects) { obj ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = obj.name, style = MaterialTheme.typography.titleMedium)
                    Text(text = obj.data, style = MaterialTheme.typography.bodySmall)
                    Row (modifier = Modifier.padding(top = 8.dp)) {
                        Button (onClick = {
                            viewModel.update(obj.copy(name = obj.name + " (Updated)"))
                        }) {
                            Text("Update")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { viewModel.delete(obj) }) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}
