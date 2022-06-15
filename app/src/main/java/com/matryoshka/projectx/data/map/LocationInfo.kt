package com.matryoshka.projectx.data.map

data class LocationInfo(
    val address: String,
    val geoPoint: GeoPoint,
    val name: String?
) {
    val displayName: String
        get() = name ?: address
}