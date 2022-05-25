package com.matryoshka.projectx.ui.common.pickers

import android.widget.CalendarView
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.matryoshka.projectx.ui.theme.ProjectxTheme
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun DatePickerDialog(
    display: Boolean,
    date: LocalDate,
    onCancel: () -> Unit,
    onSubmit: (LocalDate) -> Unit
) {
    if (display) {
        var selectedDate = remember { date }

        AlertDialog(
            onDismissRequest = onCancel,
            text = {
                DatePickerView(
                    date = selectedDate,
                    onDateChangeListener = { selectedDate = it }
                )
            },
            confirmButton = {
                Button(onClick = { onSubmit(selectedDate) }) {
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
private fun DatePickerView(
    date: LocalDate,
    onDateChangeListener: (LocalDate) -> Unit
) {
    AndroidView(
        factory = { context ->
            CalendarView(context).apply {
                setOnDateChangeListener { _, year, month, dayOfMonth ->
                    onDateChangeListener(
                        LocalDate.of(year, month + 1, dayOfMonth)
                    )
                }
                minDate = System.currentTimeMillis()
            }
        },
        update = { calendarView ->
            calendarView.date = date.atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        }
    )
}

@Preview
@Composable
fun DatePickerModalView() {
    ProjectxTheme {
        DatePickerDialog(
            display = true,
            date = LocalDate.now(),
            onCancel = { },
            onSubmit = {}
        )
    }
}