package com.matryoshka.projectx.ui.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.ui.theme.LightGray

@Composable
fun TextField(
    fieldState: FieldState<String>,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    enabled: Boolean = true,
    keyBoardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    trailingIcon: @Composable (() -> Unit)? = null,
    maxLines: Int = Int.MAX_VALUE
) {
    TextField(
        value = TextFieldValue(
            text = fieldState.value,
            selection = TextRange(fieldState.value.length)
        ),
        placeholder = { Text(text = placeholder ?: "") },
        singleLine = maxLines == 1,
        enabled = enabled,
        keyboardOptions = keyBoardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier
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
        maxLines = maxLines,
        onValueChange = { textFieldValue -> fieldState.onChange(textFieldValue.text) },
    )
    FieldError(inputField = fieldState)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OutlinedTextFieldSm(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = if (label != null) {
            modifier.padding(top = 8.dp)
        } else {
            modifier
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        maxLines = 3,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = remember { MutableInteractionSource() },
                singleLine = true,
                placeholder = placeholder,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                trailingIcon = trailingIcon,
                label = label,
            )
        }
    )
}