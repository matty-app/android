package com.matryoshka.projectx.ui.event

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.matryoshka.projectx.R
import com.matryoshka.projectx.navigation.LocalNavController
import com.matryoshka.projectx.ui.common.pickers.DataPickerScreen

@Composable
fun LocationChangeScreen(
    state: LocationChangeState,
    onLocationChange: (String) -> Unit,
    onSubmit: (NavController) -> Unit,
    onCancel: (NavController) -> Unit
) {
    val navController = LocalNavController.current

    DataPickerScreen(
        title = "Set location",
        onBackClicked = { onCancel(navController) },
        onSubmit = { onSubmit(navController) }
    ) {
        OutlinedTextField(
            value = state.location,
            onValueChange = onLocationChange,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    Icons.Outlined.LocationOn,
                    contentDescription = stringResource(R.string.location)
                )
            },
            label = {
                Text(text = "Location")
            }
        )
    }
}