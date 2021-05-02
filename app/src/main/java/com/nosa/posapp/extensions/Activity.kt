package com.nosa.posapp.extensions

import android.app.Activity
import android.os.Build
import androidx.core.app.ActivityCompat

fun Activity.requestPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_PHONE_STATE
            ), 1000
        )
    }else{
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_PHONE_STATE
            ), 1000
        )
    }
}
