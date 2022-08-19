package com.matryoshka.projectx.ui.email

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matryoshka.projectx.ui.common.numberFieldState
import com.matryoshka.projectx.ui.theme.LightGray
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@Composable
fun VerificationCode(
    state: VerificationCodeState,
    onChanged: () -> Unit
) {
    val focusRequesterNum1 = remember { FocusRequester() }
    val focusRequesterNum2 = remember { FocusRequester() }
    val focusRequesterNum3 = remember { FocusRequester() }
    val focusRequesterNum4 = remember { FocusRequester() }

    LaunchedEffect(state.enteringStarted) {
        if (!state.enteringStarted) {
            focusRequesterNum1.requestFocus()
            state.onEnteringStarted()
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Number(
            value = state.number1Field.value,
            onNumberChanged = { newValue ->
                state.number1Field.onChange(newValue)
                if (newValue.isNotBlank()) focusRequesterNum2.requestFocus()
                onChanged()
            },
            onCleared = {},
            focusRequester = focusRequesterNum1
        )
        Number(
            value = state.number2Field.value,
            onNumberChanged = { newValue ->
                state.number2Field.onChange(newValue)
                if (newValue.isNotBlank()) focusRequesterNum3.requestFocus()
                onChanged()
            },
            onCleared = {
                focusRequesterNum1.requestFocus()
            },
            focusRequester = focusRequesterNum2
        )
        Number(
            value = state.number3Field.value,
            onNumberChanged = { newValue ->
                state.number3Field.onChange(newValue)
                if (newValue.isNotBlank()) focusRequesterNum4.requestFocus()
                onChanged()
            },
            onCleared = {
                focusRequesterNum2.requestFocus()
            },
            focusRequester = focusRequesterNum3
        )
        Number(
            value = state.number4Field.value,
            onNumberChanged = { newValue ->
                state.number4Field.onChange(newValue)
                if (newValue == "") focusRequesterNum3.requestFocus()
                onChanged()
            },
            onCleared = { focusRequesterNum3.requestFocus() },
            focusRequester = focusRequesterNum4
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Number(
    value: String?,
    onNumberChanged: (value: String) -> Unit,
    onCleared: () -> Unit,
    focusRequester: FocusRequester
) {
    var isUpdating = remember { false }

    val textStyle = LocalTextStyle.current.copy(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    OutlinedTextField(
        value = TextFieldValue(
            text = value ?: "",
            selection = TextRange(value?.length ?: 0)
        ),
        onValueChange = {
            val fieldValue = it.text
            val newValue = if (it.text.length > 1) {
                it.text.takeLast(1)
            } else fieldValue
            if (!isUpdating) {
                isUpdating = true
                onNumberChanged(newValue)
                isUpdating = false
            }

        },
        modifier = Modifier
            .width(56.dp)
            .onKeyEvent { keyEvent ->
                if (keyEvent.key == Key.Backspace) {
                    if (value.isNullOrBlank()) onCleared()
                }
                false
            }
            .focusRequester(focusRequester),
        textStyle = textStyle,
        placeholder = {
            Text(
                text = "_",
                modifier = Modifier.fillMaxWidth(),
                style = textStyle
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.None,
        ),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = LightGray,
            focusedIndicatorColor = MaterialTheme.colors.primary,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Transparent,
        )
    )
}

@Stable
class VerificationCodeState(
    number1: Int? = null,
    number2: Int? = null,
    number3: Int? = null,
    number4: Int? = null
) {
    val number1Field = numberFieldState(number1)
    val number2Field = numberFieldState(number2)
    val number3Field = numberFieldState(number3)
    val number4Field = numberFieldState(number4)

    var enteringStarted by mutableStateOf(false)
        private set

    val isFull: Boolean
        get() = !number1Field.value.isNullOrBlank()
                && !number2Field.value.isNullOrBlank()
                && !number3Field.value.isNullOrBlank()
                && !number4Field.value.isNullOrBlank()

    fun toInt() =
        "${number1Field.value}${number2Field.value}${number3Field.value}${number4Field.value}".toInt()

    fun clear() {
        number1Field.onChange(null)
        number2Field.onChange(null)
        number3Field.onChange(null)
        number4Field.onChange(null)
        enteringStarted = false
    }

    fun onEnteringStarted() {
        enteringStarted = true
    }
}

@Preview(showBackground = true)
@Composable
fun VerificationCodePreview() {
    ProjectxTheme {
        VerificationCode(
            state = VerificationCodeState(null, 2, 3, 4),
            onChanged = {}
        )
    }
}