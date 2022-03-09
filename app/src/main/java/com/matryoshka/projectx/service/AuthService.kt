package com.matryoshka.projectx.service

import com.google.firebase.auth.FirebaseUser

interface AuthService {
    suspend fun sendSignInLinkToEmail(email: String)

    fun isSignInWithEmailLink(link: String): Boolean

    suspend fun signInByEmailLink(email: String, link: String): FirebaseUser?

    suspend fun checkEmailExists(email: String): Boolean
}