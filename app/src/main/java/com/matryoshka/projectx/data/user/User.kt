package com.matryoshka.projectx.data.user

import com.google.firebase.auth.FirebaseUser
import com.matryoshka.projectx.data.interest.Interest

data class User(
    val id: String,
    val name: String,
    val email: String?,
    val interests: List<Interest> = emptyList()
)

fun FirebaseUser.toProjectxUser() = User(
    id = uid,
    name = displayName ?: "",
    email = email
)