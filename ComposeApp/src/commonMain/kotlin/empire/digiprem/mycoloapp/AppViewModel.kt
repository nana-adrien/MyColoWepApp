package empire.digiprem.mycoloapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycoloapp.core.domain.service.AppSessionManager
import empire.digiprem.mycoloapp.features.registration.presentation.RegistrationEvent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    val superbaseClient: SupabaseClient,
    val sessionManager: SessionManager,
) : ViewModel() {
    var isStarted = false
    private val _state = MutableStateFlow(AppState())
    val state = combine(
        _state,
        superbaseClient.auth.sessionStatus,
        //sessionManager.observeSession().distinctUntilChanged()
    )
    { currentState, sessionStatus ->
/*
        println("sessionStatus=${sessionStatus}")
        currentState.copy(
            isAuthenticated = sessionStatus is SessionStatus.Authenticated
        )*/
        when (sessionStatus) {
            is SessionStatus.Initializing -> {
                // Supabase est en train de restaurer, on attend
                currentState.copy(isLoadingSession = true)
            }
            is SessionStatus.Authenticated -> {
                currentState.copy(
                    isAuthenticated = true,
                    isLoadingSession = false,
                    userSession = sessionStatus.session,
                )
            }
            is SessionStatus.NotAuthenticated -> {
                currentState.copy(
                    isAuthenticated = false,
                    isLoadingSession = false,
                    userSession = null
                )
            }
            else -> {
                currentState.copy(isLoadingSession = false)
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = AppState()
        )
    private val _eventChannel = Channel<RegistrationEvent>()
    val events = _eventChannel.receiveAsFlow()


    init {
        //    observeAuthenticatedUser()
    }

}