package empire.digiprem.mycoloapp.navigation.web_site

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import androidx.navigation.toRoute
import empire.digiprem.mycoloapp.core.design_system.WebPageScaffold
import empire.digiprem.mycoloapp.core.design_system.components.MyColoFooter
import empire.digiprem.mycoloapp.core.design_system.components.MyColoNavBar
import empire.digiprem.mycoloapp.core.design_system.components.NavItem
import empire.digiprem.mycoloapp.core.design_system.theme.Primary
import empire.digiprem.mycoloapp.core.navigation.NavigationGraph
import empire.digiprem.mycoloapp.features.pre_auth.presentation.LandingPageSection
import empire.digiprem.mycoloapp.features.pre_auth.presentation.LandingPageSection.Companion.fromString
import empire.digiprem.mycoloapp.features.pre_auth.presentation.LandingScreen
import empire.digiprem.mycoloapp.features.profile.presentation.profile.ProfileRoot
import empire.digiprem.mycoloapp.features.registration.presentation.RegistrationScreen
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.landing_nav_contact
import mycolowepapp.shared.generated.resources.landing_nav_program
import mycolowepapp.shared.generated.resources.powered_by


fun NavGraphBuilder.webSiteNavGraph(
    navController: NavHostController,
    onLoginButtonClick: () -> Unit,
) {

    navigation<WebSiteNavGraphRoute.WebSiteRoute>(
        startDestination = WebSiteNavGraphRoute.LandingPage(),
    ) {
        composable<WebSiteNavGraphRoute.LandingPage> {
            LandingScreen(
                navigateToSection = fromString(it.toRoute<WebSiteNavGraphRoute.LandingPage>().section),
                onNavigateToRegistration = {
                    navController.navigate(WebSiteNavGraphRoute.RegisterParticipant("test1235"))
                },
                onNavigateToAdmin = onLoginButtonClick,
                onNavigateToLive = {
                    navController.navigate(NavigationGraph.LiveLobby)
                }
            )
        }

        composable<WebSiteNavGraphRoute.RegisterParticipant> {
            RegistrationScreen(
                defaultAdminSecurityCode = it.toRoute<WebSiteNavGraphRoute.RegisterParticipant>().securityCode,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToConfirmation = { ref ->
                    /* navController.navigate(NavigationGraph.Confirmation(ref)) {
                         popUpTo<NavigationGraph.Registration> { inclusive = true }
                     }*/
                }
            )
        }
        composable<WebSiteNavGraphRoute.Error404> {
            Box(modifier = Modifier.fillMaxSize().background(Color.Red))
        }
    }
}


