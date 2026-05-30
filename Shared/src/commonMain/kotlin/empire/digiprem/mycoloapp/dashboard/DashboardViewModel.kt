package empire.digiprem.mycoloapp.dashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycoloapp.core.domain.model.UserRole
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


class DashboardViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val userRole: UserRole = UserRole.fromString(savedStateHandle["user_role"])
    private var hasLoadedInitialData = false

    private val _eventChannel = Channel<DashboardEvent>()
    val events = _eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(
        DashboardState(
            userRole = userRole,
        )
    )

    val state = _state.onStart {
        if (!hasLoadedInitialData) {

            hasLoadedInitialData = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = DashboardState()
    )


    fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.OnInitAction -> {}
           is DashboardAction.OnSectionChange->{
                _state.update {
                    it.copy(
                        currentSection = action.section,
                    )
                }
            }
            else -> {}
        }
    }

}
