package com.matryoshka.projectx.ui.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.matryoshka.projectx.data.map.SuggestedLocation

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SuggestionItem(
    location: SuggestedLocation,
    onClick: (SuggestedLocation) -> Unit
) {
    androidx.compose.material.ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick(location) }),
        text = {
            Text(text = location.name)
        },
        secondaryText = {
            Text(text = location.description)
        },
        icon = {
            Icon(Icons.Outlined.LocationOn, contentDescription = "")
        }
    )
}