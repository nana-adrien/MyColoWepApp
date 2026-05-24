package empire.digiprem.mycoloapp.feature.admin.security_code.data.di

import empire.digiprem.mycoloapp.feature.admin.security_code.data.datasource.SecurityCodeRemoteDataSource
import empire.digiprem.mycoloapp.feature.admin.security_code.data.repository.SecurityCodeRepository
import empire.digiprem.mycoloapp.feature.admin.security_code.domain.repository.ISecurityCodeRepository
import empire.digiprem.mycoloapp.feature.admin.security_code.domain.usecase.GenerateSecurityCodeUseCase
import empire.digiprem.mycoloapp.feature.admin.security_code.domain.usecase.GetSecurityCodesUseCase
import empire.digiprem.mycoloapp.feature.admin.security_code.domain.usecase.ToggleSecurityCodeUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val securityCodeDataModule = module {
    singleOf(::SecurityCodeRemoteDataSource)
    singleOf(::SecurityCodeRepository) bind ISecurityCodeRepository::class
    singleOf(::GetSecurityCodesUseCase)
    singleOf(::GenerateSecurityCodeUseCase)
    singleOf(::ToggleSecurityCodeUseCase)
}
