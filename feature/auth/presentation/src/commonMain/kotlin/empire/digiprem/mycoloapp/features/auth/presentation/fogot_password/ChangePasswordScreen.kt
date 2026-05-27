package empire.digiprem.mycoloapp.features.auth.presentation.fogot_password

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycoloapp.core.design_system.FormScaffold
import empire.digiprem.mycoloapp.core.design_system.MyColoButton
import empire.digiprem.mycoloapp.core.design_system.WebFormPageScaffold
import empire.digiprem.mycoloapp.core.design_system.components.form.MyColoPasswordTextField
import empire.digiprem.mycoloapp.features.auth.presentation.components.AuthenticationScaffold
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChangePasswordScreen(
    onNavigateBack: () -> Unit,
    onPasswordUpdated: () -> Unit,
    viewModel: ChangePasswordViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ChangePasswordEvent.OnPasswordChanged -> onPasswordUpdated()
            }
        }
    }

    AuthenticationScaffold(
        modifier = Modifier.wrapContentHeight(),
        title = "Modifier le mot de passe",
        description = "Saisissez votre nouveau mot de passe",
    ) {
        ChangePasswordForm(state = state, onAction = viewModel::onAction)
    }
}

@Composable
private fun ChangePasswordForm(
    state: ChangePasswordState,
    onAction: (ChangePasswordAction) -> Unit,
) {
    FormScaffold(
        errorMessage = state.errorMessage,
        onCleanErrorClick = { onAction(ChangePasswordAction.OnClearError) },
        footer = {
            MyColoButton(
                text = "Confirmer",
                isLoading = state.isLoading,
                enabled = !state.isLoading && state.userCanSend,
                onClick = { onAction(ChangePasswordAction.OnConfirmClick) },
            )
        }
    ) {
        MyColoPasswordTextField(
            state = state.newPasswordState,
            title = "Nouveau mot de passe",
            placeholder = "Minimum 6 caractères",
           // isPasswordVisible = state.isNewPasswordVisible,
            enabled = !state.isLoading,
           // onToggleVisibility = { onAction(ChangePasswordAction.OnToggleNewPasswordVisibility) },
        )
        MyColoPasswordTextField(
            state = state.confirmPasswordState,
            title = "Confirmer le mot de passe",
            placeholder = "Répétez le mot de passe",
            //isPasswordVisible = state.isConfirmPasswordVisible,
            enabled = !state.isLoading,
           // onToggleVisibility = { onAction(ChangePasswordAction.OnToggleConfirmPasswordVisibility) },
        )
    }
}
