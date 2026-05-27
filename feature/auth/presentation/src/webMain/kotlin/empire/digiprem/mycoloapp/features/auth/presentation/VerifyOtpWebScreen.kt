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
import empire.digiprem.mycoloapp.features.auth.presentation.verify_identity.VerifyOtpAction
import empire.digiprem.mycoloapp.features.auth.presentation.verify_identity.VerifyOtpEvent
import empire.digiprem.mycoloapp.features.auth.presentation.verify_identity.VerifyOtpViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VerifyOtpWebScreen(
    email: String,
    onNavigateBack: () -> Unit,
    onOtpVerified: () -> Unit,
    viewModel: VerifyOtpViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(email) { viewModel.initEmail(email) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                VerifyOtpEvent.OnOtpVerified -> onOtpVerified()
            }
        }
    }

    WebFormPageScaffold(
        modifier = Modifier.wrapContentHeight(),
        title = "Vérification",
        description = "Code envoyé à $email",
    ) {
        FormScaffold(
            errorMessage = state.errorMessage,
            onCleanErrorClick = { viewModel.onAction(VerifyOtpAction.OnClearError) },
            footer = {
                MyColoButton(
                    text = if (state.isResend) "Renvoi…" else "Renvoyer le code",
                    isLoading = state.isResend,
                    enabled = !state.isLoading && !state.isResend,
                    onClick = { viewModel.onAction(VerifyOtpAction.OnResendCodeClick) },
                )
                MyColoButton(
                    text = "Vérifier",
                    isLoading = state.isLoading,
                    enabled = !state.isLoading && state.userCanSend,
                    onClick = { viewModel.onAction(VerifyOtpAction.OnVerifyClick) },
                )
            }
        ) {
            MyColoTextField(
                state = state.otpState,
                title = "Code OTP (6 chiffres)",
                placeholder = "123456",
                keyboardType = KeyboardType.NumberPassword,
            )
        }
    }
}
