package com.matryoshka.projectx.utils

import android.location.Location
import com.matryoshka.projectx.data.map.BoundingArea
import com.matryoshka.projectx.data.map.Coordinates
import com.matryoshka.projectx.data.map.GeoData
import com.matryoshka.projectx.data.map.LocationInfo
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point

fun BoundingBox.toBoundingArea() = BoundingArea(
    southWest = Coordinates(southWest.latitude, southWest.longitude),
    northEast = Coordinates(northEast.latitude, northEast.longitude)
)

fun BoundingArea.toBoundingBox() = BoundingBox(
    Point(southWest.latitude, southWest.longitude),
    Point(northEast.latitude, northEast.longitude)
)

fun Location.toGeoPoint() = Coordinates(latitude, longitude)

fun Point.toGeoPoint() = Coordinates(latitude, longitude)

fun Coordinates.toYandexPoint() = Point(latitude, longitude)

private const val BOUNDING_AREA_SIZE = 0.2

fun com.matryoshka.projectx.data.event.Location.toLocationInfo(): LocationInfo {
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