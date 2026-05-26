package empire.digiprem.mycoloapp.features.live.config

import empire.digiprem.mycoloapp.features.live.data.service.IosLiveKitManager
import empire.digiprem.mycoloapp.features.live.domain.service.LiveKitManager
import org.koin.dsl.bind
import org.koin.dsl.module

actual val livePlatformModule = module {
    single { IosLiveKitManager() } bind LiveKitManager::class
}
