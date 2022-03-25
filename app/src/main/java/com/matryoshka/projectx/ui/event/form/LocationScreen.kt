package com.matryoshka.projectx.ui.event.form

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.common.sharedPreferences

@Composable
fun LocationScreen(
    state: LocationUiState,
    onInit: (SharedPreferences) -> Unit,
    onSubmit: (SharedPreferences) -> Unit,
    onLocationChange: (String) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        onInit(context.sharedPreferences)
    }

    Column {
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
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = { onSubmit(context.sharedPreferences) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Submit")
        }
    }
}