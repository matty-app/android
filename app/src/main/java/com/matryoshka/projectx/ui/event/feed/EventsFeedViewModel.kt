package com.matryoshka.projectx.ui.event.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matryoshka.projectx.data.event.Event
import com.matryoshka.projectx.data.event.EventsRepository
import com.matryoshka.projectx.exception.AppException
import com.matryoshka.projectx.ui.common.ScreenStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class EventsFeedViewModel @Inject constructor(
    private val eventsRepository: EventsRepository
) : ViewModel() {

    var state by mutableStateOf(EventsFeedState(status = ScreenStatus.LOADING))
        private set

    init {
        viewModelScope.launch {
            try {
                val events = eventsRepository.getAll(futureOnly = true)
                state = state.copy(events = events, status = ScreenStatus.READY)
            } catch (ex: Exception) {
                state = state.copy(status = ScreenStatus.ERROR)
            }
        }
    }
}

data class EventsFeedState(
    val events: List<Event> = emptyList(),
    val status: ScreenStatus = ScreenStatus.READY,
    val error: AppException? = null
) {
    val enabled: Boolean
        get() = status == ScreenStatus.READY
    val isProgressIndicatorVisible: Boolean
        get() = status == ScreenStatus.LOADING
    val isErrorToastVisible: Boolean
        get() = status == ScreenStatus.ERROR && error != null
}