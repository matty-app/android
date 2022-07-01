package com.matryoshka.projectx.data.interest

import java.io.Serializable

data class Interest(
    val id: String,
    val name: String,
    val emoji: String? = null
) : Serializable