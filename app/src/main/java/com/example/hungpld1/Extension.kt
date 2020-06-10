package com.example.hungpld1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast

fun Activity.isDeniedPermanently(permission: String): Boolean = shouldShowRequestPermissionRationale(permission).not()

fun Context.openPermissionSettings() {
    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", packageName, null)
        startActivity(intent)
    }catch (e :Exception){
        Toast.makeText(this,e.message.toString(), Toast.LENGTH_SHORT).show()
    }
}