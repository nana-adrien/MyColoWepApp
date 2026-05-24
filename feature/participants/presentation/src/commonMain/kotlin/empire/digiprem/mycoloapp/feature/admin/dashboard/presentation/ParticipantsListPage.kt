package empire.digiprem.mycoloapp.feature.admin.dashboard.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import empire.digiprem.mycoloapp.core.design_system.components.datalist.DataListScreen
import empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ColumnDef
import empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ExportFormat
import empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction
import empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListState
import empire.digiprem.mycoloapp.core.design_system.extension.format
import empire.digiprem.mycoloapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycoloapp.feature.registration.domain.model.EducationLevel

@Composable
internal fun ParticipantsListPage(
    state: AdminDashboardState,
    onAction: (AdminDashboardAction) -> Unit,
) {
    val listState = state.toParticipantListState()

    DataListScreen(
        title = "Participants",
        columns = participantColumns(),
        state = listState,
        itemKey = { it.id },
        onAction = { action -> onAction(action.toAdminDashboardAction()) },
        newButtonLabel = "Nouveau participant",
        detailContent = { participant ->
            ParticipantDetailContent(
                participant = participant,
                onClose = { onAction(AdminDashboardAction.OnDismissParticipantDetail) },
                showHeader = false,
                modifier = Modifier.fillMaxSize()
            )
        }
    )
}

private fun AdminDashboardState.toParticipantListState(): ListState<Participant> =
    ListState(
        items = filteredParticipants,
        isLoading = isLoading,
        errorMessage = loadError,
        selectedItem = selectedParticipant,
        isDetailPanelOpen = selectedParticipant != null && !showRegistrationForm,
        totalItems = filteredParticipants.size,
        totalPages = 1,
        currentPage = 1,
        itemsPerPage = filteredParticipants.size.coerceAtLeast(10),
        visibleColumns = setOf("fullName", "familyName", "birthDate", "age", "educationLevel", "registeredAt"),
        filters = if (searchQuery.isNotBlank()) mapOf("fullName" to searchQuery) else emptyMap()
    )

private fun ListAction<Participant>.toAdminDashboardAction(): AdminDashboardAction = when (this) {
    is ListAction.View -> AdminDashboardAction.OnParticipantSelected(item)
    is ListAction.Edit -> AdminDashboardAction.OnParticipantSelected(item)
    is ListAction.Delete -> AdminDashboardAction.OnDeleteParticipant(item.id)
    is ListAction.New -> AdminDashboardAction.OnOpenRegistrationForm
    is ListAction.CloseDetail -> AdminDashboardAction.OnDismissParticipantDetail
    is ListAction.FilterBy -> AdminDashboardAction.OnSearchChange(value)
    is ListAction.Export -> when (format) {
        ExportFormat.PDF -> AdminDashboardAction.OnExportPdf
        ExportFormat.CSV -> AdminDashboardAction.OnExportExcel
        ExportFormat.XLSX -> AdminDashboardAction.OnExportExcel
    }

    else -> AdminDashboardAction.OnSearchChange("")
}

@Composable
private fun participantColumns(): List<ColumnDef<Participant>> =
    listOf(
        ColumnDef(
            key = "fullName",
            header = "Prénom",
            render = { p ->
                Text(
                    p.fullName,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
            }
        ),
        ColumnDef(
            key = "familyName",
            header = "Nom",
            render = { p -> Text(p.familyName, style = MaterialTheme.typography.bodySmall) }
        ),
        ColumnDef(
            key = "birthDate",
            header = "Date de naissance",
            render = { p ->
                val formatted =
                    "%02d/%02d/%04d".format(p.birthDate.dayOfMonth, p.birthDate.monthNumber, p.birthDate.year)
                Text(formatted, style = MaterialTheme.typography.bodySmall)
            }
        ),
        ColumnDef(
            key = "age",
            header = "Âge",
            sortable = false,
            filterable = false,
            render = { p -> Text("${p.age} ans", style = MaterialTheme.typography.bodySmall) }
        ),
        ColumnDef(
            key = "educationLevel",
            header = "Niveau d'étude",
            render = { p -> Text(educationLevelLabel(p.educationLevel), style = MaterialTheme.typography.bodySmall) }
        ),
        ColumnDef(
            key = "registeredAt",
            header = "Inscrit le",
            render = { p -> Text(p.registeredAt, style = MaterialTheme.typography.bodySmall) }
        ),
    )

@Composable
private fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

private fun educationLevelLabel(level: EducationLevel): String = when (level) {
    EducationLevel.KINDERGARTEN -> "Maternelle"
    EducationLevel.PRIMARY -> "Primaire"
    EducationLevel.SECONDARY -> "Collège / Lycée"
    EducationLevel.HIGHER_WORKER -> "Étudiant / Travailleur"
}
