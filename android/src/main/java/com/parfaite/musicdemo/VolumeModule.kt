package com.parfaite.musicdemo

import android.content.Context
import android.content.IntentFilter
import android.media.AudioManager
import android.util.Log
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.facebook.react.uimanager.IllegalViewOperationException

class VolumeModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {


    private val audioManager = reactContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val volMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    private var mVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    override fun getName() = "VolumeModule"

    override fun getConstants(): MutableMap<String, Any> {
        return hashMapOf("initialVol" to mVol)
    }

    @ReactMethod
    fun increment(promise : Promise) {
        if (mVol >= volMax) {
             //promise.reject("E_COUNT", "volume reach maximum.")
        } else {
            mVol ++
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mVol, 0)
            promise.resolve(mVol)
        }
    }

    @ReactMethod
    fun decrement(promise : Promise) {
        try {
            if (mVol == 0) {
                promise.reject("E_COUNT", "volume cannot be negative.")
            } else {
                mVol --
                Log.d("DEBUG", String.format("---------- count is (%s)", mVol))
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mVol, 0);
                promise.resolve(mVol)
            }
        } catch (e: IllegalViewOperationException) {
            promise.reject("VIEW_ERROR", e.message)
        }
    }
}
