 package empire.digiprem.mycoloapp.features.participants.presentation.detail  


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycoloapp.features.participants.presentation.detail.ParticipantItemDetailsAction
import empire.digiprem.mycoloapp.features.participants.presentation.detail.ParticipantItemDetailsState
import empire.digiprem.mycoloapp.features.participants.presentation.detail.ParticipantItemDetailsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel




@Composable
fun ParticipantItemDetailsRoot(
    viewModel:ParticipantItemDetailsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    ParticipantItemDetailsScreen(
        state = state,
        onAction = onAction
    )
}



@Composable
fun ParticipantItemDetailsScreen(
    state: ParticipantItemDetailsState,
    onAction: (ParticipantItemDetailsAction) -> Unit
) {



}











@Composable
private fun ParticipantItemDetailsPreview() {
    ParticipantItemDetailsScreen(
        state = ParticipantItemDetailsState(),
        onAction = {

        }
    )
}


@Preview
@Composable
private fun ParticipantItemDetailsLightThemePreview() {
    MyColoWepAppTheme {
        ParticipantItemDetailsPreview()
    }
}

@Preview
@Composable
private fun ParticipantItemDetailsDarkThemePreview() {
    MyColoWepAppTheme(
        darkTheme = true
    ) {
        ParticipantItemDetailsPreview()
    }
}
