package com.matryoshka.projectx.ui.common

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun withAnyPermissionsGranted(permissions: Array<String>, action: () -> Unit): () -> Unit {
    val context = LocalContext.current
    val permissionsRequestLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            if (it.any { (_, value) -> value }) {
                action()
            }
        }
    )

    return {
        if (context.anyPermissionGranted(*permissions)) {
            action()
        } else {
            permissionsRequestLauncher.launch(permissions)
        }
    }
}

fun Context.anyPermissionGranted(vararg permissions: String) = permissions.any {
    ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
}