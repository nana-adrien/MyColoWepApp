package empire.digiprem.mycolowepapp.feature.admin.dashboard.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import empire.digiprem.mycolowepapp.feature.registration.domain.model.RegistrationForm
import empire.digiprem.mycolowepapp.feature.registration.presentation.form.RegisterForm
import kotlinx.coroutines.launch
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

// ── Entry point ───────────────────────────────────────────────────────────────

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

    when {
        state.isLoading -> DashboardLoadingOverlay()
        state.loadError != null -> DashboardErrorPage(
            message = state.loadError!!,
            onRetry = { viewModel.onAction(AdminDashboardAction.OnRetryLoad) }
        )
        isMobile -> MobileDashboardLayout(state = state, onAction = viewModel::onAction)
        else     -> DesktopDashboardLayout(state = state, onAction = viewModel::onAction)
    }
}

// ── Desktop ───────────────────────────────────────────────────────────────────

@Composable
private fun DesktopDashboardLayout(state: AdminDashboardState, onAction: (AdminDashboardAction) -> Unit) {
    Row(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        DashboardSidebarContent(
            adminEmail      = state.adminEmail,
            currentSection  = state.currentSection,
            onSectionChange = { onAction(AdminDashboardAction.OnSectionChange(it)) },
            onLogout        = { onAction(AdminDashboardAction.OnLogoutClick) },
            modifier        = Modifier.widthIn(min = 200.dp, max = 240.dp).fillMaxHeight().background(PrimaryDark)
        )

        // Contenu principal selon la section active
        when (state.currentSection) {
            DashboardSection.Overview       -> DesktopOverviewContent(state = state, onAction = onAction)
            DashboardSection.Participants   -> ParticipantsManagementPage(state = state, onAction = onAction)
            DashboardSection.SecurityCodes  -> SecurityCodesPage()
            DashboardSection.Settings       -> SettingsPage(state = state, onAction = onAction)
        }
    }
}

@Composable
private fun DesktopOverviewContent(state: AdminDashboardState, onAction: (AdminDashboardAction) -> Unit) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f).fillMaxHeight().verticalScroll(rememberScrollState())) {
            DesktopDashboardTopBar(
                searchQuery           = state.searchQuery,
                onSearchChange        = { onAction(AdminDashboardAction.OnSearchChange(it)) },
                onAddParticipantClick = { onAction(AdminDashboardAction.OnOpenRegistrationForm) }
            )
            Column(
                modifier            = Modifier.fillMaxWidth().padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(stringResource(Res.string.dashboard_title), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    KpiCard(stringResource(Res.string.dashboard_total),     state.totalCount,     Primary,   PrimaryContainer,   Modifier.weight(1f))
                    KpiCard(stringResource(Res.string.dashboard_validated), state.validatedCount, Secondary, SecondaryContainer, Modifier.weight(1f))
                    KpiCard(stringResource(Res.string.dashboard_pending),   state.pendingCount,   Amber,     AmberContainer,     Modifier.weight(1f))
                    KpiCard(stringResource(Res.string.dashboard_rejected),  state.rejectedCount,  MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.errorContainer, Modifier.weight(1f))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    BarChartCard(stringResource(Res.string.dashboard_by_status), Modifier.weight(1f),
                        state.byJobStatus.map { (s, c) -> BarChartItem(jobStatusLabel(s), c, state.totalCount, Primary) })
                    BarChartCard(stringResource(Res.string.dashboard_by_age), Modifier.weight(1f),
                        state.byAgeRange.map { (r, c) -> BarChartItem(r, c, state.totalCount, Secondary) })
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MyColoTonalButton(stringResource(Res.string.dashboard_export_pdf),   { onAction(AdminDashboardAction.OnExportPdf) },   Modifier.weight(1f))
                    MyColoTonalButton(stringResource(Res.string.dashboard_export_excel), { onAction(AdminDashboardAction.OnExportExcel) }, Modifier.weight(1f))
                }
                ParticipantsTableCard(
                    participants        = state.filteredParticipants,
                    selectedParticipant = state.selectedParticipant,
                    onParticipantClick  = { onAction(AdminDashboardAction.OnParticipantSelected(it)) }
                )
                Text(stringResource(Res.string.powered_by), style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(Modifier.height(16.dp))
            }
        }
        AnimatedVisibility(
            visible = state.showRightPanel,
            enter   = slideInHorizontally(initialOffsetX = { it }),
            exit    = slideOutHorizontally(targetOffsetX = { it })
        ) {
            Box(
                modifier = Modifier.width(400.dp).fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(topStart = 0.dp))
            ) {
                if (state.showRegistrationForm) {

                    RegisterForm(
                        onSuccess = {

                        }
                    )
                  /*  ParticipantFormContent(form = state.registrationForm, onAction = onAction,
                        onClose = { onAction(AdminDashboardAction.OnDismissRegistrationForm) })*/
                } else {
                    state.selectedParticipant?.let {
                        ParticipantDetailContent(participant = it,
                            onClose = { onAction(AdminDashboardAction.OnDismissParticipantDetail) })
                    }
                }
            }
        }
    }
}

// ── Mobile ────────────────────────────────────────────────────────────────────

@Composable
private fun MobileDashboardLayout(state: AdminDashboardState, onAction: (AdminDashboardAction) -> Unit) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope       = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState   = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = PrimaryDark,
                modifier             = Modifier.widthIn(max = 280.dp)
            ) {
                DashboardSidebarContent(
                    adminEmail      = state.adminEmail,
                    currentSection  = state.currentSection,
                    onSectionChange = { section ->
                        onAction(AdminDashboardAction.OnSectionChange(section))
                        scope.launch { drawerState.close() }
                    },
                    onLogout = { onAction(AdminDashboardAction.OnLogoutClick) }
                )
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

            // Contenu principal selon section
            Column(modifier = Modifier.fillMaxSize()) {
                MobileDashboardTopBar(
                    onMenuClick           = { scope.launch { drawerState.open() } },
                    onAddParticipantClick = { onAction(AdminDashboardAction.OnOpenRegistrationForm) }
                )
                when (state.currentSection) {
                    DashboardSection.Participants   -> ParticipantsManagementPage(state = state, onAction = onAction)
                    DashboardSection.SecurityCodes  -> SecurityCodesPage()
                    DashboardSection.Settings       -> SettingsPage(state = state, onAction = onAction)
                    DashboardSection.Overview       ->
                        Column(
                            modifier            = Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            MobileSearchBar(state.searchQuery) { onAction(AdminDashboardAction.OnSearchChange(it)) }
                            Text(stringResource(Res.string.dashboard_title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                KpiCard(stringResource(Res.string.dashboard_total),     state.totalCount,     Primary,   PrimaryContainer,   Modifier.weight(1f))
                                KpiCard(stringResource(Res.string.dashboard_validated), state.validatedCount, Secondary, SecondaryContainer, Modifier.weight(1f))
                            }
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                KpiCard(stringResource(Res.string.dashboard_pending),  state.pendingCount,  Amber, AmberContainer, Modifier.weight(1f))
                                KpiCard(stringResource(Res.string.dashboard_rejected), state.rejectedCount, MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.errorContainer, Modifier.weight(1f))
                            }
                            BarChartCard(stringResource(Res.string.dashboard_by_status), Modifier.fillMaxWidth(),
                                state.byJobStatus.map { (s, c) -> BarChartItem(jobStatusLabel(s), c, state.totalCount, Primary) })
                            BarChartCard(stringResource(Res.string.dashboard_by_age), Modifier.fillMaxWidth(),
                                state.byAgeRange.map { (r, c) -> BarChartItem(r, c, state.totalCount, Secondary) })
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                MyColoTonalButton(stringResource(Res.string.dashboard_export_pdf),   { onAction(AdminDashboardAction.OnExportPdf) },   Modifier.weight(1f))
                                MyColoTonalButton(stringResource(Res.string.dashboard_export_excel), { onAction(AdminDashboardAction.OnExportExcel) }, Modifier.weight(1f))
                            }
                            MobileParticipantsCard(
                                participants       = state.filteredParticipants,
                                onParticipantClick = { onAction(AdminDashboardAction.OnParticipantSelected(it)) }
                            )
                            Text(stringResource(Res.string.powered_by), style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.align(Alignment.CenterHorizontally))
                            Spacer(Modifier.height(16.dp))
                        }
                }
            }

            // Overlay détail participant (slide depuis la droite)
            AnimatedVisibility(
                visible  = state.selectedParticipant != null,
                modifier = Modifier.fillMaxSize(),
                enter    = slideInHorizontally(initialOffsetX = { it }),
                exit     = slideOutHorizontally(targetOffsetX = { it })
            ) {
                state.selectedParticipant?.let { participant ->
                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            MobileDetailTopBar(
                                title   = "Détails",
                                onClose = { onAction(AdminDashboardAction.OnDismissParticipantDetail) }
                            )
                            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                                ParticipantDetailContent(
                                    participant = participant,
                                    onClose     = { onAction(AdminDashboardAction.OnDismissParticipantDetail) },
                                    showHeader  = false
                                )
                            }
                        }
                    }
                }
            }

            // Overlay formulaire (slide depuis le bas)
            AnimatedVisibility(
                visible  = state.showRegistrationForm,
                modifier = Modifier.fillMaxSize(),
                enter    = slideInVertically(initialOffsetY = { it }),
                exit     = slideOutVertically(targetOffsetY = { it })
            ) {
                Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        /*MobileDetailTopBar(
                            title   = "Nouveau participant",
                            onClose = { onAction(AdminDashboardAction.OnDismissRegistrationForm) }
                        )*/
                        RegisterForm(
                            onSuccess = {

                            }
                        )
                        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {

                            /*ParticipantFormContent(
                                form      = state.registrationForm,
                                onAction  = onAction,
                                onClose   = { onAction(AdminDashboardAction.OnDismissRegistrationForm) },
                                showHeader = false
                            )*/
                        }
                    }
                }
            }
        }
    }
}

// ── Top bars ──────────────────────────────────────────────────────────────────

@Composable
private fun DesktopDashboardTopBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onAddParticipantClick: () -> Unit
) {
    Row(
        modifier            = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment   = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value         = searchQuery,
            onValueChange = onSearchChange,
            placeholder   = { Text("Rechercher un participant…") },
            leadingIcon   = { Icon(Icons.Filled.Search, null, tint = Primary) },
            modifier      = Modifier.weight(1f).height(52.dp),
            singleLine    = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors        = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary, cursorColor = Primary),
            shape         = RoundedCornerShape(12.dp)
        )
        Button(
            onClick = onAddParticipantClick,
            colors  = ButtonDefaults.buttonColors(containerColor = Primary),
            shape   = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
            modifier = Modifier.height(52.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Ajouter", modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(6.dp))
            Text("Ajouter")
        }
        IconButton(onClick = {}) {
            Icon(Icons.Filled.Notifications, "Notifications", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Box(modifier = Modifier.size(40.dp).background(Primary, CircleShape), contentAlignment = Alignment.Center) {
            Icon(Icons.Filled.Person, null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
private fun MobileDashboardTopBar(onMenuClick: () -> Unit, onAddParticipantClick: () -> Unit) {
    Row(
        modifier          = Modifier.fillMaxWidth().background(PrimaryDark).padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onMenuClick) {
            Icon(Icons.Filled.Menu, "Menu", tint = Color.White)
        }
        Text(
            text       = "My Colo Admin",
            style      = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color      = Color.White,
            modifier   = Modifier.weight(1f)
        )
        IconButton(onClick = onAddParticipantClick) {
            Icon(Icons.Filled.Add, "Ajouter un participant", tint = Color.White)
        }
    }
}

@Composable
private fun MobileDetailTopBar(title: String, onClose: () -> Unit) {
    Row(
        modifier          = Modifier.fillMaxWidth().background(PrimaryDark).padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, "Fermer", tint = Color.White)
        }
        Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
private fun MobileSearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value         = query,
        onValueChange = onQueryChange,
        placeholder   = { Text("Rechercher un participant…") },
        leadingIcon   = { Icon(Icons.Filled.Search, null, tint = Primary) },
        modifier      = Modifier.fillMaxWidth().height(52.dp),
        singleLine    = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors        = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary, cursorColor = Primary),
        shape         = RoundedCornerShape(12.dp)
    )
}

// ── Sidebar (partagée entre desktop et drawer mobile) ────────────────────────

@Composable
private fun DashboardSidebarContent(
    adminEmail: String,
    currentSection: DashboardSection,
    onSectionChange: (DashboardSection) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier            = modifier.fillMaxHeight().padding(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 32.dp)) {
                Box(
                    modifier         = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) { Text("MC", fontWeight = FontWeight.ExtraBold, color = Color.White, fontSize = 14.sp) }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("My Colo Admin", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color.White)
                    if (adminEmail.isNotEmpty()) {
                        Text(adminEmail, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.6f), maxLines = 1)
                    }
                }
            }
            SidebarNavItem(
                icon     = Icons.Filled.Home,
                label    = "Vue d'ensemble",
                selected = currentSection == DashboardSection.Overview,
                onClick  = { onSectionChange(DashboardSection.Overview) }
            )
            SidebarNavItem(
                icon     = Icons.Filled.AccountCircle,
                label    = "Participants",
                selected = currentSection == DashboardSection.Participants,
                onClick  = { onSectionChange(DashboardSection.Participants) }
            )
            SidebarNavItem(
                icon     = Icons.Filled.Lock,
                label    = "Codes de sécurité",
                selected = currentSection == DashboardSection.SecurityCodes,
                onClick  = { onSectionChange(DashboardSection.SecurityCodes) }
            )
            SidebarNavItem(
                icon     = Icons.Filled.Settings,
                label    = "Paramètres",
                selected = currentSection == DashboardSection.Settings,
                onClick  = { onSectionChange(DashboardSection.Settings) }
            )
        }
        Column {
            TextButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.Close, null, tint = Color.White.copy(alpha = 0.7f))
                Spacer(Modifier.width(8.dp))
                Text("Déconnexion", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                text     = stringResource(Res.string.powered_by),
                style    = MaterialTheme.typography.bodySmall,
                color    = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun SidebarNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val bgColor    = if (selected) Color.White.copy(alpha = 0.15f) else Color.Transparent
    val textColor  = if (selected) Color.White else Color.White.copy(alpha = 0.7f)
    val iconColor  = if (selected) Color.White else Color.White.copy(alpha = 0.7f)
    val fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier          = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 12.dp)
    ) {
        Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium, color = textColor, fontWeight = fontWeight)
    }
}

// ── Panneau détail participant ────────────────────────────────────────────────

@Composable
fun ParticipantDetailContent(
    participant: Participant,
    onClose: () -> Unit,
    showHeader: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier            = modifier.fillMaxWidth().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        if (showHeader) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("Détails du participant", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                IconButton(onClick = onClose) { Icon(Icons.Filled.Close, "Fermer") }
            }
            Spacer(Modifier.height(16.dp))
        }

        // Avatar
        Box(
            modifier         = Modifier.size(72.dp).background(Primary, CircleShape).align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = participant.fullName.take(2).uppercase(),
                color      = Color.White,
                fontSize   = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(participant.fullName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally))
        Text(participant.familyName, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(Modifier.height(24.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(Modifier.height(16.dp))

        DetailInfoRow("Âge",                  "${participant.age} ans")
        DetailInfoRow("Statut professionnel", jobStatusLabel(participant.jobStatus))
        DetailInfoRow("Date d'inscription",   participant.registrationDate)

        Spacer(Modifier.height(8.dp))
        Row(
            modifier          = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Statut", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
            StatusChip(status = when (participant.status) {
                ParticipantStatus.VALIDATED -> ParticipantChipStatus.VALIDATED
                ParticipantStatus.PENDING   -> ParticipantChipStatus.PENDING
                ParticipantStatus.REJECTED  -> ParticipantChipStatus.REJECTED
            })
        }
    }
}

@Composable
private fun DetailInfoRow(label: String, value: String) {
    Row(
        modifier          = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
    }
}

// ── Panneau formulaire d'enregistrement (même structure visuelle) ─────────────

@Composable
fun ParticipantFormContent(
    form: ParticipantFormData,
    onAction: (AdminDashboardAction) -> Unit,
    onClose: () -> Unit,
    showHeader: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier            = modifier.fillMaxWidth().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (showHeader) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("Nouveau participant", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                IconButton(onClick = onClose) { Icon(Icons.Filled.Close, "Fermer") }
            }
        }

        // Avatar placeholder
        Box(
            modifier         = Modifier.size(72.dp).background(MaterialTheme.colorScheme.surfaceVariant, CircleShape).align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Person, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(40.dp))
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        OutlinedTextField(
            value         = form.fullName,
            onValueChange = { onAction(AdminDashboardAction.OnFormFullNameChange(it)) },
            label         = { Text("Prénom *") },
            modifier      = Modifier.fillMaxWidth(),
            singleLine    = true,
            colors        = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary, focusedLabelColor = Primary, cursorColor = Primary)
        )
        OutlinedTextField(
            value         = form.familyName,
            onValueChange = { onAction(AdminDashboardAction.OnFormFamilyNameChange(it)) },
            label         = { Text("Nom de famille *") },
            modifier      = Modifier.fillMaxWidth(),
            singleLine    = true,
            colors        = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary, focusedLabelColor = Primary, cursorColor = Primary)
        )
        OutlinedTextField(
            value           = form.age,
            onValueChange   = { onAction(AdminDashboardAction.OnFormAgeChange(it)) },
            label           = { Text("Âge *") },
            modifier        = Modifier.fillMaxWidth(),
            singleLine      = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors          = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary, focusedLabelColor = Primary, cursorColor = Primary)
        )

        // Sélection du statut professionnel
        Text("Statut professionnel *", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            JobStatus.entries.chunked(2).forEach { rowItems ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    rowItems.forEach { jobStatus ->
                        FilterChip(
                            selected = form.selectedJobStatus == jobStatus,
                            onClick  = { onAction(AdminDashboardAction.OnFormJobStatusChange(jobStatus)) },
                            label    = { Text(jobStatusLabel(jobStatus), style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.weight(1f),
                            colors   = FilterChipDefaults.filterChipColors(selectedContainerColor = Primary, selectedLabelColor = Color.White)
                        )
                    }
                    if (rowItems.size < 2) Spacer(Modifier.weight(1f))
                }
            }
        }

        form.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        if (form.isSubmitting) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Primary)
            }
        } else {
            Button(
                onClick  = { onAction(AdminDashboardAction.OnSubmitRegistrationForm) },
                modifier = Modifier.fillMaxWidth(),
                enabled  = form.isValid,
                colors   = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("Enregistrer le participant")
            }
        }
    }
}

// ── KPI & Charts ─────────────────────────────────────────────────────────────

@Composable
private fun KpiCard(label: String, value: Int, color: Color, backgroundColor: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.Start) {
            Text(value.toString(), fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Spacer(Modifier.height(4.dp))
            Text(label, style = MaterialTheme.typography.bodyMedium, color = color.copy(alpha = 0.8f))
        }
    }
}

private data class BarChartItem(val label: String, val value: Int, val total: Int, val color: Color)

@Composable
private fun BarChartCard(title: String, modifier: Modifier = Modifier, items: List<BarChartItem>) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            items.forEach { item ->
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(item.label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${item.value}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = item.color)
                    }
                    LinearProgressIndicator(
                        progress  = { if (item.total > 0) item.value.toFloat() / item.total else 0f },
                        modifier  = Modifier.fillMaxWidth().height(8.dp),
                        color     = item.color,
                        trackColor = item.color.copy(alpha = 0.15f)
                    )
                }
            }
        }
    }
}

// ── Listes participants ───────────────────────────────────────────────────────

@Composable
private fun ParticipantsTableCard(
    participants: List<Participant>,
    selectedParticipant: Participant?,
    onParticipantClick: (Participant) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Liste des participants", style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 12.dp))
            // En-têtes
            Row(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)).padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text("Nom complet",           style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(2f))
                Text("Âge",                   style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(0.5f))
                Text("Statut professionnel",  style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1.5f))
                Text("Inscription",           style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                Text("Statut",                style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
            }
            participants.forEach { participant ->
                val isSelected = participant.id == selectedParticipant?.id
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
                Row(
                    modifier          = Modifier.fillMaxWidth()
                        .background(if (isSelected) Primary.copy(alpha = 0.08f) else Color.Transparent)
                        .clickable { onParticipantClick(participant) }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(2f)) {
                        Text(participant.fullName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                        Text(participant.familyName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("${participant.age} ans", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.5f))
                    Text(jobStatusLabel(participant.jobStatus), style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1.5f))
                    Text(participant.registrationDate, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.weight(1f)) {
                        StatusChip(status = when (participant.status) {
                            ParticipantStatus.VALIDATED -> ParticipantChipStatus.VALIDATED
                            ParticipantStatus.PENDING   -> ParticipantChipStatus.PENDING
                            ParticipantStatus.REJECTED  -> ParticipantChipStatus.REJECTED
                        })
                    }
                }
            }
            if (participants.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("Aucun participant trouvé", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun MobileParticipantsCard(participants: List<Participant>, onParticipantClick: (Participant) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Liste des participants", style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 8.dp))
            participants.forEach { participant ->
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
                Row(
                    modifier          = Modifier.fillMaxWidth()
                        .clickable { onParticipantClick(participant) }
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                        Text(participant.fullName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                        Text("${participant.age} ans · ${jobStatusLabel(participant.jobStatus)}",
                            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    StatusChip(status = when (participant.status) {
                        ParticipantStatus.VALIDATED -> ParticipantChipStatus.VALIDATED
                        ParticipantStatus.PENDING   -> ParticipantChipStatus.PENDING
                        ParticipantStatus.REJECTED  -> ParticipantChipStatus.REJECTED
                    })
                }
            }
            if (participants.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text("Aucun participant trouvé", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

// ── États loading/erreur ──────────────────────────────────────────────────────

@Composable
private fun DashboardLoadingOverlay() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            CircularProgressIndicator(color = Primary)
            Text("Chargement des participants…", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun DashboardErrorPage(message: String, onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
        Card(modifier = Modifier.padding(32.dp).widthIn(max = 480.dp), shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)) {
            Column(Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("⚠️", fontSize = 48.sp)
                Text("Chargement impossible", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onErrorContainer)
                Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onErrorContainer)
                Button(onClick = onRetry) { Text("Réessayer") }
            }
        }
    }
}

// ── Utilitaire ────────────────────────────────────────────────────────────────

private fun jobStatusLabel(status: JobStatus): String = when (status) {
    JobStatus.STUDENT_SCHOOL -> "Élève"
    JobStatus.STUDENT_HIGHER -> "Étudiant"
    JobStatus.WORKER         -> "Travailleur"
    JobStatus.SEEKING_WORK   -> "En recherche"
}
