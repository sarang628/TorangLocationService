package com.example.toranglocationservice

import com.example.torang_core.util.ITorangLocationManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class TorangLocationModule {
    @Binds
    abstract fun provideLocationManager(torangLocationManager: TorangLocationManagerImpl): ITorangLocationManager
}