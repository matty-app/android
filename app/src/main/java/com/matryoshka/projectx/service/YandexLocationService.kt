package com.matryoshka.projectx.service

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.matryoshka.projectx.data.map.GeoData
import com.matryoshka.projectx.data.map.GeoPoint
import com.matryoshka.projectx.data.map.LocationInfo
import com.matryoshka.projectx.data.map.SuggestedLocation
import com.matryoshka.projectx.utils.toBoundingArea
import com.matryoshka.projectx.utils.toGeoPoint
import com.matryoshka.projectx.utils.toYandexPoint
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.BusinessObjectMetadata
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType.COMBINED
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.search.SuggestItem
import com.yandex.mapkit.search.SuggestOptions
import com.yandex.mapkit.search.SuggestSession
import com.yandex.mapkit.search.SuggestType
import com.yandex.mapkit.search.ToponymObjectMetadata
import com.yandex.runtime.Error
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

private const val SEARCHING_ZOOM = 18 //buildings

private const val TAG = "LocationService"

@ViewModelScoped
class YandexLocationService(
    context: Context,
    searchManagerProvider: () -> SearchManager
) {
    @Inject
    constructor(@ApplicationContext context: Context) : this(
        context,
        searchManagerProvider = {
            SearchFactory.getInstance().createSearchManager(COMBINED) //must be called in UI thread
        }
    )

    init {
        MapKitFactory.initialize(context)
    }

    private val searchManager by lazy(searchManagerProvider)

    private val suggestSession by lazy {
        searchManager.createSuggestSession()
    }

    suspend fun resolveByURI(uri: String): LocationInfo {
        return suspendCancellableCoroutine { continuation ->
            val session = searchManager.resolveURI(
                uri,
                SearchOptions(),
                createSearchListener(continuation),
            )
            continuation.invokeOnCancellation {
                session.cancel()
            }
        }
    }

    suspend fun resolveByGeoPoint(geoPoint: GeoPoint): LocationInfo {
        return suspendCancellableCoroutine { continuation ->
            val session = searchManager.submit(
                geoPoint.toYandexPoint(),
                SEARCHING_ZOOM,
                SearchOptions().setSearchTypes(SearchType.GEO.value),
                createSearchListener(continuation)
            )

            continuation.invokeOnCancellation {
                session.cancel()
            }
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun getUserLocation(context: Context): LocationInfo {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val lastKnownLocation = fusedLocationClient.lastLocation.await()
        val userGeoPoint = (lastKnownLocation ?: suspendCancellableCoroutine { continuation ->
            val locationRequest = LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    fusedLocationClient.removeLocationUpdates(this)
                    continuation.resumeWith(
                        Result.success(
                            result.lastLocation
                        )
                    )
                }
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
            continuation.invokeOnCancellation {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }).toGeoPoint()
        return resolveByGeoPoint(userGeoPoint)
    }

    suspend fun getSuggestions(locationName: String, geoPoint: GeoPoint): List<SuggestedLocation> {
        return suspendCancellableCoroutine { continuation ->
            suggestSession.suggest(
                locationName,
                BoundingBox(
                    Point(
                        geoPoint.latitude - 0.2,
                        geoPoint.longitude - 0.2
                    ),
                    Point(
                        geoPoint.latitude + 0.2,
                        geoPoint.longitude + 0.2
                    ),
                ),
                SuggestOptions().setSuggestTypes(
                    SuggestType.BIZ.value or SuggestType.GEO.value
                ),
                createSuggestListener(continuation)
            )
        }
    }

    private fun createSuggestListener(continuation: CancellableContinuation<List<SuggestedLocation>>): SuggestSession.SuggestListener {
        return object : SuggestSession.SuggestListener {
            override fun onResponse(suggestItems: MutableList<SuggestItem>) {
                Log.d(TAG, "SuggestListener::onResponse: ${suggestItems.size}")
                suggestItems
                    .filter { it.uri != null }
                    .map {
                        SuggestedLocation(
                            name = it.title.text,
                            description = it.subtitle?.text ?: "",
                            uri = it.uri!!
                        )
                    }.let {
                        continuation.resumeWith(
                            Result.success(
                                it
                            )
                        )
                    }
            }

            override fun onError(error: Error) {
                Log.e(TAG, "onSearchError: $error")
                continuation.resumeWithException(LocationServiceException())
            }
        }
    }

    private fun createSearchListener(
        continuation: CancellableContinuation<LocationInfo>
    ): Session.SearchListener {
        return object : Session.SearchListener {
            override fun onSearchResponse(response: Response) {
                val geoObject = response.collection.children.firstOrNull()?.obj!!
                val boundingArea = geoObject.boundingBox!!.toBoundingArea()
                val point = geoObject.geometry.first().point!!
                val (name, address) = resolveNameAndAddress(geoObject)
                val locationInfo = LocationInfo(
                    name = name,
                    address = address,
                    geoData = GeoData(
                        point = GeoPoint(point.latitude, point.longitude),
                        boundingArea = boundingArea
                    )
                )
                continuation.resumeWith(
                    Result.success(
                        locationInfo
                    )
                )
            }

            override fun onSearchError(error: Error) {
                Log.e(TAG, "onSearchError: $error")
                continuation.resumeWithException(LocationServiceException())
            }
        }
    }

    private fun resolveNameAndAddress(geoObject: GeoObject): Pair<String?, String> {
        val businessMetadata =
            geoObject.metadataContainer.getItem(BusinessObjectMetadata::class.java)
        if (businessMetadata != null) {
            return businessMetadata.name to businessMetadata.address.formattedAddress
        }
        val toponymMetadata =
            geoObject.metadataContainer.getItem(ToponymObjectMetadata::class.java)
        if (toponymMetadata != null) {
            return geoObject.name to toponymMetadata.address.formattedAddress
        }
        throw LocationServiceException()
    }

}

class LocationServiceException : RuntimeException()