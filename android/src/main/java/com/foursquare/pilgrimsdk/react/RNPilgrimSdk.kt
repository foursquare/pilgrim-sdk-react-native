@file:JvmName("RNPilgrimSdk")

package com.foursquare.pilgrimsdk.react

import android.Manifest
import android.content.Context
import android.os.Build
import com.facebook.react.ReactActivity
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.foursquare.pilgrim.*


class RNPilgrimSdk(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    init {
        reactContextHack = reactContext
    }

    override fun getName() = REACT_MODULE_NAME

    @ReactMethod
    fun requestAuthorization() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            sendEvent(reactApplicationContext, AUTHORIZED_EVENT, true)
            return
        }
        if (currentActivity != null && currentActivity is ReactActivity) {
            (currentActivity as ReactActivity).requestPermissions(Array(1) { Manifest.permission.ACCESS_FINE_LOCATION }, 0, { requestCode, permissions, grantResults ->
                if (grantResults.isNotEmpty()) {
                    sendEvent(reactApplicationContext, AUTHORIZED_EVENT, grantResults[0] == 1)
                } else {
                    sendEvent(reactApplicationContext, AUTHORIZED_EVENT, true)
                }
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

    @ReactMethod
    fun testVenueVisit(isDeparture: Boolean) {
        PilgrimSdk.sendTestNotification(reactApplicationContext, Confidence.HIGH, RegionType.VENUE, isDeparture)
    }

    override fun getConstants(): MutableMap<String, Any> {
        return mutableMapOf(
                AUTHORIZED_EVENT to AUTHORIZED_EVENT,
                DID_VISIT_EVENT to DID_VISIT_EVENT,
                DID_BACKFILL_EVENT to DID_BACKFILL_EVENT
        )
    }

    override fun hasConstants() = true

    companion object {
        const val REACT_MODULE_NAME = "RNPilgrimSdk"

        const val AUTHORIZED_EVENT = "AuthorizedEvent"
        const val DID_VISIT_EVENT = "DidVisitEvent"
        const val DID_BACKFILL_EVENT = "DidBackfillVisitEvent"

        @JvmField
        var reactContextHack: ReactContext? = null

        @JvmStatic
        fun initialize(
                context: Context,
                key: String,
                secret: String
        ) {
            PilgrimSdk.with(
                    PilgrimSdk.Builder(context)
                            .consumer(key, secret)
                            .logLevel(PilgrimSdk.LogLevel.DEBUG)
                            .notificationHandler(object : PilgrimNotificationHandler() {
                                override fun handlePlaceNotification(context: Context, notification: PilgrimSdkPlaceNotification) {
                                    val venue = notification.currentPlace.venue

                                    val venueMap = WritableNativeMap()
                                    venueMap.putString("name", venue!!.name)

                                    val venueLocationMap = WritableNativeMap()
                                    venueLocationMap.putString("address", venue.location.address)
                                    venueLocationMap.putString("crossStreet", venue.location.crossStreet)
                                    venueLocationMap.putString("city", venue.location.city)
                                    venueLocationMap.putString("state", venue.location.state)
                                    venueLocationMap.putString("postalCode", venue.location.postalCode)
                                    venueLocationMap.putString("country", venue.location.country)
                                    venueLocationMap.putDouble("lat", venue.location.lat.toDouble())
                                    venueLocationMap.putDouble("lng", venue.location.lng.toDouble())
                                    venueMap.putMap("location", venueLocationMap)

                                    val visitMap = WritableNativeMap()
                                    visitMap.putString("pilgrimVisitId", notification.currentPlace.pilgrimVisitId)
                                    visitMap.putMap("venue", venueMap)
                                    visitMap.putBoolean("isArrival", !notification.currentPlace.hasExited())

                                    sendEvent(reactContextHack!!, DID_VISIT_EVENT, visitMap)
                                }
                            })
            )
        }

        private fun sendEvent(reactContext: ReactContext,
                              eventName: String,
                              params: Any) {
            if (reactContext.hasActiveCatalystInstance()) {
                reactContext
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                        .emit(eventName, params)
            }
        }
    }
}