package com.matryoshka.projectx.data.user

import android.graphics.Bitmap
import com.matryoshka.projectx.data.interest.Interest

data class User(
    val id: String,
    val name: String,
    val email: String?,
    val aboutMe: String? = null,
    val thumbnail: Bitmap? = null,
    val interests: List<Interest> = emptyList()
)