package com.example.bookxpert.ui.screens
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.bookxpert.auth.AuthViewModel
import java.io.File
import android.media.ExifInterface
import android.graphics.BitmapFactory
import androidx.activity.result.PickVisualMediaRequest
import com.example.bookxpert.ui.components.PDFWebView

fun createImageFile(context: Context): Uri {
    val file = File.createTempFile("captured", ".jpg", context.externalCacheDir)
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}

fun cropToSquare(bitmap: Bitmap): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val newEdge = minOf(width, height)

    val xOffset = (width - newEdge) / 2
    val yOffset = (height - newEdge) / 2

    return Bitmap.createBitmap(bitmap, xOffset, yOffset, newEdge, newEdge)
}

fun getRotatedBitmap(context: Context, imageUri: Uri): Bitmap? {
    val inputStream = context.contentResolver.openInputStream(imageUri) ?: return null
    val bitmap = BitmapFactory.decodeStream(inputStream)
    inputStream.close()

    val exifInputStream = context.contentResolver.openInputStream(imageUri) ?: return bitmap
    val exif = ExifInterface(exifInputStream)
    val orientation = exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )
    exifInputStream.close()

    val rotationDegrees = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
        else -> 0f
    }

    return if (rotationDegrees != 0f) {
        val matrix = Matrix().apply { postRotate(rotationDegrees) }
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } else {
        bitmap
    }
}

@Composable
fun HomeScreen(viewModel: AuthViewModel, onSignOut: () -> Unit,onNavigateToObjectList: () -> Unit
) {
    var showPdf by remember { mutableStateOf(false) }
    val pdfUrl = "https://fssservices.bookxpert.co/GeneratedPDF/Companies/nadc/2024-2025/BalanceSheet.pdf"
    val user = viewModel.user.collectAsState()
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    // ✅ First: Declare the camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = photoUri
        }
    }

    // ✅ Then: Declare the permission launcher that uses `cameraLauncher`
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            photoUri = createImageFile(context)
            photoUri?.let { cameraLauncher.launch(it) }
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome, ${user.value?.displayName ?: "User"}")
        Spacer(modifier = Modifier.height(16.dp))

        imageUri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val bitmap = getRotatedBitmap(context, it)
            bitmap?.let { bmp ->
                val squareBitmap = cropToSquare(bmp)
                Image(
                    bitmap = squareBitmap.asImageBitmap(),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .size(150.dp) // Resize image to fit
                        .clip(CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }) {
            Text("Select from Gallery")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            } else {
                photoUri = createImageFile(context)
                photoUri?.let { cameraLauncher.launch(it) }
            }
        }) {
            Text("Capture Image")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            showPdf = !showPdf
        }) {
            Text(if (showPdf) "Hide PDF" else "Show PDF")
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (showPdf) {
            PDFWebView(pdfUrl)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onNavigateToObjectList){
            Text("objectlist")
        }


        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            viewModel.signOut()
            onSignOut()
        }) {
            Text("Sign Out")
        }
    }
}
