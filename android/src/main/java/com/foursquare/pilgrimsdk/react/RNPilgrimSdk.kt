@file:JvmName("RNPilgrimSdk")

package com.foursquare.pilgrimsdk.react

import android.Manifest
import android.content.Context
import android.os.Build
import com.facebook.react.ReactActivity
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.foursquare.pilgrim.PilgrimNotificationHandler
import com.foursquare.pilgrim.PilgrimSdk
import com.foursquare.pilgrim.PilgrimSdkPlaceNotification

class RNPilgrimSdk(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName() = REACT_MODULE_NAME

    @ReactMethod
    fun setDebugLoggingEnabled(enabled: Boolean) {
        // TODO IS IT POSSIBLE TO UPDATE SINCE SET IN with USING BUILDER?!?
    }

    @ReactMethod
    fun requestAuthorization() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }
        if (currentActivity != null && currentActivity is ReactActivity) {
            (currentActivity as ReactActivity).requestPermissions(Array(1) { Manifest.permission.ACCESS_FINE_LOCATION }, 0, { requestCode, permissions, grantResults ->
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
    fun getDebugLogs(promise: Promise) {
//        val logs = PilgrimSdk.getLogs()
        val l = Arguments.createArray()
        var m = Arguments.createMap()
        m.putString("eventDescription", "Test log")
        m.putDouble("timestamp", 1529699471 * 1000.0)
        l.pushMap(m)
        promise.resolve(l)
    }

    @ReactMethod
    fun getInstallId(promise: Promise) {
        promise.resolve(PilgrimSdk.getPilgrimInstallId())
    }

    override fun getConstants(): MutableMap<String, Any> {
        return mutableMapOf(
                "AuthorizedEvent" to "AuthorizedEvent",
                "DidVisitEvent" to "DidVisitEvent",
                "DidBackfillVisitEvent" to "DidBackfillVisitEvent"
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
                            .logLevel(PilgrimSdk.LogLevel.DEBUG)
                            .notificationHandler(object : PilgrimNotificationHandler() {
                                override fun handlePlaceNotification(p0: Context, p1: PilgrimSdkPlaceNotification) {

                                }
                            })
            )
        }
    }
}