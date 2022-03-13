package com.matryoshka.projectx.ui.common

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

private const val PREF_SIGN_IN_EMAIL = "SIGN_IN_EMAIL"
private const val PREF_USER_NAME = "USER_NAME"
private const val PREF_IS_NEW_USER = "IS_NEW_USER"

fun Context.setUserEmail(email: String) {
    sharedPreferences.edit().putString(PREF_SIGN_IN_EMAIL, email).apply()
}

fun Context.setUserName(name: String) {
    sharedPreferences.edit().putString(PREF_USER_NAME, name).apply()
}

fun Context.setIsNewUser(value: Boolean) {
    sharedPreferences.edit().putBoolean(PREF_IS_NEW_USER, value).apply()
}

val Context.sharedPreferences: SharedPreferences
    get() = getSharedPreferences(packageName, MODE_PRIVATE)

val Context.userEmail: String?
    get() = sharedPreferences.getString(PREF_SIGN_IN_EMAIL, null)

val Context.userName: String?
    get() = sharedPreferences.getString(PREF_USER_NAME, null)

val Context.isNewUser: Boolean
    get() = sharedPreferences.getBoolean(PREF_IS_NEW_USER, false)
