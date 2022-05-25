package com.matryoshka.projectx.ui.event.form

import com.matryoshka.projectx.ui.common.numValue
import kotlin.test.Test
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
        val formState = EventFormState(
            name = "title",
            summary = "summary",
            details = "details",
            public = false,
            limitMaxParticipants = true,
            maxParticipants = 12,
            location = "moon",
            interest = "astronomy"
        )

        assertEquals("title", formState.name.value, "Title field")
        assertEquals("summary", formState.summary.value, "Summary field")
        assertEquals("details", formState.details.value, "Details field")
        assertEquals(false, formState.public.value, "Public field")
        assertEquals(true, formState.limitMaxParticipants.value, "LimitMaxParticipants field")
        assertEquals(12, formState.maxParticipants.numValue, "MaxParticipants field")
        assertEquals("moon", formState.location.value, "Location fields")
        assertEquals("astronomy", formState.interest.value, "Interest field")
    }
}