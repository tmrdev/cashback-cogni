package org.timreynolds.cogni.utils

import android.util.Log

/**
 * Logger
 */
class Logger {

    companion object {
        private val LOG_TAG: String = "KOTLIN_SAMPLE"
        private val DEBUG = Log.isLoggable(LOG_TAG, Log.DEBUG)

        fun i(tag: String, msg: String) {
            Log.i(tag, msg)
        }

        fun d(tag: String, msg: String) {
            if (DEBUG) {
                Log.v(tag, msg)
            }
        }

        fun e(tag: String, msg: String) {
            Log.e(tag, msg)
        }

        fun e(tag: String, msg: String, e: Exception) {
            Log.e(tag, msg + "\nException: " + e.message)
            e.printStackTrace()
        }

    }

}