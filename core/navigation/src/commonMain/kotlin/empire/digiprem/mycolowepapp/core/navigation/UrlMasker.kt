package empire.digiprem.mycolowepapp.core.navigation

object UrlMasker {
    // Table de correspondance : Vraie Route -> Route Masquée dans l'URL


    private val maskTable = mapOf(
        NavigationGraph.Landing::class.simpleName to "home",
        NavigationGraph.Registration::class.simpleName to "join-us",
        NavigationGraph.Confirmation::class.simpleName to "success",
        NavigationGraph.AdminLogin::class.simpleName to "kx9z2m",
        NavigationGraph.AdminDashboard::class.simpleName to "p4q7w1"
    )
    // Table inverse pour le décodage : Route masquée -> Nom de la classe technique
    private val unmaskTable = maskTable.entries.associate { it.value to it.key }

    /**
     * Reçoit l'objet de destination de Jetpack Navigation (ex: Landing)
     * et extrait son nom de classe pour trouver le masque.
     */
    fun mask(routeClassName: String?): String {
        if (routeClassName == null) return ""
        // Si le routeClassName contient le package (ex: com.digiprem.NavigationGraph.Landing), on ne prend que la fin
        val cleanClassName = routeClassName.substringAfterLast(".")
        return maskTable[cleanClassName] ?: ""
    }

    /**
     * Reçoit la route masquée de l'URL et retourne le nom de la classe technique correspondante.
     */
    fun unmask(maskedRoute: String): String {
        return unmaskTable[maskedRoute] ?: maskedRoute
    }
}