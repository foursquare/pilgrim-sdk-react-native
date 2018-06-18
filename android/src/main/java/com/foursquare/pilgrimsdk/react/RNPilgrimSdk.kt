@file:JvmName("RNPilgrimSdk")
package com.foursquare.pilgrimsdk.react

import android.content.Context
import android.util.Log
import com.facebook.react.bridge.*
import com.foursquare.pilgrim.*

class RNPilgrimSdk(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    init {
        Log.e("RNPilgrimSdk", "Initializing the pilgrim sdk module");
    }

    override fun getName() = REACT_MODULE_NAME

    @ReactMethod
    fun start() {
        PilgrimSdk.start(reactApplicationContext)
    }

    @ReactMethod
    fun stop() {
        PilgrimSdk.stop(reactApplicationContext)
    }

    @ReactMethod
    fun logout() {
        PilgrimSdk.clear(reactApplicationContext)
    }

    @ReactMethod
    fun getDebugInfo(promise: Promise) {
        promise.resolve(PilgrimSdk.getDebugInfo())
    }

    @ReactMethod
    fun getInstallId(promise: Promise) {
        promise.resolve(PilgrimSdk.getPilgrimInstallId())
    }

    @ReactMethod
    fun leaveVisitFeedback(pilgrimVisitId: String, feedback: Int, actualVenueId: String?) {
        PilgrimSdk.leaveVisitFeedback(pilgrimVisitId, VisitFeedback.values()[feedback], actualVenueId)
    }

    @ReactMethod
    fun setUserInfo(data: ReadableMap) {
    }

    @ReactMethod
    fun setOauthToken(token: String?) {
        PilgrimSdk.get().setOauthToken(token)
    }

    @ReactMethod
    fun setLogLevel(level: Int) {
        PilgrimSdk.get().setLogLevel(PilgrimSdk.LogLevel.values()[level])
    }

    override fun getConstants(): MutableMap<String, Any> {
        return mutableMapOf(
            "VISIT_FEEDBACK_CONFIRM" to VisitFeedback.CONFIRM.ordinal,
            "VISIT_FEEDBACK_DENY" to VisitFeedback.DENY.ordinal,
            "VISIT_FEEDBACK_WRONG_VENUE" to VisitFeedback.WRONG_VENUE.ordinal,
            "VISIT_FEEDBACK_FALSE_STOP" to VisitFeedback.FALSE_STOP.ordinal,
            "LOG_DEBUG" to PilgrimSdk.LogLevel.DEBUG.ordinal,
            "LOG_INFO" to PilgrimSdk.LogLevel.INFO.ordinal,
            "LOG_ERROR" to PilgrimSdk.LogLevel.ERROR.ordinal
        )
    }

    override fun hasConstants() = true


    private val notificationHandler: PilgrimNotificationHandler = object : PilgrimNotificationHandler() {
        override fun handlePlaceNotification(context: Context, notification: PilgrimSdkPlaceNotification) {

        }

        override fun handleNearbyNotification(context: Context, notification: PilgrimSdkNearbyNotification) {

        }

        override fun handleBackfillNotification(context: Context, notification: PilgrimSdkBackfillNotification) {

        }
    }

    companion object {
        const val REACT_MODULE_NAME = "RNPilgrimSdk"

        @JvmStatic
        fun initalize(
                reactContext: Context,
                key: String,
                secret: String
        ) {
            Log.e("RNPilgrimSdk", "Initializing the sdk in application onCreate")
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