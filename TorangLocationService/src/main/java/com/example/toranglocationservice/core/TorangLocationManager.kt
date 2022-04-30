package com.example.toranglocationservice.core

import android.content.Context
import android.location.Location

interface TorangLocationManager {

    /**
     * 위치를 업데이트 합니다. 업데이트 된 정보는 저장됩니다.
     */
    fun requestLocation()

    /**
     * 마지막 저장된 위도
     */
    fun getLastLatitude() : Double

    /**
     * 마지막 저장된 경도
     */
    fun getLastLongitude() : Double

    /**
     * 권한체크했는지 확인 리스너 등록
     */
    fun setOnPermissionListener(callback: (Int) -> Unit)

    /**
     * 내 위치를 받았을 때 콜백 리스너 등록
     */
    fun setOnLocationListener(callback: (Location) -> Unit)

    companion object {
        /**
         * 사용전에 최초에 초기화를 해주세요.
         * @param context
         */
        fun init(context: Context) {
            TorangLocationManagerImpl.create(context)
        }

        fun instance(): TorangLocationManager {
            return TorangLocationManagerImpl.instance
        }
    }
}