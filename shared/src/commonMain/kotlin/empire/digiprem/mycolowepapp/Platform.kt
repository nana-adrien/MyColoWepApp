package empire.digiprem.mycolowepapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform