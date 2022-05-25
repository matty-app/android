package com.matryoshka.projectx.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.ui.theme.LightGray

@Composable
fun TextField(
    inputField: FieldState<String>,
    placeholder: String,
    enabled: Boolean = true,
    keyBoardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    TextField(
        value = inputField.value ?: "",
        placeholder = { Text(text = placeholder) },
        singleLine = true,
        enabled = enabled,
        keyboardOptions = keyBoardOptions,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(10.dp)
            ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = LightGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        trailingIcon = trailingIcon,
        onValueChange = inputField::onChange
    )
    FieldError(inputField = inputField)
}