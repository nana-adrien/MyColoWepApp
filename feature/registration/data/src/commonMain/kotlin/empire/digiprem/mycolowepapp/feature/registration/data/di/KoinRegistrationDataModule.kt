package empire.digiprem.mycolowepapp.feature.registration.data.di

import empire.digiprem.mycolowepapp.feature.registration.data.datasource.RegistrationRemoteDataSource
import empire.digiprem.mycolowepapp.feature.registration.data.repository.RegistrationRepository
import empire.digiprem.mycolowepapp.feature.registration.domain.repository.IRegistrationRepository
import empire.digiprem.mycolowepapp.feature.registration.domain.usecase.RegisterParticipantUseCase
import empire.digiprem.mycolowepapp.feature.registration.domain.usecase.ValidateSecurityCodeUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val registrationDataModule = module {
    singleOf(::RegistrationRemoteDataSource)
    singleOf(::RegistrationRepository) bind IRegistrationRepository::class
    singleOf(::ValidateSecurityCodeUseCase)
    singleOf(::RegisterParticipantUseCase)
}
