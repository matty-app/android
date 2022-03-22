package com.matryoshka.projectx.data

import com.google.firebase.auth.FirebaseUser

data class User(val uid: String, val name: String?, val email: String?)

fun FirebaseUser.toProjectxUser() = User(
    uid = uid,
    name = displayName,
    email = email
)