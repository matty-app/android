package com.matryoshka.projectx.utils

import android.location.Location
import com.matryoshka.projectx.data.map.BoundingArea
import com.matryoshka.projectx.data.map.GeoPoint
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point

fun BoundingBox.toBoundingArea() = BoundingArea(
    southWest = GeoPoint(southWest.latitude, southWest.longitude),
    northEast = GeoPoint(northEast.latitude, northEast.longitude)
)

fun BoundingArea.toBoundingBox() = BoundingBox(
    Point(southWest.latitude, southWest.longitude),
    Point(northEast.latitude, northEast.longitude)
)

fun Location.toGeoPoint() = GeoPoint(latitude, longitude)