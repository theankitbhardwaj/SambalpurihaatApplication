package com.inces.incesclient.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object UserHelper {

    fun uniqueUserId(): String {
        val uniqueKey: UUID = UUID.randomUUID()
        return uniqueKey.toString().substringBefore("-")
    }

    fun isNewProduct(date: String): Boolean {
        val prettyTime = PrettyTime(Locale.getDefault().country)
        try {
            val sdf = SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss",
                Locale.ENGLISH
            )
            val d: Date = sdf.parse(date)
            val final = prettyTime.format(d)
            Log.e("TAG", "isNewProduct: $final")
            if (!final.contains("3") and !final.contains("days"))
                return true
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }

    fun toDate(date: String): String {
        val prettyTime = PrettyTime(Locale.getDefault().country)
        try {
            val sdf = SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss",
                Locale.ENGLISH
            )
            val d: Date = sdf.parse(date)
            return prettyTime.format(d)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
    }

}