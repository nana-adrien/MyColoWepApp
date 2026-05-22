package empire.digiprem.mycolowepapp.feature.admin.dashboard.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material.icons.outlined.TableChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import empire.digiprem.mycolowepapp.core.design_system.currentDeviceConfigure
import empire.digiprem.mycolowepapp.core.theme.Primary
import empire.digiprem.mycolowepapp.core.theme.Secondary
import empire.digiprem.mycolowepapp.core.ui.ParticipantChipStatus
import empire.digiprem.mycolowepapp.core.ui.StatusChip
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.ParticipantStatus
import empire.digiprem.mycolowepapp.feature.registration.domain.model.EducationLevel

@Composable
internal fun ParticipantsManagementPage(
    state: AdminDashboardState,
    onAction: (AdminDashboardAction) -> Unit,
) {
    val isMobile = currentDeviceConfigure().isCompact()

    if (isMobile) {
        MobileParticipantsManagement(state = state, onAction = onAction)
    } else {
        DesktopParticipantsManagement(state = state, onAction = onAction)
    }
}

// ── Desktop ───────────────────────────────────────────────────────────────────

@Composable
private fun DesktopParticipantsManagement(
    state: AdminDashboardState,
    onAction: (AdminDashboardAction) -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize()) {

        // Contenu principal
        Column(
            modifier = Modifier.weight(1f).fillMaxHeight().verticalScroll(rememberScrollState())
        ) {
            ParticipantsToolbar(
                searchQuery = state.searchQuery,
                onSearchChange = { onAction(AdminDashboardAction.OnSearchChange(it)) },
                onAddClick     = { onAction(AdminDashboardAction.OnOpenRegistrationForm) },
                onExportPdf    = { onAction(AdminDashboardAction.OnExportPdf) },
                onExportExcel  = { onAction(AdminDashboardAction.OnExportExcel) },
            )
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Gestion des participants (${state.filteredParticipants.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                ParticipantsManagementTable(
                    participants        = state.filteredParticipants,
                    selectedParticipant = state.selectedParticipant,
                    onParticipantClick  = { onAction(AdminDashboardAction.OnParticipantSelected(it)) },
                    onValidate          = { onAction(AdminDashboardAction.OnValidateParticipant(it)) },
                    onReject            = { onAction(AdminDashboardAction.OnRejectParticipant(it)) },
                    onDelete            = { onAction(AdminDashboardAction.OnDeleteParticipant(it)) },
                )
                Spacer(Modifier.height(24.dp))
            }
        }

        // Panneau droit : détail ou formulaire
        AnimatedVisibility(
            visible = state.showRightPanel,
            enter   = slideInHorizontally(initialOffsetX = { it }),
            exit    = slideOutHorizontally(targetOffsetX = { it })
        ) {
            Box(
                modifier = Modifier
                    .width(360.dp).fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(topStart = 0.dp))
            ) {
                if (state.showRegistrationForm) {
                    ParticipantFormContent(
                        form     = state.registrationForm,
                        onAction = onAction,
                        onClose  = { onAction(AdminDashboardAction.OnDismissRegistrationForm) }
                    )
                } else {
                    state.selectedParticipant?.let {
                        ParticipantDetailContent(
                            participant = it,
                            onClose     = { onAction(AdminDashboardAction.OnDismissParticipantDetail) }
                        )
                    }
                }
            }
        }
    }
}

// ── Mobile ────────────────────────────────────────────────────────────────────

@Composable
private fun MobileParticipantsManagement(
    state: AdminDashboardState,
    onAction: (AdminDashboardAction) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.selectedParticipant != null && !state.showRegistrationForm) {
            ParticipantDetailContent(
                participant = state.selectedParticipant,
                onClose     = { onAction(AdminDashboardAction.OnDismissParticipantDetail) },
                showHeader  = true,
                modifier    = Modifier.fillMaxSize().padding(16.dp)
            )
        } else if (state.showRegistrationForm) {
            ParticipantFormContent(
                form     = state.registrationForm,
                onAction = onAction,
                onClose  = { onAction(AdminDashboardAction.OnDismissRegistrationForm) }
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                ParticipantsToolbar(
                    searchQuery   = state.searchQuery,
                    onSearchChange = { onAction(AdminDashboardAction.OnSearchChange(it)) },
                    onAddClick     = { onAction(AdminDashboardAction.OnOpenRegistrationForm) },
                    onExportPdf    = { onAction(AdminDashboardAction.OnExportPdf) },
                    onExportExcel  = { onAction(AdminDashboardAction.OnExportExcel) },
                )
                Column(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                        .verticalScroll(rememberScrollState()).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MobileParticipantManagementList(
                        participants = state.filteredParticipants,
                        onSelect     = { onAction(AdminDashboardAction.OnParticipantSelected(it)) },
                        onValidate   = { onAction(AdminDashboardAction.OnValidateParticipant(it)) },
                        onReject     = { onAction(AdminDashboardAction.OnRejectParticipant(it)) },
                        onDelete     = { onAction(AdminDashboardAction.OnDeleteParticipant(it)) },
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

// ── Toolbar ───────────────────────────────────────────────────────────────────

@Composable
private fun ParticipantsToolbar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onExportPdf: () -> Unit,
    onExportExcel: () -> Unit,
) {
    Row(
        modifier          = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value         = searchQuery,
            onValueChange = onSearchChange,
            modifier      = Modifier.weight(1f).height(48.dp),
            placeholder   = { Text("Rechercher…", style = MaterialTheme.typography.bodySmall) },
            leadingIcon   = { Icon(Icons.Filled.Search, null, modifier = Modifier.size(18.dp)) },
            singleLine    = true,
            shape         = RoundedCornerShape(12.dp),
            textStyle     = MaterialTheme.typography.bodySmall,
            colors        = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary, cursorColor = Primary)
        )

        // Ajouter
        IconButton(
            onClick  = onAddClick,
            modifier = Modifier.size(40.dp).background(Primary, CircleShape)
        ) {
            Icon(Icons.Filled.Add, "Ajouter un participant", tint = Color.White, modifier = Modifier.size(20.dp))
        }

        // Export PDF
        IconButton(
            onClick  = onExportPdf,
            modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(10.dp))
        ) {
            Icon(Icons.Outlined.PictureAsPdf, "Exporter PDF", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(20.dp))
        }

        // Export Excel
        IconButton(
            onClick  = onExportExcel,
            modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(10.dp))
        ) {
            Icon(Icons.Outlined.TableChart, "Exporter Excel", tint = Secondary, modifier = Modifier.size(20.dp))
        }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
}

// ── Table desktop ─────────────────────────────────────────────────────────────

@Composable
private fun ParticipantsManagementTable(
    participants: List<Participant>,
    selectedParticipant: Participant?,
    onParticipantClick: (Participant) -> Unit,
    onValidate: (String) -> Unit,
    onReject: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // En-têtes
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text("#",             style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(0.35f))
                Text("Nom complet",   style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1.8f))
                Text("Naissance",     style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                Text("Âge",           style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(0.55f))
                Text("Niveau d'étude",style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1.4f))
                Text("Situation",     style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1.2f))
                Text("Actions",       style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(0.8f))
            }

            if (participants.isEmpty()) {
                EmptyParticipantsState()
            } else {
                participants.forEachIndexed { index, p ->
                    val isSelected = p.id == selectedParticipant?.id
                    val birthFormatted = "%02d/%02d/%04d".format(
                        p.birthDate.dayOfMonth, p.birthDate.monthNumber, p.birthDate.year
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
                    Row(
                        modifier          = Modifier.fillMaxWidth()
                            .background(if (isSelected) Primary.copy(alpha = 0.06f) else Color.Transparent)
                            .clickable { onParticipantClick(p) }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${index + 1}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(0.35f))
                        Column(modifier = Modifier.weight(1.8f)) {
                            Text(p.fullName,   style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                            Text(p.familyName, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(birthFormatted,              style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                        Text("${p.age} ans",               style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(0.55f))
                        Text(p.educationLevel.ifBlank { "—" }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1.4f))
                        Text(educationLevelLabel(p.educationLevel),  style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1.2f))
                        Row(modifier = Modifier.weight(0.8f), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(onClick = { onDelete(p.id) }, modifier = Modifier.size(32.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)) {
                                Icon(Icons.Filled.Delete, "Supprimer", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Liste mobile ──────────────────────────────────────────────────────────────

@Composable
private fun MobileParticipantManagementList(
    participants: List<Participant>,
    onSelect: (Participant) -> Unit,
    onValidate: (String) -> Unit,
    onReject: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    if (participants.isEmpty()) {
        EmptyParticipantsState()
        return
    }
    participants.forEachIndexed { index, p ->
        val birthFormatted = "%02d/%02d/%04d".format(
            p.birthDate.dayOfMonth, p.birthDate.monthNumber, p.birthDate.year
        )
        Card(
            modifier  = Modifier.fillMaxWidth(),
            shape     = RoundedCornerShape(12.dp),
            colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier          = Modifier.fillMaxWidth().clickable { onSelect(p) }.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("#${index + 1}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(p.fullName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    }
                    Text(p.familyName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "Né(e) le $birthFormatted · ${p.age} ans · ${educationLevelLabel(p.educationLevel)}",
                        style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (p.educationLevel.isNotBlank()) {
                        Text(p.educationLevel, style = MaterialTheme.typography.labelSmall, color = Primary)
                    }
                }
                IconButton(onClick = { onDelete(p.id) }, modifier = Modifier.size(36.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)) {
                    Icon(Icons.Filled.Delete, "Supprimer", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

@Composable
private fun EmptyParticipantsState() {
    Box(Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("👥", style = MaterialTheme.typography.displaySmall)
            Text("Aucun participant", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Cliquez sur + pour en ajouter un.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

private fun ParticipantStatus.toChipStatus(): ParticipantChipStatus = when (this) {
    ParticipantStatus.VALIDATED -> ParticipantChipStatus.VALIDATED
    ParticipantStatus.PENDING   -> ParticipantChipStatus.PENDING
    ParticipantStatus.REJECTED  -> ParticipantChipStatus.REJECTED
}

private fun educationLevelLabel(level: EducationLevel): String = when (level) {
    EducationLevel.KINDERGARTEN  -> "Maternelle"
    EducationLevel.PRIMARY       -> "Primaire"
    EducationLevel.SECONDARY     -> "Collège / Lycée"
    EducationLevel.HIGHER_WORKER -> "Étudiant / Travailleur"
}
