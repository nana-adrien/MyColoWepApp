package empire.digiprem.mycolowepapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycolowepapp.feature.registration.presentation.RegistrationEvent
import empire.digiprem.mycolowepapp.feature.registration.presentation.RegistrationState
import empire.digiprem.mycolowepapp.supabase.AppSessionManager
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.ktor.client.utils.EmptyContent.status
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication.Companion.init

class AppViewModel(
    val superbaseClient: SupabaseClient,
    val sessionManager: AppSessionManager,
) : ViewModel() {
    var isStarted = false
    private val _state = MutableStateFlow(AppState())
    val state = combine(
        _state,
        superbaseClient.auth.sessionStatus,
        sessionManager.observeSession().distinctUntilChanged()
    )
    { currentState, sessionStatus, sessions ->
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

    fun observeAuthenticatedUser() {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    isAuthenticated = sessionManager.loadSessionOrNull() != null,
                )
            }
        }
    }
}