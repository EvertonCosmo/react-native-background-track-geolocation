package com.reactnativebackgroundtrackgeolocation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.*


class BackgroundLocationService :Service() {

    private final val TAG:Class<BackgroundLocationService> = BackgroundLocationService::class.java;
    private final val PRIMARY_CHANNEL = "Location Service Track"
    private var mBinder = LocationBinder()
    private lateinit var locationRequest : LocationRequest;
    private lateinit var locationCallback: LocationCallback;
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient;

  init{}

  override fun onCreate() {
    Log.i(TAG.toString(), "onCreate");
    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    createLocationCallBack();
  }

  override fun onBind(intent: Intent?): IBinder? {
   return mBinder;
  }
  @RequiresApi(Build.VERSION_CODES.O)
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    Log.d(TAG.toString(),"onStartCommand: called")
    super.onStartCommand(intent, flags, startId);
    startForeground(333,getNotification())
    locationUpdates();
    return START_STICKY;
  }

  open fun locationUpdates(){
    locationRequest  = LocationRequest().apply { setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);setFastestInterval(5*1000);setInterval(4*1000) }
    fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
  }
  private fun createLocationCallBack() {
    locationCallback = object :LocationCallback(){
      override fun onLocationResult(result: LocationResult?) {
        if(result == null) {
          Log.d("onLocationResult: " ,"error in location")
          return;
        }
        Log.d("location:","Lat :" + result.lastLocation.latitude + "," + "Lng : " + result.lastLocation.latitude);
        val locations : List<Location> =result.locations;

        super.onLocationResult(result)

      }
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun getNotification(): Notification? {
    val notificationChannel = NotificationChannel(PRIMARY_CHANNEL,"Location",NotificationManager.IMPORTANCE_HIGH)
    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    manager.createNotificationChannel(notificationChannel)

    val notificationBuilder : NotificationCompat.Builder = NotificationCompat.Builder(this,PRIMARY_CHANNEL)
      .setAutoCancel(true)
      .setContentTitle("Background Location Tracking")
      .setContentText("Location service is running in the background.")
     return notificationBuilder.build()
  }
  class LocationBinder : Binder() {
    fun getService(): BackgroundLocationService {
      return BackgroundLocationService();
    }
  }

}

