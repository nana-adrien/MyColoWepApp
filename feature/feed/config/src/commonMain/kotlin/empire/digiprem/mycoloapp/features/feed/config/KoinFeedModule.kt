package empire.digiprem.mycoloapp.features.feed.config

import empire.digiprem.mycoloapp.features.feed.data.repository.FakeFeedRepository
import empire.digiprem.mycoloapp.features.feed.domain.repository.IFeedRepository
import empire.digiprem.mycoloapp.features.feed.domain.usecase.AddCommentUseCase
import empire.digiprem.mycoloapp.features.feed.domain.usecase.CreatePostUseCase
import empire.digiprem.mycoloapp.features.feed.domain.usecase.GetCommentsUseCase
import empire.digiprem.mycoloapp.features.feed.domain.usecase.GetFeedUseCase
import empire.digiprem.mycoloapp.features.feed.presentation.createpost.CreatePostViewModel
import empire.digiprem.mycoloapp.features.feed.presentation.feed.FeedViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val feedModule = module {
    singleOf(::FakeFeedRepository) bind IFeedRepository::class
    singleOf(::GetFeedUseCase)
    singleOf(::CreatePostUseCase)
    singleOf(::GetCommentsUseCase)
    singleOf(::AddCommentUseCase)
    viewModelOf(::FeedViewModel)
    viewModelOf(::CreatePostViewModel)
}
