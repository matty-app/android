package com.matryoshka.projectx.utils

import android.location.Location
import com.matryoshka.projectx.data.map.BoundingArea
import com.matryoshka.projectx.data.map.Coordinates
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