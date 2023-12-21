package com.example.caloriestracker.ui.auth.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    companion object {
        const val PREF_NAME = "com.example.caloriestracker.session"

        const val KEY_USER_ID = "user_id"
        const val KEY_USER_EMAIL = "user_email"
        const val KEY_USERNAME = "username"
        const val KEY_PHONE = "phone"
        const val KEY_BB_TARGET = "bb_target"
        const val KEY_BB = "bb"
        const val KEY_TB = "tb"
        const val KEY_ROLE = "role"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(KEY_USERNAME, null)
    }

    fun getPhone(): String? {
        return sharedPreferences.getString(KEY_PHONE, null)
    }

    fun getBbTarget(): String? {
        return sharedPreferences.getString(KEY_BB_TARGET, null)
    }

    fun getBb(): String? {
        return sharedPreferences.getString(KEY_BB, null)
    }

    fun getTb(): String? {
        return sharedPreferences.getString(KEY_TB, null)
    }

    fun getRole(): String? {
        return sharedPreferences.getString(KEY_ROLE, null)
    }

    fun saveUserInfo(
        userId: String,
        userEmail: String,
        username: String,
        phone: String,
        bbTarget: String,
        bb: String,
        tb: String,
        role: String
    )
    {
        editor.putString(KEY_USER_ID, userId)
        editor.putString(KEY_USER_EMAIL, userEmail)
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_PHONE, phone)
        editor.putString(KEY_BB_TARGET, bbTarget)
        editor.putString(KEY_BB, bb)
        editor.putString(KEY_TB, tb)
        editor.putString(KEY_ROLE, role)
        editor.apply()
    }

    fun clearSession() {
        editor.clear().apply()
    }
}
