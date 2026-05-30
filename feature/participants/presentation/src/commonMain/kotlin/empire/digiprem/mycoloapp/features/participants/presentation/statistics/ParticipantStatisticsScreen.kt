 package empire.digiprem.mycoloapp.features.participants.presentation.statistics  


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycoloapp.features.participants.presentation.statistics.ParticipantStatisticsAction
import empire.digiprem.mycoloapp.features.participants.presentation.statistics.ParticipantStatisticsState
import empire.digiprem.mycoloapp.features.participants.presentation.statistics.ParticipantStatisticsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel




@Composable
fun ParticipantStatisticsRoot(
    viewModel:ParticipantStatisticsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    ParticipantStatisticsScreen(
        state = state,
        onAction = onAction
    )
}



@Composable
fun ParticipantStatisticsScreen(
    state: ParticipantStatisticsState,
    onAction: (ParticipantStatisticsAction) -> Unit
) {



}











@Composable
private fun ParticipantStatisticsPreview() {
    ParticipantStatisticsScreen(
        state = ParticipantStatisticsState(),
        onAction = {

        }
    )
}


@Preview
@Composable
private fun ParticipantStatisticsLightThemePreview() {
    MyColoWepAppTheme {
        ParticipantStatisticsPreview()
    }
}

@Preview
@Composable
private fun ParticipantStatisticsDarkThemePreview() {
    MyColoWepAppTheme(
        darkTheme = true
    ) {
        ParticipantStatisticsPreview()
    }
}
