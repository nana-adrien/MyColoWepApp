package empire.digiprem.mycoloapp.features.auth.presentation

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
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ModifierMotDePasseDesktopScreen(
    onNavigateBack: () -> Unit,
    onPasswordUpdated: () -> Unit,
    viewModel: ModifierMotDePasseViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ModifierMotDePasseEvent.OnMotDePasseModifie -> onPasswordUpdated()
            }
        }
    }

    WebFormPageScaffold(
        modifier = Modifier.wrapContentHeight(),
        title = "Modifier le mot de passe",
        description = "Saisissez votre nouveau mot de passe sécurisé",
    ) {
        FormScaffold(
            errorMessage = state.errorMessage,
            onCleanErrorClick = { viewModel.onAction(ModifierMotDePasseAction.OnEffacerErreur) },
            footer = {
                MyColoButton(
                    text = "Confirmer la modification",
                    isLoading = state.isLoading,
                    enabled = !state.isLoading && state.userCanSend,
                    onClick = { viewModel.onAction(ModifierMotDePasseAction.OnConfirmerClick) },
                )
            }
        ) {
            MyColoPasswordTextField(
                state = state.nouveauMotDePasseState,
                title = "Nouveau mot de passe",
                placeholder = "Minimum 6 caractères",
                isPasswordVisible = state.isNouveauMotDePasseVisible,
                enabled = !state.isLoading,
                onToggleVisibility = { viewModel.onAction(ModifierMotDePasseAction.OnToggleNouveauMotDePasseVisibility) },
            )
            MyColoPasswordTextField(
                state = state.confirmationMotDePasseState,
                title = "Confirmer le mot de passe",
                placeholder = "Répétez le mot de passe",
                isPasswordVisible = state.isConfirmationMotDePasseVisible,
                enabled = !state.isLoading,
                onToggleVisibility = { viewModel.onAction(ModifierMotDePasseAction.OnToggleConfirmationMotDePasseVisibility) },
            )
        }
    }
}
