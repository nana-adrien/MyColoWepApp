package empire.digiprem.mycoloapp.features.registration.config

import empire.digiprem.mycoloapp.features.registration.domain.repository.IRegistrationRepository
import empire.digiprem.mycoloapp.features.registration.presentation.RegistrationViewModel
import empire.digiprem.mycoloapp.features.registration.presentation.form.RegisterFormViewModel
import empire.digiprem.mycoloapp.features.registration.data.datasource.RegistrationRemoteDataSource
import empire.digiprem.mycoloapp.features.registration.data.repository.RegistrationRepository
import empire.digiprem.mycoloapp.features.registration.domain.usecase.RegisterParticipantUseCase
import empire.digiprem.mycoloapp.features.registration.domain.usecase.ValidateSecurityCodeUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val registrationModule = module {
    singleOf(::RegistrationRepository) bind IRegistrationRepository::class
    singleOf(::RegistrationRemoteDataSource)
    singleOf(::RegisterParticipantUseCase)
    singleOf(::ValidateSecurityCodeUseCase)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::RegisterFormViewModel)
}
