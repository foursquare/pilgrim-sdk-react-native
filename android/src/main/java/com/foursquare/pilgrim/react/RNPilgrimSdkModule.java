
package com.foursquare.pilgrim.react;

import android.content.Intent;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.foursquare.pilgrim.CurrentLocation;
import com.foursquare.pilgrim.PilgrimNotificationTester;
import com.foursquare.pilgrim.PilgrimSdk;
import com.foursquare.pilgrim.Result;
import com.foursquare.pilgrimsdk.debugging.PilgrimSdkDebugActivity;

public class RNPilgrimSdkModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNPilgrimSdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNPilgrimSdk";
    }

    @ReactMethod
    public void getInstallId(Promise promise) {
        promise.resolve(PilgrimSdk.getInstallId());
    }

    @ReactMethod
    public void start(Promise promise) {
        PilgrimSdk.start(reactContext);
    }

    @ReactMethod
    public void stop(Promise promise) {
        PilgrimSdk.stop(reactContext);
    }

    @ReactMethod
    public void getCurrentLocation(final Promise promise) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Result<CurrentLocation, Exception> currentLocationResult = PilgrimSdk.get().getCurrentLocation();
                if (currentLocationResult.isOk()) {
                    CurrentLocation currentLocation = currentLocationResult.getResult();
                    promise.resolve(Utils.currentLocationJson(currentLocation));
                } else {
                    promise.reject("E_GET_CURRENT_LOCATION", currentLocationResult.getErr());
                }
            }
        }).start();
    }

    @ReactMethod
    public void fireTestVisit(double latitude, double longitude) {
        PilgrimNotificationTester.sendTestVisitArrivalAtLocation(reactContext, latitude, longitude, false);
    }

    @ReactMethod
    public void showDebugScreen() {
        Intent intent = new Intent(reactContext, PilgrimSdkDebugActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        reactContext.startActivity(intent);
    }
}