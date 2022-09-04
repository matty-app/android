package com.matryoshka.projectx.data.auth

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String
)