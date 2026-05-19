package empire.digiprem.mycolowepapp.feature.admin.dashboard.data.di

import empire.digiprem.mycolowepapp.feature.admin.dashboard.data.datasource.ParticipantRemoteDataSource
import empire.digiprem.mycolowepapp.feature.admin.dashboard.data.repository.ParticipantRepository
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.repository.IParticipantRepository
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.usecase.GetParticipantsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dashboardDataModule = module {
    singleOf(::ParticipantRemoteDataSource)
    singleOf(::ParticipantRepository) bind IParticipantRepository::class
    singleOf(::GetParticipantsUseCase)
}
