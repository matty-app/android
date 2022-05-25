package com.matryoshka.projectx.ui.common.pickers

import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.matryoshka.projectx.ui.theme.ProjectxTheme
import java.time.LocalTime

@Composable
fun TimePickerDialog(
    display: Boolean,
    time: LocalTime,
    onCancel: () -> Unit,
    onSubmit: (LocalTime) -> Unit
) {
    if (display) {
        var selectedTime = remember {
            time
        }

        AlertDialog(
            onDismissRequest = onCancel,
            text = {
                TimePickerView(
                    time,
                    onTimeChangeListener = { selectedTime = it }
                )
            },
            confirmButton = {
                Button(onClick = { onSubmit(selectedTime) }) {
                    Text(text = "Set", color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = onCancel) {
                    Text(text = "Cancel", color = Color.White)
                }
            }
        )
    }

}

@Composable
private fun TimePickerView(
    time: LocalTime,
    onTimeChangeListener: (LocalTime) -> Unit
) {
    AndroidView(
        factory = { context ->
            TimePicker(context).apply {
                setIs24HourView(DateFormat.is24HourFormat(context))
                setOnTimeChangedListener { _, hourOfDay, minute ->
                    onTimeChangeListener(
                        LocalTime.of(hourOfDay, minute)
                    )
                }
            }
        },
        update = { timePicker ->
            timePicker.hour = time.hour
            timePicker.minute = time.minute
        }
    )
}

@Preview
@Composable
fun TimePickerModalView() {
    ProjectxTheme {
        TimePickerDialog(
            display = true,
            time = LocalTime.now(),
            onSubmit = { },
            onCancel = {}
        )
    }
}