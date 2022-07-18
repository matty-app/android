package com.matryoshka.projectx.data.event

interface EventsRepository {

    suspend fun save(event: Event): Event

    suspend fun getById(id: String): Event?

    suspend fun getAll(futureOnly: Boolean = true): List<Event>
}