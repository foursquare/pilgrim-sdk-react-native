@file:JvmName("RNPilgrimSdk")

package com.foursquare.pilgrimsdk.react

import android.Manifest
import android.content.Context
import android.support.v4.app.ActivityCompat
import com.facebook.react.ReactActivity
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.modules.core.PermissionListener
import com.foursquare.pilgrim.PilgrimNotificationHandler
import com.foursquare.pilgrim.PilgrimSdk
import com.foursquare.pilgrim.PilgrimSdkPlaceNotification

class RNPilgrimSdk(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName() = REACT_MODULE_NAME

    @ReactMethod
    fun requestAuthorization() {
        if (currentActivity != null && currentActivity is ReactActivity) {
            (currentActivity as ReactActivity).requestPermissions(Array(1){Manifest.permission.ACCESS_FINE_LOCATION}, 0, { requestCode, permissions, grantResults ->
                // TODO check if actually granted
                reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java).emit("AuthorizedEvent", null)
                true
            })
        }
    }

    @ReactMethod
    fun start() {
        PilgrimSdk.start(reactApplicationContext)
    }

    @ReactMethod
    fun stop() {
        PilgrimSdk.stop(reactApplicationContext)
    }

    @ReactMethod
    fun getInstallId(promise: Promise) {
        promise.resolve(PilgrimSdk.getPilgrimInstallId())
    }

    override fun getConstants(): MutableMap<String, Any> {
        return mutableMapOf(
                "AuthorizedEvent" to "AuthorizedEvent"
        )
    }

    override fun hasConstants() = true

    companion object {
        const val REACT_MODULE_NAME = "RNPilgrimSdk"

        @JvmStatic
        fun initalize(
                reactContext: Context,
                key: String,
                secret: String
        ) {
            PilgrimSdk.with(
                    PilgrimSdk.Builder(reactContext)
                            .consumer(key, secret)
                            .notificationHandler(object : PilgrimNotificationHandler() {
                                override fun handlePlaceNotification(p0: Context, p1: PilgrimSdkPlaceNotification) {

                                }
                            })
            )
        }
    }
}