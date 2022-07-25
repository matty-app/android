package com.matryoshka.projectx.ui.map.viewing

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.matryoshka.projectx.data.map.LocationInfo
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.map.MapState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewingViewModel @Inject constructor() : ViewModel() {
    private var isInitialized = false
    var state by mutableStateOf(
        LocationViewingState(
            mapState = MapState(),
            status = ScreenStatus.LOADING
        )
    )
        private set

    fun init(location: LocationInfo) {
        if (!isInitialized) {
            state = state.copy(
                status = ScreenStatus.READY,
                location = location
            )
            isInitialized = true
        }
    }
}

@Stable
data class LocationViewingState(
    val mapState: MapState,
    val location: LocationInfo? = null,
    val status: ScreenStatus
) {
    val requireLocation: LocationInfo
        get() = requireNotNull(location) {
            "Location can't be null!"
        }
}