package com.matryoshka.projectx.ui.event.viewing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.matryoshka.projectx.SavedStateKey
import com.matryoshka.projectx.data.event.Event
import com.matryoshka.projectx.data.event.EventsRepository
import com.matryoshka.projectx.data.user.User
import com.matryoshka.projectx.data.user.UsersRepository
import com.matryoshka.projectx.exception.AppException
import com.matryoshka.projectx.navigation.navToEventEditingScreen
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.common.authorizedAction
import com.matryoshka.projectx.utils.collectOnce
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class EventViewingViewModel @Inject constructor(
    private val eventsRepository: EventsRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {
    private var isInitialized = false
    var state by mutableStateOf(EventViewingState(status = ScreenStatus.LOADING))
        private set

    suspend fun init(eventId: String, navController: NavController) {
        if (!isInitialized) {
            try {
                val event = eventsRepository.getById(eventId)
                var user: User? = null
                authorizedAction(navController) {
                    user = usersRepository.getRequireUser()
                }
                state = state.copy(
                    event = event,
                    isMine = user?.id == event?.creator?.id,
                    status = ScreenStatus.READY
                )
                isInitialized = true
            } catch (ex: Exception) {
                state = state.copy(status = ScreenStatus.ERROR)
            }
        }
    }

    fun onEditClick(navController: NavController) {
        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
        viewModelScope.launch {
            savedStateHandle?.collectOnce(
                key = SavedStateKey.EVENT_KEY,
                initialValue = state.event
            ) { event ->
                state = state.copy(event = event)
            }
        }
        navController.navToEventEditingScreen(state.event)
    }
}

data class EventViewingState(
    val event: Event? = null,
    val isMine: Boolean = false,
    val status: ScreenStatus = ScreenStatus.READY,
    val error: AppException? = null
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