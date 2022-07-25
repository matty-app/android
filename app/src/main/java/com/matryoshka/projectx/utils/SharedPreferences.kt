package com.matryoshka.projectx.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

private const val PREF_SIGN_IN_EMAIL = "SIGN_IN_EMAIL"
private const val PREF_USER_NAME = "USER_NAME"
private const val PREF_IS_NEW_USER = "IS_NEW_USER"

fun SharedPreferences.setUserEmail(email: String) {
    edit().putString(PREF_SIGN_IN_EMAIL, email).apply()
}

fun SharedPreferences.setUserName(name: String) {
    edit().putString(PREF_USER_NAME, name).apply()
}

fun SharedPreferences.setIsNewUser(value: Boolean) {
    edit().putBoolean(PREF_IS_NEW_USER, value).apply()
}

val Context.sharedPreferences: SharedPreferences
    get() = getSharedPreferences(packageName, MODE_PRIVATE)

val SharedPreferences.userEmail: String?
    get() = getString(PREF_SIGN_IN_EMAIL, null)

val SharedPreferences.userName: String?
    get() = getString(PREF_USER_NAME, null)

val SharedPreferences.isNewUser: Boolean
    get() = getBoolean(PREF_IS_NEW_USER, false)
