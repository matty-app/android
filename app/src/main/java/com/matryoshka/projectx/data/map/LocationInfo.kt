package com.matryoshka.projectx.data.map

import com.matryoshka.projectx.data.event.Location
import java.io.Serializable

private const val BOUNDING_AREA_SIZE = 0.2

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

fun Location.toLocationInfo(): LocationInfo {
    val boundingArea = BoundingArea(
        southWest = Coordinates(
            latitude = coordinates!!.latitude - BOUNDING_AREA_SIZE,
            longitude = coordinates.longitude - BOUNDING_AREA_SIZE
        ),
        northEast = Coordinates(
            latitude = coordinates.latitude + BOUNDING_AREA_SIZE,
            longitude = coordinates.longitude + BOUNDING_AREA_SIZE
        )
    )
    return LocationInfo(
        address = address!!,
        geoData = GeoData(
            coordinates = coordinates,
            boundingArea = boundingArea
        ),
        name = name
    )
}