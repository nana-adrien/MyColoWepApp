package empire.digiprem.mycoloapp.features.profile.config

import empire.digiprem.mycoloapp.features.profile.data.repository.FakeProfileRepository
import empire.digiprem.mycoloapp.features.profile.domain.repository.IProfileRepository
import empire.digiprem.mycoloapp.features.profile.presentation.profile.ProfileViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val profileModule = module {
    singleOf(::FakeProfileRepository) bind IProfileRepository::class
    viewModelOf(::ProfileViewModel)
}
