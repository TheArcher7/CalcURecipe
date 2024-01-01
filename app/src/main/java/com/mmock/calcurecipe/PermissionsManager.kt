package com.mmock.calcurecipe

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private const val MEDIA_PERMISSION_CODE = 101

object PermissionsManager {

    fun requestAllPermissions(context: Context, activity: Activity, tag: String){

    }

    fun requestReadMediaPermission(context: Context, activity: Activity, tag: String){
        // Permissions for storage access
        // code for API 33+
        var permission = Manifest.permission.READ_MEDIA_IMAGES
        if(ContextCompat.checkSelfPermission(context, permission)
            != PackageManager.PERMISSION_GRANTED){
            //ask for permission
            Log.d(tag, "Storage permissions not granted. Requesting permissions. (code 1)")
            ActivityCompat.requestPermissions(activity, arrayOf(permission) , MEDIA_PERMISSION_CODE)
        } else {
            Log.d(tag, "Storage permissions granted. (code 1)")
            //Toast.makeText(context, "Accessing photos from gallery.", Toast.LENGTH_SHORT).show()
        }

        // code for legacy systems
        // This code is copied from the android documentation. It asks for permissions in a similar way to the first
        permission = Manifest.permission.READ_EXTERNAL_STORAGE
        when {
            ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                //Code goes here
                Log.d(tag, "Storage permissions granted. (code 2)")
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity, permission) -> {
                // In an educational UI, explain to the user why your app requires this permission
            }
            else -> {
                // You can directly ask for the permission.
                Log.d(tag, "Storage permissions not granted. Requesting permissions. (code 2)")
                ActivityCompat.requestPermissions(activity,
                    arrayOf(permission),
                    MEDIA_PERMISSION_CODE)
            }
        }
    }
}