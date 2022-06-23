package com.matryoshka.projectx.data.map

import java.io.Serializable

data class LocationInfo(
    val name: String?,
    val address: String,
    val geoData: GeoData
) : Serializable {
    val displayName: String
        get() = name ?: address
}

data class GeoData(
    val point: GeoPoint,
    val boundingArea: BoundingArea
)