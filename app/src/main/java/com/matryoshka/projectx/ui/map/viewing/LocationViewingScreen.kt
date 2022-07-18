package com.matryoshka.projectx.ui.map.viewing

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.map.MapScreenTopBar
import com.matryoshka.projectx.ui.map.MapView

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LocationViewingScreen(
    state: LocationViewingState,
    onCancel: () -> Unit
) {
    Scaffold(
        topBar = {
            MapScreenTopBar(onBackClick = onCancel)
        }
    ) {
        if (state.status == ScreenStatus.READY) {
            MapView(
                mapState = state.mapState,
                onInit = { state.mapState.setMarker(state.requireLocation.geoData) }
            )
        }
    }
}