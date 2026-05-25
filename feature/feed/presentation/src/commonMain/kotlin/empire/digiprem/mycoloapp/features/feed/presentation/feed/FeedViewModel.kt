package empire.digiprem.mycoloapp.features.feed.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycoloapp.core.domain.util.UiText
import empire.digiprem.mycoloapp.core.domain.util.onFailure
import empire.digiprem.mycoloapp.core.domain.util.onSuccess
import empire.digiprem.mycoloapp.features.feed.domain.repository.IFeedRepository
import empire.digiprem.mycoloapp.features.feed.domain.usecase.AddCommentUseCase
import empire.digiprem.mycoloapp.features.feed.domain.usecase.GetCommentsUseCase
import empire.digiprem.mycoloapp.features.feed.domain.usecase.GetFeedUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedViewModel(
    private val getFeedUseCase: GetFeedUseCase,
    private val feedRepository: IFeedRepository,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val addCommentUseCase: AddCommentUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(FeedState())
    val state = combine(_state, getFeedUseCase()) { state, posts ->
        state.copy(posts = posts)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = FeedState(),
    )

    private val _eventChannel = Channel<FeedEvent>()
    val events = _eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    init {
        loadFeed()
    }

    fun onAction(action: FeedAction) {
        when (action) {
            FeedAction.OnRefresh -> loadFeed(isRefresh = true)
            is FeedAction.OnLikeClick -> toggleLike(action.postId, action.isLiked)
            is FeedAction.OnProfileClick -> viewModelScope.launch {
                _eventChannel.send(FeedEvent.NavigateToProfile(action.userId))
            }
            is FeedAction.OnCommentClick -> loadComments(action.postId)
            FeedAction.OnDismissComments -> _state.update {
                it.copy(selectedPostId = null, comments = emptyList(), commentInput = "")
            }
            is FeedAction.OnCommentInputChange -> _state.update { it.copy(commentInput = action.text) }
            FeedAction.OnAddComment -> addComment()
        }
    }

    private fun loadFeed(isRefresh: Boolean = false) {
        if (hasLoadedInitialData && !isRefresh) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = !isRefresh, isRefreshing = isRefresh) }
            feedRepository.fetchPosts()
                .onFailure {
                    _state.update { s ->
                        s.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = UiText.DynamicString("Impossible de charger le feed."),
                        )
                    }
                }
            _state.update { it.copy(isLoading = false, isRefreshing = false) }
            hasLoadedInitialData = true
        }
    }

    private fun toggleLike(postId: String, isCurrentlyLiked: Boolean) {
        viewModelScope.launch {
            if (isCurrentlyLiked) feedRepository.unlikePost(postId)
            else feedRepository.likePost(postId)
            feedRepository.fetchPosts()
        }
    }

    private fun loadComments(postId: String) {
        viewModelScope.launch {
            _state.update { it.copy(selectedPostId = postId, isCommentsLoading = true, comments = emptyList()) }
            getCommentsUseCase(postId)
                .onSuccess { comments -> _state.update { it.copy(comments = comments, isCommentsLoading = false) } }
                .onFailure { _state.update { it.copy(isCommentsLoading = false) } }
        }
    }

    private fun addComment() {
        val postId = _state.value.selectedPostId ?: return
        val content = _state.value.commentInput.trim()
        if (content.isBlank()) return
        viewModelScope.launch {
            _state.update { it.copy(isAddingComment = true, commentInput = "") }
            addCommentUseCase(postId, content)
                .onSuccess { comment ->
                    _state.update { it.copy(comments = it.comments + comment, isAddingComment = false) }
                    feedRepository.fetchPosts()
                }
                .onFailure { _state.update { it.copy(isAddingComment = false, commentInput = content) } }
        }
    }
}
