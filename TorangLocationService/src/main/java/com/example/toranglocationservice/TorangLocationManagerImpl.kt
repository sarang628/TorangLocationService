package com.example.toranglocationservice

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.location.Location
import android.os.IBinder
import android.util.Log
import com.example.torang_core.util.ITorangLocationManager
import com.example.toranglocationservice.core.TorangLocationManager
import com.example.toranglocationservice.core.TorangLocationService
import java.lang.Exception
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TorangLocationManagerImpl @Inject constructor(@ApplicationContext private val context: Context) :
    ITorangLocationManager, TorangLocationManager {
    internal lateinit var mService: TorangLocationService
    internal var mBound: Boolean = false
    private var mPermissionListener: ((Int) -> Unit)? = null
    private val pref = context.getSharedPreferences("torangLocatipn", Context.MODE_PRIVATE)

    private var reservedRequestLocation = false

    private var mLocationListener: ((Location) -> Unit)? = object : ((Location) -> Unit) {
        override fun invoke(location: Location) {
            Log.v("__sarang", location.toString())

            pref.edit().putString("lat", location.latitude.toString()).commit()
            pref.edit().putString("lon", location.longitude.toString()).commit()

            mLocationListener1?.let {
                it.invoke(location)
            }
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
            Log.v("__sarang", "TorangLocationManager onServiceConnected")
            mService = binder.getService()
            mBound = true

            mPermissionListener?.let {
                mService.setOnPermissionListener(it)
            }
            mLocationListener?.let {
                mService.setOnLocationListener(it)
            }

            if (reservedRequestLocation)
                requestLocation()
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

    override fun init(context: Context) {

    }

    override fun requestLocation() {
        if (mBound) {
            reservedRequestLocation = false
            mService.requestLocation()
        } else {
            reservedRequestLocation = true
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