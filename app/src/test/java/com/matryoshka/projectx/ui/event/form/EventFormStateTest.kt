package com.matryoshka.projectx.ui.event.form

import com.matryoshka.projectx.data.interest.Interest
import com.matryoshka.projectx.data.map.BoundingArea
import com.matryoshka.projectx.data.map.GeoData
import com.matryoshka.projectx.data.map.Coordinates
import com.matryoshka.projectx.data.map.LocationInfo
import com.matryoshka.projectx.ui.common.numValue
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EventFormStateTest {
    @Test
    fun `should clear max participants if limiting is false`() {
        val formState = EventFormState(
            maxParticipants = 10,
            limitMaxParticipants = true
        )

        formState.limitMaxParticipantsField.onChange(false)

        assertNull(formState.maxParticipantsField.numValue)
    }

    @Test
    fun `should apply initial values to fields`() {
        val location = LocationInfo(
            name = "Moscow",
            address = "Moscow, Russia",
            geoData = GeoData(
                coordinates = Coordinates(0.0, 0.0),
                BoundingArea(
                    Coordinates(0.0, 0.0), Coordinates(0.0, 0.0)
                )
            )
        )
        val interest = Interest(id = "astronomy", name = "astronomy")
        val formState = EventFormState(
            name = "title",
            summary = "summary",
            details = "details",
            public = false,
            limitMaxParticipants = true,
            maxParticipants = 12,
            location = location,
            interest = Interest(id = "astronomy", name = "astronomy")
        )

        assertEquals("title", formState.nameField.value, "Title field")
        assertEquals("summary", formState.summaryField.value, "Summary field")
        assertEquals("details", formState.detailsField.value, "Details field")
        assertEquals(false, formState.isPublicField.value, "Public field")
        assertEquals(true, formState.limitMaxParticipantsField.value, "LimitMaxParticipants field")
        assertEquals(12, formState.maxParticipantsField.numValue, "MaxParticipants field")
        assertEquals(location, formState.locationField.value, "Location field")
        assertEquals(interest, formState.interestField.value, "Interest field")
    }
}