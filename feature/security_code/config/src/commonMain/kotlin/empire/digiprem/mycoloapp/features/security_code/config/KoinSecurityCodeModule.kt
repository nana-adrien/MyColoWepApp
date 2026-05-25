package empire.digiprem.mycoloapp.features.security_code.config

import empire.digiprem.mycoloapp.features.security_code.data.datasource.SecurityCodeRemoteDataSource
import empire.digiprem.mycoloapp.features.security_code.data.repository.SecurityCodeRepository
import empire.digiprem.mycoloapp.features.security_code.domain.repository.ISecurityCodeRepository
import empire.digiprem.mycoloapp.features.security_code.domain.usecase.GetSecurityCodesUseCase
import empire.digiprem.mycoloapp.features.security_code.presentation.SecurityCodeViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val securityCodeModule = module {
    singleOf(::SecurityCodeRemoteDataSource)
    singleOf(::SecurityCodeRepository) bind ISecurityCodeRepository::class
    singleOf(::GetSecurityCodesUseCase)
    viewModelOf(::SecurityCodeViewModel)
}
