package com.matryoshka.projectx.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    colors: ListItemColors = OneLine.colors(),
    text: @Composable () -> Unit
) {
    OneLine.ListItem(
        text = text,
        modifier = modifier,
        icon = icon,
        trailing = trailing,
        colors = colors
    )
}

private object OneLine {

    private val MIN_HEIGHT = 48.dp
    private val MIN_HEIGHT_WITH_ICON = 56.dp

    private val ICON_MIN_WIDTH = 40.dp
    private val ICON_VERTICAL_PADDING = 8.dp
    private val ICON_LEFT_PADDING = 8.dp

    private val CONTENT_HORIZONTAL_PADDING = 16.dp

    private val TRAILING_MIN_WIDTH = 24.dp
    private val TRAILING_MIN_HEIGHT = 24.dp
    private val TRAILING_PADDING = 16.dp

    @Composable
    fun colors(
        leadingIcon: Color = MaterialTheme.colors.onSurface.copy(TextFieldDefaults.IconOpacity),
        trailingIcon: Color = MaterialTheme.colors.onSurface.copy(TextFieldDefaults.IconOpacity)
    ): ListItemColors = ListItemColors(
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    )

    @Composable
    fun ListItem(
        text: @Composable () -> Unit,
        modifier: Modifier = Modifier,
        icon: (@Composable () -> Unit)? = null,
        trailing: (@Composable () -> Unit)? = null,
        colors: ListItemColors = colors()
    ) {
        val minHeight = if (icon == null) MIN_HEIGHT else MIN_HEIGHT_WITH_ICON

        Row(
            modifier
                .heightIn(min = minHeight)
        ) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .widthIn(min = ICON_MIN_WIDTH)
                        .padding(
                            top = ICON_VERTICAL_PADDING,
                            bottom = ICON_VERTICAL_PADDING,
                            start = ICON_LEFT_PADDING,
                        ),
                    contentAlignment = Alignment.Center

                ) {
                    CompositionLocalProvider(
                        LocalContentColor provides colors.leadingIcon,
                        LocalContentAlpha provides colors.leadingIcon.alpha
                    ) {
                        icon()
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = CONTENT_HORIZONTAL_PADDING)
                    .align(Alignment.CenterVertically),
                contentAlignment = Alignment.CenterStart
            ) {
                ProvideTextStyle(value = MaterialTheme.typography.subtitle1) {
                    text()
                }
            }
            if (trailing != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .widthIn(min = TRAILING_MIN_WIDTH)
                        .heightIn(min = TRAILING_MIN_HEIGHT)
                        .padding(end = TRAILING_PADDING),
                    contentAlignment = Alignment.Center

                ) {
                    CompositionLocalProvider(
                        LocalContentColor provides colors.trailingIcon,
                        LocalContentAlpha provides colors.trailingIcon.alpha
                    ) {
                        trailing()
                    }
                }
            }
        }
    }
}

@Stable
data class ListItemColors(
    val leadingIcon: Color,
    val trailingIcon: Color
)

@Preview(showBackground = true)
@Composable
fun OneLineListItemPreview() {
    ProjectxTheme {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            ListItem(
                text = {
                    Text(text = "Category")
                },
                modifier = Modifier
                    .fillMaxWidth(),
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Category,
                        contentDescription = "Category"
                    )
                },
                trailing = {
                    Icon(
                        imageVector = Icons.Outlined.ArrowForwardIos,
                        contentDescription = "Arrow Forward"
                    )

                }
            )
            ListItem(
                text = {
                    Text(text = "Location")
                },
                modifier = Modifier
                    .fillMaxWidth(),
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Location"
                    )
                }
            )
            ListItem(
                text = {
                    Text(text = "I love Android development")
                },
                modifier = Modifier
                    .fillMaxWidth(),
                trailing = {
                    Switch(
                        checked = true,
                        onCheckedChange = { }
                    )
                }
            )
        }
    }
}
