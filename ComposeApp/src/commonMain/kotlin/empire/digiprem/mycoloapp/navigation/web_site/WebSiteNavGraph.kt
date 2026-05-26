package octopusfx.client.mobile.boxoffice.navigation.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import octopusfx.client.mobile.boxoffice.dashbord.DashBoardRoot
import octopusfx.client.mobile.boxoffice.features.notifications.presentation.notifications.NotificationRoot
import octopusfx.client.mobile.boxoffice.features.profiles.presentation.profile.ProfileRoot
import octopusfx.client.mobile.boxoffice.features.settings.presentation.general_settings.GeneralSettingsRoot
import octopusfx.client.mobile.boxoffice.home.HomeRoot
import octopusfx.client.mobile.core.design_system.components.icon.OctopusfxIcon
import octopusfx.client.mobile.core.design_system.components.icon.OctopusfxIconResource
import octopusfx.client.mobile.core.design_system.components.iconButtons.OctopusfxIconButton


fun NavGraphBuilder.appNavGraph(
    navController: NavHostController,
    onLogOutSuccess: () -> Unit,
){
    navigation<AppNavGraphRoute.Application>(
        startDestination = AppNavGraphRoute.Home,
    ){
        composable<AppNavGraphRoute.Home> {
            HomeRoot(
                onLogOutSuccess=onLogOutSuccess,
                onNavigate = { route ->
                    navController.navigate(route){
                        launchSingleTop = true
                    }
                }

            )
        }
        composable<AppNavGraphRoute.SettingsGroup> {
            GeneralSettingsRoot(onBackButtonClick = { navController.popBackStack() })
        }
        composable<AppNavGraphRoute.Profile> {
            ProfileRoot(onBackButtonClick = { navController.popBackStack() })
        }
        composable<AppNavGraphRoute.Notification> {
            NotificationRoot(onBackButtonClick = { navController.popBackStack() })
        }
    }
}
@Composable

fun SettingsGroup(onNavigate: () -> Unit) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "SettingsGroup")

        OctopusfxIconButton(
            icon = OctopusfxIconResource.VectorResource(Icons.Filled.Settings),
            contentDescription = "SettingsGroup",
            onClick = {onNavigate()},
        )

    }
}
