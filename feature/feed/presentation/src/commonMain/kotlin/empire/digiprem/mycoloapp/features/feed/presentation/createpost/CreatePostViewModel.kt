package empire.digiprem.mycoloapp.features.feed.presentation.createpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycoloapp.core.domain.util.onFailure
import empire.digiprem.mycoloapp.core.domain.util.onSuccess
import empire.digiprem.mycoloapp.features.feed.domain.usecase.CreatePostUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreatePostViewModel(
    private val createPostUseCase: CreatePostUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CreatePostState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = CreatePostState(),
    )

    private val _eventChannel = Channel<CreatePostEvent>()
    val events = _eventChannel.receiveAsFlow()

    fun onAction(action: CreatePostAction) {
        when (action) {
            is CreatePostAction.OnCaptionChange -> _state.update { it.copy(caption = action.caption, errorMessage = null) }
            is CreatePostAction.OnMediaPicked -> _state.update { it.copy(selectedFile = action.file, errorMessage = null) }
            CreatePostAction.OnRemoveMedia -> _state.update { it.copy(selectedFile = null, uploadProgress = 0f) }
            CreatePostAction.OnPublishClick -> publish()
            CreatePostAction.OnNavigateBack -> viewModelScope.launch {
                _eventChannel.send(CreatePostEvent.OnNavigateBack)
            }
        }
    }

    private fun publish() {
        viewModelScope.launch {
            _state.update { it.copy(isPublishing = true, errorMessage = null) }
            createPostUseCase(
                caption = _state.value.caption,
                mediaUrl = _state.value.selectedFile?.path,
                mediaType = _state.value.selectedMediaType,
            )
                .onSuccess {
                    _state.update { it.copy(isPublishing = false) }
                    _eventChannel.send(CreatePostEvent.OnPublished)
                }
                .onFailure {
                    _state.update { it.copy(isPublishing = false, errorMessage = "Impossible de publier. Réessayez.") }
                }
        }
    }
}
