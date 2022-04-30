package com.example.toranglocationservice

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.toranglocationservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        TorangLocationManager.init(this)
        val torangLocationManager = TorangLocationManager.instance()

        activityMainBinding.btn.setOnClickListener {
            torangLocationManager.requestLocation()
            Log.d("__sarang", "lat:${torangLocationManager.getLastLatitude()}")
            Log.d("__sarang", "lon:${torangLocationManager.getLastLongitude()}")
        }

        torangLocationManager.setOnPermissionListener {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }
    }
}