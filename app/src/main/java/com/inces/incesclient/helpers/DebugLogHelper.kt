package com.inces.incesclient.helpers

import android.util.Log
import com.inces.incesclient.BuildConfig

object DebugLogHelper {
    fun Log(tag: String, message: Any?) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message as String)
        }
    }
}