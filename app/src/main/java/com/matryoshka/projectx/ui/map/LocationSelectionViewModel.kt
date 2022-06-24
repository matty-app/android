package com.matryoshka.projectx.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.matryoshka.projectx.SavedStateKey.LOCATION_KEY
import com.matryoshka.projectx.data.map.GeoPoint
import com.matryoshka.projectx.data.map.LocationInfo
import com.matryoshka.projectx.data.map.SuggestedLocation
import com.matryoshka.projectx.service.YandexLocationService
import com.matryoshka.projectx.ui.common.FieldState
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.common.SourceType
import com.matryoshka.projectx.ui.common.SourceType.APPLICATION
import com.matryoshka.projectx.ui.common.SourceType.USER
import com.matryoshka.projectx.ui.common.anyPermissionGranted
import com.matryoshka.projectx.ui.common.textFieldState
import com.matryoshka.projectx.utils.debounce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

private const val TAG = "LocationSelectionVM"

@HiltViewModel
class LocationSelectionViewModel @Inject constructor(
    private val locationService: YandexLocationService
) : ViewModel() {
    var state by mutableStateOf(
        LocationChangeState(
            status = ScreenStatus.LOADING,
            searchField = textFieldState(onChange = ::onSearchFieldChange),
            mapState = MapState(
                onTap = ::onMapTapped
            ),
        )
    )
        private set

    fun init(location: LocationInfo?) {
        state = state.copy(
            status = ScreenStatus.READY,
            location = location
        )
        location?.let {
            state.searchField.onChange(it.displayName, APPLICATION)
        }
    }

    fun onSubmit(navController: NavController) {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set(LOCATION_KEY, state.location)
        navController.popBackStack()
    }

    fun onCancel(navController: NavController) {
        navController.popBackStack()
    }

    fun onSuggestionClick(suggestion: SuggestedLocation) {
        viewModelScope.launch {
            val location = locationService.resolveByURI(suggestion.uri)
            state.mapState.setMarker(location.geoData, inCenter = true)
            state = state.copy(
                location = location,
                suggestions = emptyList()
            )
            state.searchField.onChange(location.displayName, APPLICATION)
        }
    }

    fun onCancelingSearch() {
        val fieldValue = state.location?.displayName ?: ""
        state.searchField.onChange(fieldValue, APPLICATION)
        state = state.copy(suggestions = emptyList())
    }

    @SuppressLint("MissingPermission")
    fun displayUserLocation(context: Context) {
        if (
            context.anyPermissionGranted(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            viewModelScope.launch {
                val location = locationService.getUserLocation(context)
                state.mapState.setMarker(location.geoData)
                updateLocationState(location)
            }
        }
    }

    private fun onMapTapped(geoPoint: GeoPoint) {
        state.mapState.setMarker(geoPoint, inCenter = false)
        viewModelScope.launch {
            delay(2000L)
            val location = locationService.resolveByGeoPoint(geoPoint)
            val geoData = location.geoData.copy(point = geoPoint) //keep users selection
            updateLocationState(location.copy(geoData = geoData))
        }
    }

    private fun updateLocationState(location: LocationInfo) {
        state = state.copy(location = location)
        state.searchField.onChange(location.displayName, APPLICATION)
    }

    private fun onSearchFieldChange(
        prevValue: String,
        newValue: String,
        sourceType: SourceType
    ): Boolean {
        val shouldUpdate = prevValue != newValue
        if (shouldUpdate && sourceType == USER) {
            loadSuggestions(newValue)
        }
        return shouldUpdate
    }

    private val loadSuggestions: (String) -> Unit =
        viewModelScope.debounce(800.milliseconds) { locationName ->
            Log.d(TAG, "updateSuggestions: query '$locationName'")
            if (locationName.isEmpty()) {
                state = state.copy(
                    suggestions = emptyList()
                )
                return@debounce
            }
            val suggestions = locationService.getSuggestions(locationName, state.mapState.position)
            state = state.copy(suggestions = suggestions)
        }
}

@Stable
data class LocationChangeState(
    val status: ScreenStatus,
    val mapState: MapState,
    val searchField: FieldState<String>,
    val suggestions: List<SuggestedLocation> = emptyList(),
    val location: LocationInfo? = null,
)