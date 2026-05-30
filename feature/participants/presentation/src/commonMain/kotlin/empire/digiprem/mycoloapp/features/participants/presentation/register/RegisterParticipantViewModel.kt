 package empire.digiprem.mycoloapp.features.participants.presentation.register  

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycoloapp.features.participants.presentation.register.RegisterParticipantAction
import empire.digiprem.mycoloapp.features.participants.presentation.register.RegisterParticipantEvent
import empire.digiprem.mycoloapp.features.participants.presentation.register.RegisterParticipantState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn





class RegisterParticipantViewModel : ViewModel() {
    private var hasLoadedInitialData = false

    private val _eventChannel = Channel<RegisterParticipantEvent>()
    val events = _eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(RegisterParticipantState())

    val state = _state.onStart {
        if (!hasLoadedInitialData) {

            hasLoadedInitialData = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = RegisterParticipantState()
    )


    fun onAction(action: RegisterParticipantAction) {
        when (action) {
            RegisterParticipantAction.OnInitAction ->{}
            else->{}
        }
    }

}
