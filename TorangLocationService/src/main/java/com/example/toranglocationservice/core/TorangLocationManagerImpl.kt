package com.example.toranglocationservice.core

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.location.Location
import android.os.IBinder
import android.util.Log
import java.lang.Exception

internal class TorangLocationManagerImpl(private val context: Context) : TorangLocationManager {
    internal lateinit var mService: TorangLocationService
    internal var mBound: Boolean = false
    private var mPermissionListener: ((Int) -> Unit)? = null
    private val pref = context.getSharedPreferences("torangLocatipn", Context.MODE_PRIVATE)

    private var mLocationListener: ((Location) -> Unit)? = object : ((Location) -> Unit) {
        override fun invoke(location: Location) {
            Log.d("__sarang", location.toString())
            mLocationListener1?.let {
                it.invoke(location)
            }
            pref.edit().putString("lat", location.latitude.toString()).commit()
            pref.edit().putString("lon", location.longitude.toString()).commit()
        }
    }

    private var mLocationListener1: ((Location) -> Unit)? = null


    companion object {
        fun create(context: Context) {
            instance = TorangLocationManagerImpl(context)
        }

        lateinit var instance: TorangLocationManager
    }

    /** Defines callbacks for service binding, passed to bindService()  */
    internal val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as TorangLocationService.TorangLocationServiceBinder
            mService = binder.getService()
            mBound = true

            mPermissionListener?.let {
                mService.setOnPermissionListener(it)
            }
            mLocationListener?.let {
                mService.setOnLocationListener(it)
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    init {
        if (!mBound) {
            TorangLocationService.startService(context, connection)
        }
    }

    override fun requestLocation() {
        if (mBound) {
            mService.requestLocation()
        } else {
            TODO("not yet implenent")
        }
    }

    override fun getLastLatitude(): Double {
        var d: Double = 0.0
        try {
            d = pref.getString("lat", "0")?.toDouble()!!
        } catch (e: Exception) {

        }
        return d
    }

    override fun getLastLongitude(): Double {
        var d: Double = 0.0
        try {
            d = pref.getString("lon", "0")?.toDouble()!!
        } catch (e: Exception) {

        }
        return d
    }

    override fun setOnPermissionListener(callback: (Int) -> Unit) {
        mPermissionListener = callback
    }

    override fun setOnLocationListener(callback: (Location) -> Unit) {
        mLocationListener1 = callback
    }
}