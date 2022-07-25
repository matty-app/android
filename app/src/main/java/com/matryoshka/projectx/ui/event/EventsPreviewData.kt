package com.matryoshka.projectx.ui.event

import com.matryoshka.projectx.data.event.Event
import com.matryoshka.projectx.data.event.Location
import com.matryoshka.projectx.data.interest.Interest
import java.time.LocalDateTime

val eventPreview = Event(
    name = "Intense training",
    summary = "Finding motivation to work out can be challenging.",
    details = "There are so many distractions and less physically demanding alternatives to spending an hour at the gym or working out at home. Even when we do get to the gym or start a home workout, having the desire to work hard and push ourselves is another challenge.",
    interest = Interest(id = "sport", name = "Sport", emoji = "🏋"),
    public = true,
    maxParticipants = 20,
    location = Location("Forest gym", null, null),
    startDate = LocalDateTime.now().plusDays(1).plusHours(2),
    endDate = LocalDateTime.now().plusDays(1).plusHours(3).plusMinutes(30),
    withApproval = true
)

val eventListPreview = listOf(
    eventPreview.copy(
        name = "3D printing",
        summary = "3D printing is the construction of a three-dimensional object from a CAD model.",
        interest = Interest(id = "science", name = "Science", emoji = "🧑‍🔬"),
        startDate = LocalDateTime.now().plusMinutes(37),
        endDate = LocalDateTime.now().plusHours(2).plusMinutes(37),
    ),
    eventPreview,
    eventPreview.copy(
        name = "Lev Nikolayevich Tolstoy",
        summary = "Let's speak about famous russian writer.",
        interest = Interest(id = "books", name = "Books", emoji = "📚"),
        startDate = LocalDateTime.now().plusDays(6).plusHours(2),
        endDate = LocalDateTime.now().plusDays(6).plusHours(5),
    )
)