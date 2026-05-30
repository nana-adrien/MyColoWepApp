 package empire.digiprem.mycoloapp.features.participants.presentation.register  


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycoloapp.features.participants.presentation.register.RegisterParticipantAction
import empire.digiprem.mycoloapp.features.participants.presentation.register.RegisterParticipantState
import empire.digiprem.mycoloapp.features.participants.presentation.register.RegisterParticipantViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel




@Composable
fun RegisterParticipantRoot(
    viewModel:RegisterParticipantViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    RegisterParticipantScreen(
        state = state,
        onAction = onAction
    )
}



@Composable
fun RegisterParticipantScreen(
    state: RegisterParticipantState,
    onAction: (RegisterParticipantAction) -> Unit
) {



}











@Composable
private fun RegisterParticipantPreview() {
    RegisterParticipantScreen(
        state = RegisterParticipantState(),
        onAction = {

        }
    )
}


@Preview
@Composable
private fun RegisterParticipantLightThemePreview() {
    MyColoWepAppTheme {
        RegisterParticipantPreview()
    }
}

@Preview
@Composable
private fun RegisterParticipantDarkThemePreview() {
    MyColoWepAppTheme(
        darkTheme = true
    ) {
        RegisterParticipantPreview()
    }
}
