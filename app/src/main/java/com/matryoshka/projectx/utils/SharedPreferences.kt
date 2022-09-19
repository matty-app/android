package com.matryoshka.projectx.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.matryoshka.projectx.SharedPrefsKey.PREF_ACCESS_TOKEN
import com.matryoshka.projectx.SharedPrefsKey.PREF_IS_NEW_USER
import com.matryoshka.projectx.SharedPrefsKey.PREF_REFRESH_TOKEN
import com.matryoshka.projectx.SharedPrefsKey.PREF_SIGN_IN_EMAIL
import com.matryoshka.projectx.SharedPrefsKey.PREF_USER_NAME

fun SharedPreferences.setUserEmail(email: String) {
    edit().putString(PREF_SIGN_IN_EMAIL, email).apply()
}

fun SharedPreferences.setUserName(name: String) {
    edit().putString(PREF_USER_NAME, name).apply()
}

fun SharedPreferences.setIsNewUser(value: Boolean) {
    edit().putBoolean(PREF_IS_NEW_USER, value).apply()
}

fun SharedPreferences.setTokens(accessToken: String, refreshToken: String) {
    edit()
        .putString(PREF_ACCESS_TOKEN, accessToken)
        .putString(PREF_REFRESH_TOKEN, refreshToken)
        .apply()
}

val Context.sharedPreferences: SharedPreferences
    get() = getSharedPreferences(packageName, MODE_PRIVATE)

val SharedPreferences.userEmail: String?
    get() = getString(PREF_SIGN_IN_EMAIL, null)

val SharedPreferences.userName: String?
    get() = getString(PREF_USER_NAME, null)

val SharedPreferences.isNewUser: Boolean
    get() = getBoolean(PREF_IS_NEW_USER, false)

val SharedPreferences.accessToken: String?
    get() = getString(PREF_ACCESS_TOKEN, null)

val SharedPreferences.refreshToken: String?
    get() = getString(PREF_REFRESH_TOKEN, null)