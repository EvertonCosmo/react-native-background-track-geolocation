package com.reactnativebackgroundtrackgeolocation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.modules.permissions.PermissionsModule
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult


class BackgroundTrackGeolocationModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
  private lateinit var locationCallback: LocationCallback;
  private val mContext: ReactApplicationContext;
  private var backgroundService: BackgroundLocationService? = null;

  override fun getName(): String {
    return "BackgroundTrackGeolocation"
  }


  init {
    mContext = reactContext;
    onLocationChanged()

  }

  override fun initialize() {
    super.initialize()
  }


    val serviceConnection: ServiceConnection = object : ServiceConnection {
      override fun onServiceConnected(name: ComponentName, service: IBinder?): Unit {
        val name: String = name.className;
        if (name.startsWith("BackgroundLocationService")) {
          backgroundService = BackgroundLocationService.LocationBinder().getService()
          backgroundService!!.locationUpdates()
        }
      }

      override fun onServiceDisconnected(name: ComponentName) {
        val name: String = name.className;
        if (name.startsWith("BackgroundLocationService")) {
          backgroundService = null
        }
      }
    }


  fun onLocationChanged() {

    locationCallback = object : LocationCallback() {
      override fun onLocationResult(result: LocationResult?) {
        super.onLocationResult(result)
        if (result != null) {
          Log.d("location:", "Lat in foreground:" + result.lastLocation.latitude + "," + "Lng in foreground: " + result.lastLocation.latitude)
        };
        val params: WritableMap = Arguments.createMap()
        if (result != null) {
          params.putString("latitude:", result.lastLocation.latitude.toString())
        }
        if (result != null) {
          params.putString("longitude:", result.lastLocation.longitude.toString())
        }
        sendLocationEvent("updateLocation", params)
      }
    }
  }

  private fun sendLocationEvent(name: String, params: WritableMap) {
    mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit(name, params)
  }

//  private fun checkPermissions() {
//    val perms: PermissionsModule = reactApplicationContext.getNativeModule(PermissionsModule::class.java)
//  }


  @ReactMethod
  fun startTracking() {
    if (LocationUtil.hasLocationPermissions(mContext)) {
      val intent: Intent = Intent(mContext, BackgroundLocationService::class.java);


        Toast.makeText(mContext, "Service  started", Toast.LENGTH_SHORT).show()

      mContext.bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE)
      
    } else {
      Toast.makeText(mContext, "Service Stopped", Toast.LENGTH_SHORT).show()
    }
  }

  @ReactMethod
  fun stopTracking() {
    if (backgroundService != null) {


      if (LocationUtil.hasLocationPermissions(mContext)) {
        val intent: Intent = Intent(mContext, BackgroundLocationService::class.java);
        mContext.stopService(intent)
        Toast.makeText(mContext, "Service stopped", Toast.LENGTH_SHORT).show()
      }

    }
  }
}
