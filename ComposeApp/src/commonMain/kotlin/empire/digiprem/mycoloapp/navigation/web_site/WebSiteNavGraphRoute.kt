package empire.digiprem.mycoloapp.navigation.web_site

import empire.digiprem.mycoloapp.features.pre_auth.presentation.LandingPageSection
import kotlinx.serialization.Serializable

@Serializable
sealed interface WebSiteNavGraphRoute {


    @Serializable
    object WebSiteRoute : WebSiteNavGraphRoute

    @Serializable
    data class LandingPage(val section: String = LandingPageSection.HOME_SECTION.name) : WebSiteNavGraphRoute

    @Serializable
    data class RegisterParticipant(val securityCode: String = "") : WebSiteNavGraphRoute

    @Serializable
    data class Error404(val path: String) : WebSiteNavGraphRoute

}