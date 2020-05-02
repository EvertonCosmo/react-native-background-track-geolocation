package com.reactnativebackgroundtrackgeolocation

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import java.util.jar.Manifest

open class LocationUtil {

companion object {
  fun hasLocationPermissions(context: Context): Boolean {
    return ActivityCompat.checkSelfPermission(context,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
  }



}


}
