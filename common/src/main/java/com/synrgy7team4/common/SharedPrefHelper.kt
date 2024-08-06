package com.synrgy7team4.common

import android.content.Context
import android.content.SharedPreferences

class SharedPrefHelper(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun getJwtToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    fun saveJwtToken(token: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("jwt_token", token)
        editor.apply()
    }
}
