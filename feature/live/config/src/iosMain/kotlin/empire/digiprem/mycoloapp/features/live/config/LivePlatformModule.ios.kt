package empire.digiprem.mycoloapp.features.live.config

import empire.digiprem.mycoloapp.features.live.data.service.StubLiveKitManager
import empire.digiprem.mycoloapp.features.live.domain.service.LiveKitManager
import org.koin.dsl.bind
import org.koin.dsl.module

actual val livePlatformModule = module {
    single { StubLiveKitManager() } bind LiveKitManager::class
}
