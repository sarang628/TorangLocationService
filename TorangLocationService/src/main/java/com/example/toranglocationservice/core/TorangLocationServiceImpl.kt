package com.example.toranglocationservice.core

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.torang_core.util.Logger
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task

internal class TorangLocationServiceImpl : TorangLocationService() {

    private var cancellationTokenSource = CancellationTokenSource()

    override fun onCreate() {
        super.onCreate()
        Log.v("__sarang", "TorangLocationService Created")
    }

    private var mPermissionCallback: ((Int) -> Unit)? = null
    private var mLocationCallback: ((Location) -> Unit)? = null

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(applicationContext)
    }

    override fun setOnPermissionListener(callback: (Int) -> Unit) {
        mPermissionCallback = callback
    }

    override fun setOnLocationListener(callback: (Location) -> Unit) {
        mLocationCallback = callback
    }

    override fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val currentLocationTask: Task<Location> = fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )
            Logger.v("")
            currentLocationTask.addOnCompleteListener { task: Task<Location> ->
                val result = if (task.isSuccessful && task.result != null) {
                    val result: Location = task.result
                    "Location (success): ${result.latitude}, ${result.longitude}"
                    //result.latitude = 37.5376765; result.longitude = 127.1272484 // testdata
                    mLocationCallback?.let { it.invoke(result) }
                } else {
                    val exception = task.exception
                    "Location (failure): $exception"
                }
            }
        } else {
            Logger.d("permission denied")
            mPermissionCallback?.invoke(1)
            return
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}