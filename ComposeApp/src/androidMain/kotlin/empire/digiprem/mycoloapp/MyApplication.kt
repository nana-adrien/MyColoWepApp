package empire.digiprem.mycoloapp

import android.app.Application
import empire.digiprem.mycoloapp.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MyApplication)
        }
    }
}