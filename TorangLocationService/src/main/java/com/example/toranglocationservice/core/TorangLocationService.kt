package com.example.toranglocationservice.core

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.Binder
import android.util.Log

internal abstract class TorangLocationService : Service() {
    internal val binder = TorangLocationServiceBinder()

    /**
     * 서비스를 사용 시 위치권한이 허용되었는지 판단하는 리스너입니다.
     */
    abstract fun setOnPermissionListener(callback: (Int) -> Unit)

    /**
     * 서비스에서 위치정보를 얻었을때 호출되는 콜백 리스너입니다.
     */
    abstract fun setOnLocationListener(callback: (Location) -> Unit)

    /**
     * 위치를 요청하는 함수입니다.
     */
    abstract fun requestLocation()

    companion object {
        /**
         * 위치 서비스를 실행을 요청하는 함수입니다.
         */
        fun startService(context: Context, connection: ServiceConnection) {
            Log.v("__sarang", "start TorangLocationService")
            Intent(context, TorangLocationServiceImpl::class.java).also { intent ->
                context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
            }
        }
    }

    inner class TorangLocationServiceBinder : Binder() {
        fun getService(): TorangLocationService = this@TorangLocationService
    }
}