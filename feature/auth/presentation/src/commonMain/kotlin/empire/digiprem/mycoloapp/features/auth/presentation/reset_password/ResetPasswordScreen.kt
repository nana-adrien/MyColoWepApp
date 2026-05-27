package empire.digiprem.mycoloapp.features.auth.presentation.reset_password

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycoloapp.core.design_system.FormScaffold
import empire.digiprem.mycoloapp.core.design_system.MyColoButton
import empire.digiprem.mycoloapp.core.design_system.WebFormPageScaffold
import empire.digiprem.mycoloapp.core.design_system.components.form.MyColoTextField
import empire.digiprem.mycoloapp.features.auth.presentation.components.AuthenticationScaffold
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ResetPasswordScreen(
    onNavigateBack: () -> Unit,
    onNavigateToVerification: (String) -> Unit,
    viewModel: ResetPasswordViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ResetPasswordEvent.OnLinkSent -> { /* affichage géré par state.emailSent */ }
                is ResetPasswordEvent.OnNavigateToVerification ->
                    onNavigateToVerification(event.email)
            }
        }
    }

    AuthenticationScaffold(
        modifier = Modifier.wrapContentHeight(),
        title = "Réinitialiser le mot de passe",
        description = "Entrez votre email pour recevoir un lien de réinitialisation",
    ) {
        ResetPasswordForm(state = state, onAction = viewModel::onAction)
    }
}

@Composable
private fun ResetPasswordForm(
    state: ResetPasswordState,
    onAction: (ResetPasswordAction) -> Unit,
) {
    FormScaffold(
        errorMessage = state.errorMessage,
        onCleanErrorClick = { onAction(ResetPasswordAction.OnClearError) },
        footer = {
            if (state.emailSent) {
                MyColoButton(
                    text = "Entrer le code de vérification",
                    isLoading = false,
                    enabled = true,
                    onClick = { onAction(ResetPasswordAction.OnNavigateToVerification) },
                )
            } else {
                MyColoButton(
                    text = "Envoyer le lien",
                    isLoading = state.isLoading,
                    enabled = !state.isLoading && state.userCanSend,
                    onClick = { onAction(ResetPasswordAction.OnSendLinkClick) },
                )
            }
        }
    ) {
        MyColoTextField(
            state = state.emailState,
            title = "Adresse email",
            placeholder = "exemple@email.com",
            leadingIcon = Icons.Filled.Email,
            keyboardType = KeyboardType.Email,
        )
    }
}
