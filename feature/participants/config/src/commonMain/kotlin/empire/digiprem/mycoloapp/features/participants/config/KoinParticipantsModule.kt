package empire.digiprem.mycoloapp.features.participants.config

import empire.digiprem.mycoloapp.features.participants.data.datasource.ParticipantRemoteDataSource
import empire.digiprem.mycoloapp.features.participants.data.repository.ParticipantRepository
import empire.digiprem.mycoloapp.features.participants.domain.repository.IParticipantRepository
import empire.digiprem.mycoloapp.features.participants.domain.usecase.GetParticipantsUseCase
import empire.digiprem.mycoloapp.features.participants.presentation.AdminDashboardViewModel
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
