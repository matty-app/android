package com.matryoshka.projectx.data.map

import java.io.Serializable

data class LocationInfo(
    val address: String,
    val geoPoint: GeoPoint,
    val name: String?
) : Serializable {
    val displayName: String
        get() = name ?: address
}