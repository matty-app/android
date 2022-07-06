package com.matryoshka.projectx.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.lang.Double.min
import java.lang.Integer.max
import kotlin.math.roundToInt

private const val PHOTO_WIDTH = 64.0
private const val PHOTO_HEIGHT = 64.0

fun compressImage(context: Context, uri: Uri): Bitmap? {
    val bitmapOptions = context.contentResolver.openInputStream(uri).use {
        BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            BitmapFactory.decodeStream(it, null, this)
            inSampleSize = max(
                1,
                min(
                    outWidth / PHOTO_WIDTH,
                    outHeight / PHOTO_HEIGHT
                ).roundToInt()
            )
            inJustDecodeBounds = false
        }
    }

    return context.contentResolver.openInputStream(uri).use {
        BitmapFactory.decodeStream(it, null, bitmapOptions)
    }
}