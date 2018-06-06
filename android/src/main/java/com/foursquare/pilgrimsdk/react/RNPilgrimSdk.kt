@file:JvmName("RNPilgrimSdk")
package com.foursquare.pilgrimsdk.react

import android.content.Context
import com.facebook.react.bridge.*
import com.foursquare.pilgrim.PilgrimSdk
import com.foursquare.pilgrim.PilgrimUserInfo
import com.foursquare.pilgrim.VisitFeedback

class RNPilgrimSdk(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

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
            )
        }
    }
}