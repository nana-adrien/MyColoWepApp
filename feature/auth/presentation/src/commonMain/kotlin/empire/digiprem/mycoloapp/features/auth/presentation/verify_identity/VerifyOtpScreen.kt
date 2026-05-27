package empire.digiprem.mycoloapp.features.auth.presentation.verify_identity

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
import empire.digiprem.mycoloapp.features.auth.presentation.components.AuthenticationScaffold
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VerifyOtpScreen(
    email: String,
    onNavigateBack: () -> Unit,
    onOtpVerified: () -> Unit,
    viewModel: VerifyOtpViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(email) {
        viewModel.initEmail(email)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                VerifyOtpEvent.OnOtpVerified -> onOtpVerified()
            }
        }
    }

    AuthenticationScaffold(
        modifier = Modifier.wrapContentHeight(),
        title = "Vérification",
        description = "Entrez le code à 6 chiffres envoyé à $email",
    ) {
        VerifyOtpForm(state = state, onAction = viewModel::onAction)
    }
}

@Composable
private fun VerifyOtpForm(
    state: VerifyOtpState,
    onAction: (VerifyOtpAction) -> Unit,
) {
    FormScaffold(
        errorMessage = state.errorMessage,
        onCleanErrorClick = { onAction(VerifyOtpAction.OnClearError) },
        footer = {
            MyColoButton(
                text = if (state.isResend) "Renvoi en cours…" else "Renvoyer le code",
                isLoading = state.isResend,
                enabled = !state.isLoading && !state.isResend,
                onClick = { onAction(VerifyOtpAction.OnResendCodeClick) },
            )
            MyColoButton(
                text = "Vérifier",
                isLoading = state.isLoading,
                enabled = !state.isLoading && state.userCanSend,
                onClick = { onAction(VerifyOtpAction.OnVerifyClick) },
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
