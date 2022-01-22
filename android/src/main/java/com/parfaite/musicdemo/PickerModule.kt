package com.parfaite.musicdemo

import android.content.ContentUris
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import com.facebook.common.file.FileUtils
import com.facebook.react.bridge.*
import org.json.JSONException
import org.json.JSONObject
import com.facebook.react.bridge.WritableNativeMap
import com.facebook.react.bridge.WritableMap


class PickerModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName() = "PickerModule"

    private val audioList = mutableListOf<Audio>()
    // Container for information about each audio.
    data class Audio(
        val name : String,
        val path: String,
        val duration: Int,
        val size: Int
    ) {
        fun toJSON(): String? {
            val jsonObject = JSONObject()
            return try {
                jsonObject.put("path", path)
                jsonObject.put("name", name)
                jsonObject.put("duration", duration)
                jsonObject.put("size", size)
                jsonObject.toString()
            } catch (e: JSONException) {
                e.printStackTrace()
                ""
            }
        }
    }

    override fun getConstants(): MutableMap<String, Any> {
        return hashMapOf("initialVol" to 0)
    }

    @ReactMethod
    fun getFileList(promise : Promise){
        val collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DATA,
        )

        // Display Audios in alphabetical order based on their display name.
        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"
        val query = reactContext.contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )
        if (query == null) promise.reject("ERROR", "No audio exist.")
        var a : Audio? = null
        val map = WritableNativeArray()
        query?.use{ cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            println("getFileList count=" + cursor.count)
            while (cursor.moveToNext()) {
                // Get values of columns for a given Audio.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)
                val data = cursor.getString(dataColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                // Stores column values and the contentUri in a local object
                // that represents the media file.

                val audio = Audio(name, data, duration, size)

                val content: WritableMap = WritableNativeMap()
                content.putString("name", audio.name)
                content.putString("path", audio.path)
                content.putInt("duration", audio.duration)
                content.putInt("size", audio.size)

                if (a == null) a = audio
                map.pushMap(content)
                audioList += audio
            }
        }

        promise.resolve(map)
//        a?.let {
//            println("play ${it.name}  ,path= ${it.path}")
//
//            val mp = MediaPlayer()
//            mp.setDataSource(reactContext, Uri.parse(it.path))
//            mp.prepare()
//            mp.start()
//        }

    }
}
