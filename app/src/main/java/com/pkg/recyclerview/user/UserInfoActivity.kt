package com.pkg.recyclerview.user

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.pkg.recyclerview.R
import com.pkg.recyclerview.network.Api
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@RequiresApi(Build.VERSION_CODES.M)
class UserInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        findViewById<Button>(R.id.take_picture_button).setOnClickListener {
            launchCameraWithPermission()
        }
    }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { accepted ->
            if (accepted) {
                launchCameraWithPermission()
            } else {
                showExplanation()
            }
        }

    private fun launchAppSettings() {
        // Cet intent permet d'ouvrir les paramètres de l'app (pour modifier les permissions déjà refusées par ex)
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", this.packageName, null)
        )
        // ici pas besoin de vérifier avant car on vise un écran système:
        startActivity(intent)
    }

    private fun launchCameraWithPermission() {
        val camPermission = Manifest.permission.CAMERA
        val permissionStatus = checkSelfPermission(camPermission)
        val isAlreadyAccepted = permissionStatus == PackageManager.PERMISSION_GRANTED
        val isExplanationNeeded = shouldShowRequestPermissionRationale(camPermission)
        when {
            isAlreadyAccepted -> {
                launchCamera()
            }
            isExplanationNeeded -> {
                showExplanation()
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun showExplanation() {
        // ici on construit une pop-up système (Dialog) pour expliquer la nécessité de la demande de permission
        AlertDialog.Builder(this)
            .setMessage("🥺 On a besoin de la caméra, vraiment! 👉👈")
            .setPositiveButton("Bon, ok") { _, _ -> launchAppSettings() }
            .setNegativeButton("Nope") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun handleImage(imageUri: Uri) {
        val converted = convert(imageUri);
        lifecycleScope.launch {
            Api.userWebService.updateAvatar(converted);
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        val tmpFile = File.createTempFile("avatar", "jpeg")
        tmpFile.outputStream().use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        handleImage(tmpFile.toUri())
    }

    private fun launchCamera() {
        cameraLauncher.launch(null)
    }

    private fun convert(uri: Uri): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = this.contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
        )
    }

}