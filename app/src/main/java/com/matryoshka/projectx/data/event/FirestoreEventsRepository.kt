package com.matryoshka.projectx.data.event

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.matryoshka.projectx.data.interest.FIRESTORE_INTERESTS
import com.matryoshka.projectx.data.interest.FirestoreInterestsRepository
import com.matryoshka.projectx.data.map.Coordinates
import com.matryoshka.projectx.data.user.FIRESTORE_USERS
import com.matryoshka.projectx.data.user.UsersRepository
import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.service.requireUser
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

const val FIRESTORE_EVENTS = "events"

class FirestoreEventsRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val authService: AuthService,
    private val interestsRepository: FirestoreInterestsRepository,
    private val usersRepository: UsersRepository
) : EventsRepository {

    override suspend fun save(event: Event): Event {
        val document = db.collection(FIRESTORE_EVENTS).document()
        val eventToSave = if (event.isNew) event.prepareNewEvent(document.id) else event.copy()
        val eventFs = eventToSave.toFirestore()
        document
            .set(eventFs, SetOptions.merge())
            .await()
        return eventToSave
    }

    override suspend fun getAll(futureOnly: Boolean): List<Event> {
        return db.collection(FIRESTORE_EVENTS).run {
            if (futureOnly) {
                whereGreaterThanOrEqualTo("startDate", Timestamp.now())
            } else this
        }
            .orderBy("startDate")
            .get()
            .await()
            .map { it.toObject<EventFs>().toDomain() }
    }

    private fun Event.prepareNewEvent(uid: String): Event {
        val currentUserRef = UserRef(
            id = authService.requireUser.id,
            name = authService.requireUser.name
        )
        return copy(
            id = uid,
            creator = currentUserRef,
            participants = listOf(currentUserRef)
        )
    }

    private fun Event.toFirestore() = EventFs(
        name = name,
        summary = summary,
        details = details,
        public = public,
        withApproval = withApproval,
        maxParticipants = maxParticipants,
        location = location.toFirestore(),
        startDate = startDate.toTimestamp(),
        endDate = endDate.toTimestamp(),
        interestRef = db.collection(FIRESTORE_INTERESTS).document(interest.id),
        creatorRef = db.collection(FIRESTORE_USERS).document(creator!!.id),
        participants = participants.map {
            db.collection(FIRESTORE_USERS).document(it.id)
        },
        uid = id!!
    )

    private suspend fun EventFs.toDomain(): Event {
        val interest = interestsRepository.getById(interestRef?.id ?: "")!!
        val creatorRef = usersRepository.getById(creatorRef?.id ?: "", flat = true)!!.let {
            UserRef(it.id, it.name)
        }
        val participantIds = participants.map { it.id }
        val participants = usersRepository.getByIds(participantIds).map {
            UserRef(it.id, it.name)
        }
        return Event(
            name = name,
            summary = summary,
            details = details,
            public = public,
            withApproval = withApproval,
            maxParticipants = maxParticipants,
            location = location.toDomain(),
            startDate = startDate.toLocalDateTime(),
            endDate = endDate.toLocalDateTime(),
            interest = interest,
            creator = creatorRef,
            participants = participants
        )
    }
}

//firebase requires default values
private data class EventFs(
    val name: String = "",
    val summary: String= "",
    val details: String= "",
    val public: Boolean = false,
    val maxParticipants: Int? = null,
    val location: LocationFs = LocationFs(null, null, null),
    val startDate: Timestamp = Timestamp.now(),
    val endDate: Timestamp = Timestamp.now(),
    val withApproval: Boolean = false,
    val interestRef: DocumentReference? = null,
    val participants: List<DocumentReference> = emptyList(),
    val creatorRef: DocumentReference? = null,
    @DocumentId
    val uid: String = ""
)

private class LocationFs(
    val name: String? = null,
    val address: String? = null,
    val coordinates: GeoPoint? = null
)

private fun LocationFs.toDomain() = Location(
    name = name,
    address = address,
    coordinates = coordinates?.let { Coordinates(it.latitude, it.longitude) }
)

private fun Location.toFirestore() = LocationFs(
    name = name,
    address = address,
    coordinates = coordinates?.let { GeoPoint(it.latitude, it.longitude) }
)

private fun LocalDateTime.toTimestamp() = atZone(ZoneId.systemDefault())
    .toInstant()
    .epochSecond
    .let { seconds ->
        Timestamp(
            seconds,
            0 // nanoseconds
        )
    }

private fun Timestamp.toLocalDateTime() = LocalDateTime.ofInstant(
    toDate().toInstant(),
    ZoneId.systemDefault()
)