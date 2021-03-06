package com.reactlibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.uimanager.IllegalViewOperationException;

import com.facebook.react.modules.core.DeviceEventManagerModule;

import android.os.Build;
import android.util.Log;

public class ScanLibraryModule extends ReactContextBaseJavaModule {
    public static final String m_Broadcastname = "com.barcode.sendBroadcast";
    private final ReactApplicationContext reactContext;

    public ScanLibraryModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(m_Broadcastname);
        reactContext.registerReceiver(receiver, intentFilter);
        Log.d("library", Build.BRAND.toLowerCase());
    }

    @Override
    public String getName() {
        return "ScanLibrary";
    }

    void sendEvent(String code) {
      if (this.reactContext.hasActiveCatalystInstance()) {
          this.reactContext
                  .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                  .emit("onRead", code);
      }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
          if (arg1.getAction().equals(m_Broadcastname)) {
            String str = arg1.getStringExtra("BARCODE");
            if (!"".equals(str)) {
              sendEvent(str);
            }
          }
        }
    };

    @ReactMethod
    public void isCompatible( Callback errorCallback, Callback successCallback) {
        try{
          successCallback.invoke(Build.BRAND.toLowerCase().contains("itos"));
        } catch (IllegalViewOperationException e) {
            errorCallback.invoke(e.getMessage());
        }

    }

}
