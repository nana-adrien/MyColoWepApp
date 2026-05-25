package empire.digiprem.mycoloapp.features.auth.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycoloapp.core.design_system.FormScaffold
import empire.digiprem.mycoloapp.core.design_system.MyColoButton
import empire.digiprem.mycoloapp.core.design_system.WebFormPageScaffold
import empire.digiprem.mycoloapp.core.design_system.WebPageScaffold
import empire.digiprem.mycoloapp.core.design_system.components.MyColoFooter
import empire.digiprem.mycoloapp.core.design_system.components.MyColoNavBar
import empire.digiprem.mycoloapp.core.design_system.components.NavItem
import empire.digiprem.mycoloapp.core.design_system.components.form.MyColoPasswordTextField
import empire.digiprem.mycoloapp.core.design_system.components.form.MyColoTextField
import empire.digiprem.mycoloapp.core.design_system.theme.Primary
import kotlinx.coroutines.launch
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.admin_connect
import mycolowepapp.shared.generated.resources.admin_email
import mycolowepapp.shared.generated.resources.admin_email_hint
import mycolowepapp.shared.generated.resources.admin_login_subtitle
import mycolowepapp.shared.generated.resources.admin_login_title
import mycolowepapp.shared.generated.resources.admin_password
import mycolowepapp.shared.generated.resources.admin_password_hint
import mycolowepapp.shared.generated.resources.landing_nav_contact
import mycolowepapp.shared.generated.resources.landing_nav_program
import mycolowepapp.shared.generated.resources.powered_by
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AdminLoginScreen(
    onNavigateBack: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: AdminLoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val navItems = listOf(
        NavItem("program", stringResource(Res.string.landing_nav_program)),
        NavItem("contact", stringResource(Res.string.landing_nav_contact)),
    )
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AdminLoginEvent.OnLoginSuccess -> onLoginSuccess()
            }
        }
    }

    WebPageScaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize(),
        scrollState = scrollState,
        header = {
            MyColoNavBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                scrollState = scrollState,
                navItems = navItems,
                onNavItemClick = { item ->
                    coroutineScope.launch {
                        when (item.id) {
                            "program" -> scrollState.animateScrollToItem(1)
                            "contact" -> scrollState.animateScrollToItem(scrollState.layoutInfo.totalItemsCount - 1)
                        }
                    }
                },
                onAdminClick = onNavigateBack
            )
        },
        footer = { MyColoFooter(stringResource(Res.string.powered_by)) },
        floatingButton = {
            AnimatedVisibility(
                modifier = Modifier.padding(end = 16.dp, bottom = 24.dp),
                visible = scrollState.firstVisibleItemIndex > 0,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally(),
            ) {
                Box(
                    modifier = Modifier.size(48.dp).background(Primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) { Text("↑", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold) }
            }
        }
    ) {
        item {
            WebFormPageScaffold(
                modifier = Modifier.wrapContentHeight(),
                title = stringResource(Res.string.admin_login_title),
                description = stringResource(Res.string.admin_login_subtitle)
            ) {
                AdminLoginForm(state = state, onAction = viewModel::onAction)
            }
        }
    }
}

@Composable
private fun AdminLoginForm(
    modifier: Modifier=Modifier,
    state: AdminLoginState,
    onAction: (AdminLoginAction) -> Unit,
) {
    FormScaffold(
        modifier = modifier,
        errorMessage = state.errorMessage,
        onCleanErrorClick = {onAction(AdminLoginAction.OnLeanErrorMessageClick) },
        footer = {
            MyColoButton(
                enabled = !state.isLoading && state.userCanSend,
                isLoading = state.isLoading,
                text = stringResource(Res.string.admin_connect),
                onClick = { onAction(AdminLoginAction.OnLoginClick) }
            )
        }
    ){
        MyColoTextField(
            state = state.emailTextFieldState,
            title = stringResource(Res.string.admin_email),
            leadingIcon = Icons.Filled.Email,
            placeholder = stringResource(Res.string.admin_email_hint),
            keyboardType = KeyboardType.Email

        )

        MyColoPasswordTextField(
            state = state.passwordTextFieldState,
            title = stringResource(Res.string.admin_password),
            placeholder = stringResource(Res.string.admin_password_hint),
            enabled = !state.isLoading,
        )
    }
}
