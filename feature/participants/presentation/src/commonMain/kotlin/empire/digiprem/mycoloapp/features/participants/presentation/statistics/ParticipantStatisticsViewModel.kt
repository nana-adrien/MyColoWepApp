 package empire.digiprem.mycoloapp.features.participants.presentation.statistics  

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycoloapp.features.participants.presentation.statistics.ParticipantStatisticsAction
import empire.digiprem.mycoloapp.features.participants.presentation.statistics.ParticipantStatisticsEvent
import empire.digiprem.mycoloapp.features.participants.presentation.statistics.ParticipantStatisticsState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn





class ParticipantStatisticsViewModel : ViewModel() {
    private var hasLoadedInitialData = false

    private val _eventChannel = Channel<ParticipantStatisticsEvent>()
    val events = _eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(ParticipantStatisticsState())

    val state = _state.onStart {
        if (!hasLoadedInitialData) {

            hasLoadedInitialData = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ParticipantStatisticsState()
    )


    fun onAction(action: ParticipantStatisticsAction) {
        when (action) {
            ParticipantStatisticsAction.OnInitAction ->{}
            else->{}
        }
    }

}
