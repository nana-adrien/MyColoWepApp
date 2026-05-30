 package empire.digiprem.mycoloapp.features.participants.presentation.detail  

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycoloapp.features.participants.presentation.detail.ParticipantItemDetailsAction
import empire.digiprem.mycoloapp.features.participants.presentation.detail.ParticipantItemDetailsEvent
import empire.digiprem.mycoloapp.features.participants.presentation.detail.ParticipantItemDetailsState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn





class ParticipantItemDetailsViewModel : ViewModel() {
    private var hasLoadedInitialData = false

    private val _eventChannel = Channel<ParticipantItemDetailsEvent>()
    val events = _eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(ParticipantItemDetailsState())

    val state = _state.onStart {
        if (!hasLoadedInitialData) {

            hasLoadedInitialData = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ParticipantItemDetailsState()
    )


    fun onAction(action: ParticipantItemDetailsAction) {
        when (action) {
            ParticipantItemDetailsAction.OnInitAction ->{}
            else->{}
        }
    }

}
