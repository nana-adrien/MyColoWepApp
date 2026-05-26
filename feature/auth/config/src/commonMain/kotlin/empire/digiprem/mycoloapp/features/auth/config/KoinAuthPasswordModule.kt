package empire.digiprem.mycoloapp.features.auth.config

import empire.digiprem.mycoloapp.features.auth.data.datasource.AuthPasswordRemoteDataSource
import empire.digiprem.mycoloapp.features.auth.data.repository.AuthPasswordRepository
import empire.digiprem.mycoloapp.features.auth.domain.repository.IAuthPasswordRepository
import empire.digiprem.mycoloapp.features.auth.domain.usecase.ModifierMotDePasseUseCase
import empire.digiprem.mycoloapp.features.auth.domain.usecase.ReinitialiserMotDePasseUseCase
import empire.digiprem.mycoloapp.features.auth.domain.usecase.VerifierMotDePasseUseCase
import empire.digiprem.mycoloapp.features.auth.presentation.ModifierMotDePasseViewModel
import empire.digiprem.mycoloapp.features.auth.presentation.ReinitialiserMotDePasseViewModel
import empire.digiprem.mycoloapp.features.auth.presentation.VerifierMotDePasseViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authPasswordModule = module {
    singleOf(::AuthPasswordRemoteDataSource)
    singleOf(::AuthPasswordRepository) bind IAuthPasswordRepository::class
    singleOf(::ModifierMotDePasseUseCase)
    singleOf(::ReinitialiserMotDePasseUseCase)
    singleOf(::VerifierMotDePasseUseCase)
    viewModelOf(::ModifierMotDePasseViewModel)
    viewModelOf(::ReinitialiserMotDePasseViewModel)
    viewModelOf(::VerifierMotDePasseViewModel)
}
