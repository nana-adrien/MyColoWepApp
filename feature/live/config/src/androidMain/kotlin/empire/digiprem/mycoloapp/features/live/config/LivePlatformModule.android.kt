package empire.digiprem.mycoloapp.features.live.config

import empire.digiprem.mycoloapp.features.live.data.service.AndroidLiveKitManager
import empire.digiprem.mycoloapp.features.live.domain.service.LiveKitManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

actual val livePlatformModule = module {
    single { AndroidLiveKitManager(androidContext()) } bind LiveKitManager::class
}
