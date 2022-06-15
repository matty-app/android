package com.matryoshka.projectx.ui.map

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.matryoshka.projectx.data.map.BoundingArea
import com.matryoshka.projectx.data.map.GeoPoint
import com.matryoshka.projectx.ui.theme.ProjectxTheme
import com.matryoshka.projectx.utils.toBoundingBox
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView

private const val TAG = "MapView"

@Composable
fun MapView(
    mapState: MapState,
    onMapInitialized: () -> Unit = {}
) {

    LaunchedEffect(mapState.isInitialized) {
        if (mapState.isInitialized) {
            onMapInitialized()
        }
    }

    YandexMapView(mapState)
}

@Composable
private fun YandexMapView(
    mapState: MapState
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    LaunchedEffect(Unit) {
        mapState.setMap(mapView.map)
    }

    YandexMapLifecycle(mapView)

    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        AndroidView(factory = { mapView })
        if (mapState.isInitialized) {
            SearchingMarker(isSearching = mapState.isMoving)
        }
    }
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
    onPositionChanged: (GeoPoint, Float) -> Unit = { _, _ -> }
) {
    var isMoving by mutableStateOf(false)
        private set

    var isInitialized by mutableStateOf(false)

    var zoom by mutableStateOf(initialZoom)

    var geoPoint by mutableStateOf(initialPosition)
        private set

    private val lock = Any()

    private val cameraListener = CameraListener { _, position, reason, isEnd ->
        if (reason == CameraUpdateReason.GESTURES) {
            isMoving = !isEnd
            if (isEnd) {
                synchronized(lock) {
                    zoom = position.zoom
                    geoPoint = GeoPoint(position.target.latitude, position.target.longitude)
                    onPositionChanged(geoPoint, zoom)
                }
            }
        }
    }

    private var map: Map? = null

    private val requireMap: Map
        get() = requireNotNull(map) {
            "Map can't be null!"
        }

    internal fun setMap(map: Map) {
        synchronized(lock) {
            isInitialized = false
            Log.d(TAG, "setting map to state")
            map.addCameraListener(cameraListener)
            this.map = map
            move(geoPoint, zoom)
            isInitialized = true
        }
    }

    fun move(geoPoint: GeoPoint, boundingBox: BoundingArea) {
        val zoom = requireMap.cameraPosition(boundingBox.toBoundingBox()).zoom
        move(geoPoint, zoom)
    }

    private fun move(geoPoint: GeoPoint, zoom: Float = this.zoom) {
        synchronized(lock) {
            Log.d(TAG, "moving to position $geoPoint with zoom $zoom")
            isMoving = true
            val position = CameraPosition(
                Point(geoPoint.latitude, geoPoint.longitude),
                zoom,
                0f, //azimuth
                0f //tilt
            )
            requireMap.move(position)
            this.geoPoint = geoPoint
            this.zoom = zoom
            isMoving = false
        }
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