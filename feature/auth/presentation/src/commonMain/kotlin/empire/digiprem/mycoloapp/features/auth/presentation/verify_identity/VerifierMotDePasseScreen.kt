package empire.digiprem.mycoloapp.features.auth.presentation

import androidx.compose.foundation.layout.wrapContentHeight
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
fun VerifierMotDePasseScreen(
    email: String,
    onNavigateBack: () -> Unit,
    onOtpVerified: () -> Unit,
    viewModel: VerifierMotDePasseViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(email) {
        viewModel.initEmail(email)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                VerifierMotDePasseEvent.OnOtpVerifie -> onOtpVerified()
            }
        }
    }

    WebFormPageScaffold(
        modifier = Modifier.wrapContentHeight(),
        title = "Vérification",
        description = "Entrez le code à 6 chiffres envoyé à $email",
    ) {
        VerifierMotDePasseForm(state = state, onAction = viewModel::onAction)
    }
}

@Composable
private fun VerifierMotDePasseForm(
    state: VerifierMotDePasseState,
    onAction: (VerifierMotDePasseAction) -> Unit,
) {
    FormScaffold(
        errorMessage = state.errorMessage,
        onCleanErrorClick = { onAction(VerifierMotDePasseAction.OnEffacerErreur) },
        footer = {
            MyColoButton(
                text = if (state.isRenvoi) "Renvoi en cours…" else "Renvoyer le code",
                isLoading = state.isRenvoi,
                enabled = !state.isLoading && !state.isRenvoi,
                onClick = { onAction(VerifierMotDePasseAction.OnRenvoyerCodeClick) },
            )
            MyColoButton(
                text = "Vérifier",
                isLoading = state.isLoading,
                enabled = !state.isLoading && state.userCanSend,
                onClick = { onAction(VerifierMotDePasseAction.OnVerifierClick) },
            )
        }
    ) {
        MyColoTextField(
            state = state.otpState,
            title = "Code OTP",
            placeholder = "123456",
            keyboardType = KeyboardType.NumberPassword,
        )
    }
}
