package com.example.bookxpert.ui.components

import android.net.Uri
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun PDFWebView(pdfUrl: String) {
    val encodedUrl = Uri.encode(pdfUrl)
    val fullUrl = "https://docs.google.com/gview?embedded=true&url=$encodedUrl"

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                loadUrl(fullUrl)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp), // Adjust height as needed
        update = { webView ->
            webView.loadUrl(fullUrl)
        }
    )
}
