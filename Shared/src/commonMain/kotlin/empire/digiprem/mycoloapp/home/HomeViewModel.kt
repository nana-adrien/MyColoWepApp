package empire.digiprem.mycoloapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycoloapp.home.HomeAction
import empire.digiprem.mycoloapp.home.HomeEvent
import empire.digiprem.mycoloapp.home.HomeState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn


class HomeViewModel : ViewModel() {
    private var hasLoadedInitialData = false

    private val _eventChannel = Channel<HomeEvent>()
    val events = _eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(HomeState())

    val state = _state.onStart {
        if (!hasLoadedInitialData) {

            hasLoadedInitialData = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeState()
    )


    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnInitAction -> {}
            else -> {}
        }
    }

}
