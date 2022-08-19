package com.matryoshka.projectx.data.auth

data class TokensInfo(
    val accessToken: String,
    val refreshToken: String
)