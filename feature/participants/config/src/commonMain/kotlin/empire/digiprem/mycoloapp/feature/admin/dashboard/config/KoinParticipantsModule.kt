package empire.digiprem.mycoloapp.feature.admin.dashboard.config

import empire.digiprem.mycoloapp.feature.admin.dashboard.data.datasource.ParticipantRemoteDataSource
import empire.digiprem.mycoloapp.feature.admin.dashboard.data.repository.ParticipantRepository
import empire.digiprem.mycoloapp.feature.admin.dashboard.domain.repository.IParticipantRepository
import empire.digiprem.mycoloapp.feature.admin.dashboard.domain.usecase.GetParticipantsUseCase
import empire.digiprem.mycoloapp.feature.admin.dashboard.presentation.AdminDashboardViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val participantsModule = module {
    singleOf(::ParticipantRemoteDataSource)
    singleOf(::ParticipantRepository) bind IParticipantRepository::class
    singleOf(::GetParticipantsUseCase)
    viewModelOf(::AdminDashboardViewModel)
}
