package com.inces.incesclient.helpers

import android.content.Context
import android.content.SharedPreferences
import com.inces.incesclient.models.User
import com.inces.incesclient.util.Constants

object SharedPrefManager {
    private const val SHARED_PREF_NAME = "user_shared_preff"


    fun setLoginWith(context: Context,loginWith: String?, phoneNo: String?) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putString("login_with", loginWith)
        editor.putString("phone_no", phoneNo)
        editor.apply()
    }

    fun getPhoneNo(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString("phone_no",null)
    }

    fun saveData(context: Context?, location: String, data: String) {
        val sharedPreferences =
            context?.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString(location, data)
        editor?.commit()
    }

    fun getData(context: Context?, location: String): String? {
        val sharedPreferences =
            context?.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE)
        return sharedPreferences?.getString(location, null)
    }

    fun deleteData(context: Context?) {
        val sharedPreferences =
            context?.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.clear()
    }

}