package empire.digiprem.mycoloapp.features.auth.config

import empire.digiprem.mycoloapp.features.auth.data.datasource.AuthPasswordRemoteDataSource
import empire.digiprem.mycoloapp.features.auth.data.repository.AuthPasswordRepository
import empire.digiprem.mycoloapp.features.auth.domain.repository.IAuthPasswordRepository
import empire.digiprem.mycoloapp.features.auth.domain.usecase.ChangePasswordUseCase
import empire.digiprem.mycoloapp.features.auth.domain.usecase.ResetPasswordUseCase
import empire.digiprem.mycoloapp.features.auth.domain.usecase.VerifyOtpUseCase
import empire.digiprem.mycoloapp.features.auth.presentation.fogot_password.ChangePasswordViewModel
import empire.digiprem.mycoloapp.features.auth.presentation.reset_password.ResetPasswordViewModel
import empire.digiprem.mycoloapp.features.auth.presentation.verify_identity.VerifyOtpViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authPasswordModule = module {
    singleOf(::AuthPasswordRemoteDataSource)
    singleOf(::AuthPasswordRepository) bind IAuthPasswordRepository::class
    singleOf(::ChangePasswordUseCase)
    singleOf(::ResetPasswordUseCase)
    singleOf(::VerifyOtpUseCase)
    viewModelOf(::ChangePasswordViewModel)
    viewModelOf(::ResetPasswordViewModel)
    viewModelOf(::VerifyOtpViewModel)
}
