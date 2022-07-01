package com.matryoshka.projectx.ui.event.form

import androidx.compose.runtime.Stable
import com.matryoshka.projectx.data.interest.Interest
import com.matryoshka.projectx.data.map.LocationInfo
import com.matryoshka.projectx.ui.common.FieldState
import com.matryoshka.projectx.ui.common.numberFieldState
import com.matryoshka.projectx.ui.common.switchState
import com.matryoshka.projectx.ui.common.textFieldState
import java.time.LocalDateTime

private const val STARTING_DELAY_MIN = 5L
private const val DEFAULT_DURATION_HR = 3L

@Stable
class EventFormState(
    name: String = "",
    summary: String = "",
    details: String = "",
    public: Boolean = true,
    withApproval: Boolean = false,
    limitMaxParticipants: Boolean = false,
    maxParticipants: Int? = null,
    location: LocationInfo? = null,
    interest: Interest? = null,
    startDate: LocalDateTime = LocalDateTime.now().plusMinutes(STARTING_DELAY_MIN),
    endDate: LocalDateTime = startDate.plusHours(DEFAULT_DURATION_HR),
) {
    val nameField = textFieldState(name)
    val summaryField = textFieldState(summary)
    val detailsField = textFieldState(details)
    val isPublicField = switchState(public)
    val withApprovalField = switchState(withApproval)
    val limitMaxParticipantsField = switchState(
        checked = limitMaxParticipants,
        onChange = { _, newValue, _ ->
            if (!newValue) {
                this.maxParticipantsField.onChange("")
            }
            true
        }
    )
    val maxParticipantsField = numberFieldState(maxParticipants)
    val startDateField = FieldState(startDate)
    val endDateField = FieldState(endDate)
    val interestField = FieldState(interest)
    val locationField = FieldState(location)

}