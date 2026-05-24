package empire.digiprem.mycoloapp.feature.registration.config

import empire.digiprem.mycoloapp.feature.registration.data.di.registrationDataModule
import empire.digiprem.mycoloapp.feature.registration.presentation.RegistrationViewModel
import empire.digiprem.mycoloapp.feature.registration.presentation.form.RegisterFormViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val registrationModule = module {
    includes(registrationDataModule)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::RegisterFormViewModel)
}
