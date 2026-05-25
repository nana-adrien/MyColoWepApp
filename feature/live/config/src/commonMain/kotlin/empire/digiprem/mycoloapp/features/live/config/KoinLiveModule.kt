package empire.digiprem.mycoloapp.features.live.config

import empire.digiprem.mycoloapp.features.live.data.datasource.LiveRemoteDataSource
import empire.digiprem.mycoloapp.features.live.data.repository.LiveRepositoryImpl
import empire.digiprem.mycoloapp.features.live.domain.repository.ILiveRepository
import empire.digiprem.mycoloapp.features.live.domain.usecase.GetActiveSessionsUseCase
import empire.digiprem.mycoloapp.features.live.domain.usecase.SendLiveCommentUseCase
import empire.digiprem.mycoloapp.features.live.domain.usecase.StartLiveUseCase
import empire.digiprem.mycoloapp.features.live.domain.usecase.StopLiveUseCase
import empire.digiprem.mycoloapp.features.live.presentation.broadcast.LiveBroadcastViewModel
import empire.digiprem.mycoloapp.features.live.presentation.lobby.LiveLobbyViewModel
import empire.digiprem.mycoloapp.features.live.presentation.watch.LiveWatchViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val liveModule = module {
    includes(livePlatformModule)

    // Data
    singleOf(::LiveRemoteDataSource)
    singleOf(::LiveRepositoryImpl) bind ILiveRepository::class

    // Use cases
    singleOf(::GetActiveSessionsUseCase)
    singleOf(::StartLiveUseCase)
    singleOf(::StopLiveUseCase)
    singleOf(::SendLiveCommentUseCase)

    // ViewModels
    viewModelOf(::LiveLobbyViewModel)
    viewModelOf(::LiveBroadcastViewModel)
    viewModel { params -> LiveWatchViewModel(
        sessionId = params.get(),
        sendCommentUseCase = get(),
        liveRepository = get(),
        liveKitManager = get(),
    ) }
}
