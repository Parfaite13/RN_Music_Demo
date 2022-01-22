package com.parfaite.musicdemo

import android.media.MediaPlayer
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule


class MDModule(private val reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext),
    LifecycleEventListener {

    private var mMediaPlayer = MediaPlayer()
    private var initialized = false
    private var playUri:String? = null

   init {
       reactContext.addLifecycleEventListener(this);
   }

    override fun getName() = "MDModule"

    override fun getConstants(): MutableMap<String, Any> {
        return hashMapOf("initial" to "Play")
    }

    override fun onHostResume() {
        //TODO("Not yet implemented")
    }

    override fun onHostPause() {
        //TODO("Not yet implemented")
    }

    override fun onHostDestroy() {
        println("onHostDestroy")
        mMediaPlayer.release()
    }

    @ReactMethod
    fun playOrPause(uri: String, promise : Promise) {
        println("MDModule + $uri")
        try {
            if (!uri.equals(playUri)) initialized = false
            if (!initialized) prepare(uri)
            println("playOrPause state ${mMediaPlayer.isPlaying}")
            if (mMediaPlayer.isPlaying) {
                mMediaPlayer.pause()
                promise.resolve("Play")
            } else {
                mMediaPlayer.start()
                promise.resolve("Pause")
            }
            println("playOrPause state 1 ${mMediaPlayer.isPlaying}")
        } catch (e : Exception) {
            promise.reject("E_ERROR", e)
        }

    }

    @ReactMethod
    fun reset() {
        mMediaPlayer.reset()
        initialized = false
        val params = Arguments.createMap()
        params.putString("title", "Replay")
        sendEvent(this.reactApplicationContext, "onComplete", params)
    }

    private fun prepare(filePath: String) {
        println("MDModule prepare $filePath")
        playUri = filePath
        mMediaPlayer.reset()
        mMediaPlayer.setDataSource(filePath)
        mMediaPlayer.setOnCompletionListener {
            println("OnCompletionListener")
            reset()
        }
        mMediaPlayer.prepare()
        initialized = true
    }

    private fun sendEvent(
        reactContext: ReactContext,
        eventName: String,
        params: WritableMap?
    ) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
    }
}
