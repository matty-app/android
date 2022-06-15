package com.matryoshka.projectx.data.map

data class BoundingArea(
    var southWest: GeoPoint,
    val northEast: GeoPoint
)