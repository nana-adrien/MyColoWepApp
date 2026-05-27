package empire.digiprem.mycoloapp.features.auth.presentation

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
import empire.digiprem.mycoloapp.features.auth.presentation.reset_password.ResetPasswordAction
import empire.digiprem.mycoloapp.features.auth.presentation.reset_password.ResetPasswordEvent
import empire.digiprem.mycoloapp.features.auth.presentation.reset_password.ResetPasswordViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ResetPasswordDesktopScreen(
    onNavigateBack: () -> Unit,
    onNavigateToVerification: (String) -> Unit,
    viewModel: ResetPasswordViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ResetPasswordEvent.OnLinkSent -> {}
                is ResetPasswordEvent.OnNavigateToVerification ->
                    onNavigateToVerification(event.email)
            }
        }
    }

    WebFormPageScaffold(
        modifier = Modifier.wrapContentHeight(),
        title = "Réinitialiser le mot de passe",
        description = "Un code sera envoyé à votre adresse email",
    ) {
        FormScaffold(
            errorMessage = state.errorMessage,
            onCleanErrorClick = { viewModel.onAction(ResetPasswordAction.OnClearError) },
            footer = {
                if (state.emailSent) {
                    MyColoButton(
                        text = "Continuer vers la vérification",
                        isLoading = false,
                        enabled = true,
                        onClick = { viewModel.onAction(ResetPasswordAction.OnNavigateToVerification) },
                    )
                } else {
                    MyColoButton(
                        text = "Envoyer le lien de réinitialisation",
                        isLoading = state.isLoading,
                        enabled = !state.isLoading && state.userCanSend,
                        onClick = { viewModel.onAction(ResetPasswordAction.OnSendLinkClick) },
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
}
