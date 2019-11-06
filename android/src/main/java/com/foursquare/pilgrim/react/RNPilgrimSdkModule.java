
package com.foursquare.pilgrim.react;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

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
}