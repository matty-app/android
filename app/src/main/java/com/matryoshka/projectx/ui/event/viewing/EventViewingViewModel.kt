package com.matryoshka.projectx.ui.event.viewing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.matryoshka.projectx.data.event.Event
import com.matryoshka.projectx.data.event.EventsRepository
import com.matryoshka.projectx.exception.ProjectxException
import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.service.requireUser
import com.matryoshka.projectx.ui.common.ScreenStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventViewingViewModel @Inject constructor(
    private val eventsRepository: EventsRepository,
    private val authService: AuthService
) : ViewModel() {
    var state by mutableStateOf(EventViewingState(status = ScreenStatus.LOADING))
        private set

    suspend fun init(eventId: String) {
        try {
            val event = eventsRepository.getById(eventId)
            val user = authService.requireUser
            state = state.copy(
                event = event,
                isMine = user.id == event?.creator?.id,
                status = ScreenStatus.READY
            )
        } catch (ex: Exception) {
            state = state.copy(status = ScreenStatus.ERROR)
        }
    }
}

data class EventViewingState(
    val event: Event? = null,
    val isMine: Boolean = false,
    val status: ScreenStatus = ScreenStatus.READY,
    val error: ProjectxException? = null
) {
    val requireEvent: Event
        get() = requireNotNull(event) {
            "Event can't be null!"
        }
    val isProgressIndicatorVisible: Boolean
        get() = status == ScreenStatus.LOADING
    val isErrorToastVisible: Boolean
        get() = status == ScreenStatus.ERROR && error != null
}