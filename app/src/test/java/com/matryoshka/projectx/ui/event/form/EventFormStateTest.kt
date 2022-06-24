package com.matryoshka.projectx.ui.event.form

import com.matryoshka.projectx.data.Interest
import com.matryoshka.projectx.data.map.BoundingArea
import com.matryoshka.projectx.data.map.GeoData
import com.matryoshka.projectx.data.map.GeoPoint
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

        formState.limitMaxParticipants.onChange(false)

        assertNull(formState.maxParticipants.numValue)
    }

    @Test
    fun `should apply initial values to fields`() {
        val location = LocationInfo(
            name = "Moscow",
            address = "Moscow, Russia",
            geoData = GeoData(
                point = GeoPoint(0.0, 0.0),
                BoundingArea(
                    GeoPoint(0.0, 0.0), GeoPoint(0.0, 0.0)
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

        assertEquals("title", formState.name.value, "Title field")
        assertEquals("summary", formState.summary.value, "Summary field")
        assertEquals("details", formState.details.value, "Details field")
        assertEquals(false, formState.public.value, "Public field")
        assertEquals(true, formState.limitMaxParticipants.value, "LimitMaxParticipants field")
        assertEquals(12, formState.maxParticipants.numValue, "MaxParticipants field")
        assertEquals(location, formState.location.value, "Location field")
        assertEquals(interest, formState.interest.value, "Interest field")
    }
}