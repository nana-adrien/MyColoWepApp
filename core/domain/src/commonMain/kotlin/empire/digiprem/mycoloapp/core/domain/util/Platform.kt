package empire.digiprem.mycoloapp.core.domain.util

enum class Platform {
    WEB,IOS,ANDROID,DESKTOP
}
expect fun getCurrentPlatform(): Platform