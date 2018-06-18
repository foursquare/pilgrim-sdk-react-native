package com.example

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.facebook.react.ReactApplication
import com.facebook.react.modules.core.DeviceEventManagerModule

class TestBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val rnApp = (context?.applicationContext as ReactApplication).reactNativeHost.reactInstanceManager.currentReactContext
        Log.d("HI", "react context $rnApp")
        val events = rnApp?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
        events?.emit("TestEvent", "Hello Test")
    }
}
