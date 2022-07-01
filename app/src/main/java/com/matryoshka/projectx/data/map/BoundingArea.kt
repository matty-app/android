package com.matryoshka.projectx.data.map

data class BoundingArea(
    var southWest: Coordinates,
    val northEast: Coordinates
)