@file:JvmName("RNPilgrimSdk")
package com.foursquare.pilgrimsdk.react

import android.content.Context
import android.util.Log
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.annotations.ReactProp
import com.foursquare.pilgrim.PilgrimSdk
import com.foursquare.pilgrim.PilgrimUserInfo

class RNPilgrimSdk(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    init {
        Log.e("RNPilgrimSdk", "In the constructor of the SDK Module")
    }

    override fun getName() = "RNPilgrimSdk"

    @ReactMethod
    fun start() {
        PilgrimSdk.start(reactApplicationContext)
    }

    @ReactMethod
    fun setUserInfo(data: ReadableMap) {
//        PilgrimSdk.get().setUserInfo()
    }

    @ReactMethod
    fun setLogLevel() {
        PilgrimSdk.get().setLogLevel(PilgrimSdk.LogLevel.DEBUG)
    }

    @ReactMethod
    fun getInstallId(promise: Promise) {
        Log.e("RNPilgrimSdk", "Getting install id ${PilgrimSdk.getPilgrimInstallId()}")
        promise.resolve(PilgrimSdk.getPilgrimInstallId())
    }

    companion object {
        @ReactMethod
        @JvmStatic
        fun initalize(
                reactContext: Context,
                key: String,
                secret: String
        ) {
            PilgrimSdk.with(
                    PilgrimSdk.Builder(reactContext)
                            .consumer(key, secret)
            )
        }
    }
}