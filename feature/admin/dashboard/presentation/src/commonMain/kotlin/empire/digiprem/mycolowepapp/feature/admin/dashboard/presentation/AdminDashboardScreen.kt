package empire.digiprem.mycolowepapp.feature.admin.dashboard.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycolowepapp.core.design_system.currentDeviceConfigure
import empire.digiprem.mycolowepapp.core.theme.Amber
import empire.digiprem.mycolowepapp.core.theme.AmberContainer
import empire.digiprem.mycolowepapp.core.theme.Primary
import empire.digiprem.mycolowepapp.core.theme.PrimaryContainer
import empire.digiprem.mycolowepapp.core.theme.PrimaryDark
import empire.digiprem.mycolowepapp.core.theme.Secondary
import empire.digiprem.mycolowepapp.core.theme.SecondaryContainer
import empire.digiprem.mycolowepapp.core.ui.MyColoTonalButton
import empire.digiprem.mycolowepapp.core.ui.ParticipantChipStatus
import empire.digiprem.mycolowepapp.core.ui.StatusChip
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.ParticipantStatus
import empire.digiprem.mycolowepapp.feature.registration.domain.model.JobStatus
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.dashboard_by_age
import mycolowepapp.shared.generated.resources.dashboard_by_status
import mycolowepapp.shared.generated.resources.dashboard_export_excel
import mycolowepapp.shared.generated.resources.dashboard_export_pdf
import mycolowepapp.shared.generated.resources.dashboard_pending
import mycolowepapp.shared.generated.resources.dashboard_rejected
import mycolowepapp.shared.generated.resources.dashboard_title
import mycolowepapp.shared.generated.resources.dashboard_total
import mycolowepapp.shared.generated.resources.dashboard_validated
import mycolowepapp.shared.generated.resources.powered_by
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AdminDashboardScreen(
    onLogout: () -> Unit,
    viewModel: AdminDashboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isMobile = currentDeviceConfigure().isMobileDevice()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AdminDashboardEvent.OnLogout -> onLogout()
            }
        }
    }

    if (isMobile) {
        MobileDashboardLayout(state = state, onAction = viewModel::onAction)
    } else {
        DesktopDashboardLayout(state = state, onAction = viewModel::onAction)
    }
}

// ── Desktop ──────────────────────────────────────────────────────────────────

@Composable
private fun DesktopDashboardLayout(state: AdminDashboardState, onAction: (AdminDashboardAction) -> Unit) {
    Row(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        DashboardSidebar(onLogout = { onAction(AdminDashboardAction.OnLogoutClick) })

        Column(modifier = Modifier.weight(1f).fillMaxHeight().verticalScroll(rememberScrollState())) {
            DashboardTopBar(
                searchQuery = state.searchQuery,
                onSearchChange = { onAction(AdminDashboardAction.OnSearchChange(it)) }
            )
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = stringResource(Res.string.dashboard_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    KpiCard(label = stringResource(Res.string.dashboard_total), value = state.totalCount, color = Primary, backgroundColor = PrimaryContainer, modifier = Modifier.weight(1f))
                    KpiCard(label = stringResource(Res.string.dashboard_validated), value = state.validatedCount, color = Secondary, backgroundColor = SecondaryContainer, modifier = Modifier.weight(1f))
                    KpiCard(label = stringResource(Res.string.dashboard_pending), value = state.pendingCount, color = Amber, backgroundColor = AmberContainer, modifier = Modifier.weight(1f))
                    KpiCard(label = stringResource(Res.string.dashboard_rejected), value = state.rejectedCount, color = MaterialTheme.colorScheme.error, backgroundColor = MaterialTheme.colorScheme.errorContainer, modifier = Modifier.weight(1f))
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    BarChartCard(
                        title = stringResource(Res.string.dashboard_by_status),
                        modifier = Modifier.weight(1f),
                        items = state.byJobStatus.map { (status, count) ->
                            BarChartItem(label = jobStatusLabel(status), value = count, total = state.totalCount, color = Primary)
                        }
                    )
                    BarChartCard(
                        title = stringResource(Res.string.dashboard_by_age),
                        modifier = Modifier.weight(1f),
                        items = state.byAgeRange.map { (range, count) ->
                            BarChartItem(label = range, value = count, total = state.totalCount, color = Secondary)
                        }
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MyColoTonalButton(text = stringResource(Res.string.dashboard_export_pdf), onClick = { onAction(AdminDashboardAction.OnExportPdf) }, modifier = Modifier.weight(1f))
                    MyColoTonalButton(text = stringResource(Res.string.dashboard_export_excel), onClick = { onAction(AdminDashboardAction.OnExportExcel) }, modifier = Modifier.weight(1f))
                }

                ParticipantsTableCard(participants = state.filteredParticipants)

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Mobile ───────────────────────────────────────────────────────────────────

@Composable
private fun MobileDashboardLayout(state: AdminDashboardState, onAction: (AdminDashboardAction) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        MobileDashboardTopBar(onLogout = { onAction(AdminDashboardAction.OnLogoutClick) })

        Column(
            modifier = Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { onAction(AdminDashboardAction.OnSearchChange(it)) },
                placeholder = { Text("Rechercher un participant…") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Primary) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary, cursorColor = Primary),
                shape = RoundedCornerShape(12.dp)
            )

            Text(
                text = stringResource(Res.string.dashboard_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // KPI 2×2
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                KpiCard(label = stringResource(Res.string.dashboard_total), value = state.totalCount, color = Primary, backgroundColor = PrimaryContainer, modifier = Modifier.weight(1f))
                KpiCard(label = stringResource(Res.string.dashboard_validated), value = state.validatedCount, color = Secondary, backgroundColor = SecondaryContainer, modifier = Modifier.weight(1f))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                KpiCard(label = stringResource(Res.string.dashboard_pending), value = state.pendingCount, color = Amber, backgroundColor = AmberContainer, modifier = Modifier.weight(1f))
                KpiCard(label = stringResource(Res.string.dashboard_rejected), value = state.rejectedCount, color = MaterialTheme.colorScheme.error, backgroundColor = MaterialTheme.colorScheme.errorContainer, modifier = Modifier.weight(1f))
            }

            // Charts empilés
            BarChartCard(
                title = stringResource(Res.string.dashboard_by_status),
                modifier = Modifier.fillMaxWidth(),
                items = state.byJobStatus.map { (status, count) ->
                    BarChartItem(label = jobStatusLabel(status), value = count, total = state.totalCount, color = Primary)
                }
            )
            BarChartCard(
                title = stringResource(Res.string.dashboard_by_age),
                modifier = Modifier.fillMaxWidth(),
                items = state.byAgeRange.map { (range, count) ->
                    BarChartItem(label = range, value = count, total = state.totalCount, color = Secondary)
                }
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MyColoTonalButton(text = stringResource(Res.string.dashboard_export_pdf), onClick = { onAction(AdminDashboardAction.OnExportPdf) }, modifier = Modifier.weight(1f))
                MyColoTonalButton(text = stringResource(Res.string.dashboard_export_excel), onClick = { onAction(AdminDashboardAction.OnExportExcel) }, modifier = Modifier.weight(1f))
            }

            MobileParticipantsCard(participants = state.filteredParticipants)

            Text(
                text = stringResource(Res.string.powered_by),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MobileDashboardTopBar(onLogout: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(PrimaryDark).padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(32.dp).background(Color.White.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "MC", fontWeight = FontWeight.ExtraBold, color = Color.White, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "My Colo Admin",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
        TextButton(onClick = onLogout) {
            Icon(imageVector = Icons.Filled.Close, contentDescription = "Déconnexion", tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Quitter", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun MobileParticipantsCard(participants: List<Participant>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Liste des participants",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            participants.forEach { participant ->
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                        Text(
                            text = participant.fullName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${participant.age} ans · ${jobStatusLabel(participant.jobStatus)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    StatusChip(
                        status = when (participant.status) {
                            ParticipantStatus.VALIDATED -> ParticipantChipStatus.VALIDATED
                            ParticipantStatus.PENDING -> ParticipantChipStatus.PENDING
                            ParticipantStatus.REJECTED -> ParticipantChipStatus.REJECTED
                        }
                    )
                }
            }
            if (participants.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text(text = "Aucun participant trouvé", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

// ── Composants partagés ───────────────────────────────────────────────────────

@Composable
private fun DashboardSidebar(onLogout: () -> Unit) {
    Column(
        modifier = Modifier.widthIn(min = 200.dp, max = 240.dp).fillMaxHeight().background(PrimaryDark).padding(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 32.dp)) {
                Box(modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.2f), CircleShape), contentAlignment = Alignment.Center) {
                    Text(text = "MC", fontWeight = FontWeight.ExtraBold, color = Color.White, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "My Colo Admin", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color.White)
            }
            SidebarNavItem(icon = Icons.Filled.Home, label = "Home")
            SidebarNavItem(icon = Icons.Filled.Info, label = "Statistiques")
            SidebarNavItem(icon = Icons.Filled.AccountCircle, label = "Participants")
            SidebarNavItem(icon = Icons.Filled.Share, label = "Rapport PDF")
            SidebarNavItem(icon = Icons.Filled.List, label = "Export Excel")
            SidebarNavItem(icon = Icons.Filled.Settings, label = "Paramètres")
        }
        Column {
            TextButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = null, tint = Color.White.copy(alpha = 0.7f))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Déconnexion", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                text = stringResource(Res.string.powered_by),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun SidebarNavItem(icon: ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 8.dp)) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White.copy(alpha = 0.85f), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.85f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardTopBar(searchQuery: String, onSearchChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Rechercher un participant…") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Primary) },
            modifier = Modifier.weight(1f).height(52.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary, cursorColor = Primary),
            shape = RoundedCornerShape(12.dp)
        )
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Filled.Notifications, contentDescription = "Notifications", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Box(modifier = Modifier.size(40.dp).background(Primary, CircleShape), contentAlignment = Alignment.Center) {
            Icon(imageVector = Icons.Filled.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
private fun KpiCard(label: String, value: Int, color: Color, backgroundColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.Start) {
            Text(text = value.toString(), fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, style = MaterialTheme.typography.bodyMedium, color = color.copy(alpha = 0.8f))
        }
    }
}

private data class BarChartItem(val label: String, val value: Int, val total: Int, val color: Color)

@Composable
private fun BarChartCard(title: String, items: List<BarChartItem>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            items.forEach { item ->
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = item.label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "${item.value}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = item.color)
                    }
                    LinearProgressIndicator(
                        progress = { if (item.total > 0) item.value.toFloat() / item.total else 0f },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = item.color,
                        trackColor = item.color.copy(alpha = 0.15f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ParticipantsTableCard(participants: List<Participant>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Liste des participants",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)).padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Nom complet", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(2f))
                Text("Âge", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(0.5f))
                Text("Statut professionnel", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1.5f))
                Text("Inscription", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                Text("Statut", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
            }
            participants.forEach { participant ->
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(2f)) {
                        Text(participant.fullName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                        Text(participant.familyName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("${participant.age} ans", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(0.5f))
                    Text(
                        text = jobStatusLabel(participant.jobStatus),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1.5f)
                    )
                    Text(participant.registrationDate, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.weight(1f)) {
                        StatusChip(
                            status = when (participant.status) {
                                ParticipantStatus.VALIDATED -> ParticipantChipStatus.VALIDATED
                                ParticipantStatus.PENDING -> ParticipantChipStatus.PENDING
                                ParticipantStatus.REJECTED -> ParticipantChipStatus.REJECTED
                            }
                        )
                    }
                }
            }
            if (participants.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text(text = "Aucun participant trouvé", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

private fun jobStatusLabel(status: JobStatus): String = when (status) {
    JobStatus.STUDENT_SCHOOL -> "Élève"
    JobStatus.STUDENT_HIGHER -> "Étudiant"
    JobStatus.WORKER -> "Travailleur"
    JobStatus.SEEKING_WORK -> "En attente"
}
