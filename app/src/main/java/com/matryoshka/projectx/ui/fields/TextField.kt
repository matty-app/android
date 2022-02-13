package com.matryoshka.projectx.ui.fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.ui.theme.LightGray

@Composable
fun TextField(
    placeholder: String,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        TextField(
            value = placeholder,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = LightGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = trailingIcon,
            onValueChange = {}
        )
    }
}