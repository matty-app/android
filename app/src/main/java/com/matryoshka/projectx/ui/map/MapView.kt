package com.matryoshka.projectx.ui.map

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.matryoshka.projectx.R
import com.matryoshka.projectx.data.map.BoundingArea
import com.matryoshka.projectx.data.map.GeoPoint
import com.matryoshka.projectx.ui.theme.ProjectxTheme
import com.matryoshka.projectx.utils.toBoundingBox
import com.matryoshka.projectx.utils.toGeoPoint
import com.matryoshka.projectx.utils.toYandexPoint
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

private const val TAG = "MapView"

@Composable
fun MapView(
    mapState: MapState,
    onMapInitialized: () -> Unit = {}
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    var mapInitialized = rememberSaveable { false }

    LaunchedEffect(Unit) {
        mapState.init(
            map = mapView.map,
            markerIcon = ContextCompat.getDrawable(context, R.drawable.map_marker)?.toBitmap()
        )
        if (!mapInitialized) {
            mapInitialized = true
            onMapInitialized()
        }
    }

    YandexMapLifecycle(mapView)

    AndroidView(factory = { mapView })
}

@Composable
private fun YandexMapLifecycle(mapView: MapView) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val lifecycleObserver = remember {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    MapKitFactory.getInstance().onStart()
                    mapView.onStart()
                }
                Lifecycle.Event.ON_STOP -> {
                    mapView.onStop()
                    MapKitFactory.getInstance().onStop()
                }
                else -> {}
            }
        }
    }

    DisposableEffect(Unit) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
}

@Stable
class MapState(
    initialPosition: GeoPoint = GeoPoint(0.0, 0.0),
    initialZoom: Float = 2f,
    onLongTap: ((GeoPoint) -> Unit)? = null,
) {
    var position by mutableStateOf(initialPosition)
    private var zoom by mutableStateOf(initialZoom)

    private var markerImageProvider: ImageProvider? = null
    private val lock = Any()

    private var marker: PlacemarkMapObject? = null

    private val cameraListener = CameraListener { _, cameraPosition, reason, isEnd ->
        if (reason == CameraUpdateReason.GESTURES) {
            if (isEnd) {
                synchronized(lock) {
                    position = cameraPosition.target.toGeoPoint()
                    zoom = cameraPosition.zoom
                }
            }

        }
    }

    private val inputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) {}

        override fun onMapLongTap(map: Map, point: Point) {
            onLongTap?.invoke(point.toGeoPoint())
        }
    }

    private var map: Map? = null

    private val requireMap: Map
        get() = requireNotNull(map) {
            "Map can't be null!"
        }

    internal fun init(map: Map, markerIcon: Bitmap?) {
        synchronized(lock) {
            Log.d(TAG, "setting map to state")
            this.map = map
            map.addInputListener(inputListener)
            map.addCameraListener(cameraListener)
            markerIcon?.let {
                markerImageProvider = ImageProvider.fromBitmap(it)
            }
            //set marker to map (when re-initialized)
            marker?.let { prevMarker ->
                marker = addMarkerToMap(prevMarker.geometry)
            }
            move(position, zoom)
        }
    }

    fun toggleMarkerPosition(
        geoPoint: GeoPoint,
        boundingArea: BoundingArea,
        inCenter: Boolean = true
    ) {
        Log.d(TAG, "setMarkerPosition: $geoPoint. Center: $inCenter")
        synchronized(lock) {
            val mapObjects = requireMap.mapObjects
            val zoom = requireMap.cameraPosition(boundingArea.toBoundingBox()).zoom
            marker?.let { mapObjects.remove(it) }
            marker = addMarkerToMap(geoPoint.toYandexPoint())
            if (inCenter) {
                move(geoPoint, zoom)
            }
        }
    }

    private fun addMarkerToMap(point: Point): PlacemarkMapObject {
        val mapObjects = requireMap.mapObjects
        return markerImageProvider?.let { imageProvider ->
            mapObjects.addPlacemark(point, imageProvider)
        } ?: mapObjects.addPlacemark(point)
    }

    private fun move(geoPoint: GeoPoint, zoom: Float = this.zoom) {
        Log.d(TAG, "moving to position $geoPoint with zoom $zoom")
        this.zoom = zoom
        this.position = geoPoint
        val position = CameraPosition(
            Point(geoPoint.latitude, geoPoint.longitude),
            zoom,
            0f, //azimuth
            0f //tilt
        )
        requireMap.move(position)
    }
}

@Preview
@Composable
fun MapPreview() {
    ProjectxTheme {
        MapView(
            mapState = MapState(
                initialPosition = GeoPoint(59.945933, 30.320045),
                initialZoom = 2f
            )
        )
    }
}