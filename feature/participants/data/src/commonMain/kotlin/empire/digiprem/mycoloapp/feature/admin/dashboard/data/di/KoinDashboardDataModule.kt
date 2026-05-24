package empire.digiprem.mycoloapp.feature.admin.dashboard.data.di

import empire.digiprem.mycoloapp.feature.admin.dashboard.data.datasource.ParticipantRemoteDataSource
import empire.digiprem.mycoloapp.feature.admin.dashboard.data.repository.ParticipantRepository
import empire.digiprem.mycoloapp.feature.admin.dashboard.domain.repository.IParticipantRepository
import empire.digiprem.mycoloapp.feature.admin.dashboard.domain.usecase.GetParticipantsUseCase
import empire.digiprem.mycoloapp.feature.admin.dashboard.domain.usecase.ObserveParticipantsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dashboardDataModule = module {
    singleOf(::ParticipantRemoteDataSource)
    singleOf(::ParticipantRepository) bind IParticipantRepository::class
    singleOf(::GetParticipantsUseCase)
    singleOf(::ObserveParticipantsUseCase)
}
