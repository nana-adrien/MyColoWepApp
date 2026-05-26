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
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReinitialiserMotDePasseScreen(
    onNavigateBack: () -> Unit,
    onNavigateToVerification: (String) -> Unit,
    viewModel: ReinitialiserMotDePasseViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ReinitialiserMotDePasseEvent.OnLienEnvoye -> { /* affichage géré par state.emailEnvoye */ }
                is ReinitialiserMotDePasseEvent.OnNaviguerVersVerification ->
                    onNavigateToVerification(event.email)
            }
        }
    }

    WebFormPageScaffold(
        modifier = Modifier.wrapContentHeight(),
        title = "Réinitialiser le mot de passe",
        description = "Entrez votre email pour recevoir un lien de réinitialisation",
    ) {
        ReinitialiserMotDePasseForm(state = state, onAction = viewModel::onAction)
    }
}

@Composable
private fun ReinitialiserMotDePasseForm(
    state: ReinitialiserMotDePasseState,
    onAction: (ReinitialiserMotDePasseAction) -> Unit,
) {
    FormScaffold(
        errorMessage = state.errorMessage,
        onCleanErrorClick = { onAction(ReinitialiserMotDePasseAction.OnEffacerErreur) },
        footer = {
            if (state.emailEnvoye) {
                MyColoButton(
                    text = "Entrer le code de vérification",
                    isLoading = false,
                    enabled = true,
                    onClick = { onAction(ReinitialiserMotDePasseAction.OnNaviguerVersVerification) },
                )
            } else {
                MyColoButton(
                    text = "Envoyer le lien",
                    isLoading = state.isLoading,
                    enabled = !state.isLoading && state.userCanSend,
                    onClick = { onAction(ReinitialiserMotDePasseAction.OnEnvoyerLienClick) },
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
