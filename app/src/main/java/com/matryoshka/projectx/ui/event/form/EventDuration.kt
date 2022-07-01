package com.matryoshka.projectx.ui.event.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.common.FieldState
import com.matryoshka.projectx.ui.common.ListItem
import com.matryoshka.projectx.ui.common.pickers.DatePickerDialog
import com.matryoshka.projectx.ui.common.pickers.TimePickerDialog
import com.matryoshka.projectx.ui.theme.ProjectxTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun EventDuration(
    startDateField: FieldState<LocalDateTime>,
    endDateField: FieldState<LocalDateTime>,
) {
    Column {
        DateTimeItem(
            dateField = startDateField,
            icon = {
                Icon(
                    Icons.Outlined.DateRange,
                    contentDescription = stringResource(R.string.date_time),
                    tint = MaterialTheme.colors.primary
                )
            }
        )
        DateTimeItem(dateField = endDateField)
    }
}

@Composable
private fun DateTimeItem(
    dateField: FieldState<LocalDateTime>,
    icon: (@Composable () -> Unit)? = null
) {
    val dateTime = dateField.value
    val formattedDate = remember(dateField.value) {
        dateField.value.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    }
    val formattedTime = remember(dateField.value) {
        dateField.value.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
    }
    var displayDatePickerDialog by remember {
        mutableStateOf(false)
    }
    var displayTimePickerDialog by remember {
        mutableStateOf(false)
    }

    ListItem(
        icon = icon ?: {}, //reserve space
        trailing = {
            Text(
                text = formattedTime,
                modifier = Modifier.clickable { displayTimePickerDialog = true },
            )
        }
    ) {
        Text(
            text = formattedDate,
            modifier = Modifier.clickable { displayDatePickerDialog = true },
        )
    }

    DatePickerDialog(
        display = displayDatePickerDialog,
        date = dateTime.toLocalDate(),
        onCancel = { displayDatePickerDialog = false },
        onSubmit = { selectedDate ->
            dateField.onChange(
                LocalDateTime.of(selectedDate, dateTime.toLocalTime())
            )
            displayDatePickerDialog = false
        }
    )

    TimePickerDialog(
        display = displayTimePickerDialog,
        time = dateTime.toLocalTime(),
        onCancel = { displayTimePickerDialog = false },
        onSubmit = { selectedTime ->
            dateField.onChange(
                LocalDateTime.of(dateTime.toLocalDate(), selectedTime)
            )
            displayTimePickerDialog = false
        }
    )
}

@Preview(showBackground = true)
@Composable
fun EventDateTimeItemPreview() {
    ProjectxTheme {
        EventDuration(
            startDateField = FieldState(LocalDateTime.now()),
            endDateField = FieldState(LocalDateTime.now().plusHours(12))
        )
    }
}