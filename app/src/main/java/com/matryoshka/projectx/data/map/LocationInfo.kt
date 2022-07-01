package com.matryoshka.projectx.data.map

import java.io.Serializable

data class LocationInfo(
    val address: String,
    val geoData: GeoData,
    val name: String?
) : Serializable {
    val displayName: String
        get() = name ?: address
}

data class GeoData(
    val coordinates: Coordinates,
    val boundingArea: BoundingArea? = null
)