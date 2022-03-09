package com.matryoshka.projectx.ui.common

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

private const val PREF_SIGN_IN_EMAIL = "SIGN_IN_EMAIL"

fun Context.getSharedPreferences(): SharedPreferences = 
    getSharedPreferences(packageName, MODE_PRIVATE)

fun Context.setSignInEmail(email: String) {
    val sharedPrefs = getSharedPreferences()
    sharedPrefs.edit().putString(PREF_SIGN_IN_EMAIL, email).apply()
}

val Context.signInEmail: String?
    get() = getSharedPreferences().getString(PREF_SIGN_IN_EMAIL, null)