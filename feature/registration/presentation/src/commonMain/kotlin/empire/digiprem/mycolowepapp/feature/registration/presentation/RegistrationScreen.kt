package empire.digiprem.mycolowepapp.feature.registration.presentation

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycolowepapp.core.design_system.currentDeviceConfigure
import empire.digiprem.mycolowepapp.core.theme.Primary
import empire.digiprem.mycolowepapp.core.theme.PrimaryContainer
import empire.digiprem.mycolowepapp.core.theme.PrimaryDark
import empire.digiprem.mycolowepapp.core.ui.MyColoFilledButton
import empire.digiprem.mycolowepapp.core.ui.MyColoTextField
import empire.digiprem.mycolowepapp.core.ui.SecurityCodeField
import empire.digiprem.mycolowepapp.feature.registration.domain.model.JobStatus
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.form_age
import mycolowepapp.shared.generated.resources.form_family_name
import mycolowepapp.shared.generated.resources.form_full_name
import mycolowepapp.shared.generated.resources.form_job_seeking
import mycolowepapp.shared.generated.resources.form_job_status
import mycolowepapp.shared.generated.resources.form_job_student_higher
import mycolowepapp.shared.generated.resources.form_job_student_school
import mycolowepapp.shared.generated.resources.form_job_worker
import mycolowepapp.shared.generated.resources.form_submit
import mycolowepapp.shared.generated.resources.form_title
import mycolowepapp.shared.generated.resources.powered_by
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToConfirmation: (String) -> Unit,
    viewModel: RegistrationViewModel = koinViewModel()
) {
    val state    by viewModel.state.collectAsStateWithLifecycle()
    val isMobile = currentDeviceConfigure().isCompact()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is RegistrationEvent.OnRegistrationSuccess ->
                    onNavigateToConfirmation(event.referenceNumber)
            }
        }
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.onAction(RegistrationAction.OnClearError)
        }
    }

    if (isMobile) {
        MobileRegistrationLayout(
            state             = state,
            onAction          = viewModel::onAction,
            snackbarHostState = snackbarHostState,
            onNavigateBack    = onNavigateBack
        )
    } else {
        DesktopRegistrationLayout(
            state             = state,
            onAction          = viewModel::onAction,
            snackbarHostState = snackbarHostState,
            onNavigateBack    = onNavigateBack
        )
    }
}

// ── Mobile — Scaffold + TopAppBar ─────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MobileRegistrationLayout(
    state: RegistrationState,
    onAction: (RegistrationAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.form_title), fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor          = Primary,
                    titleContentColor       = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        snackbarHost   = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        RegistrationFormContent(
            state    = state,
            onAction = onAction,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
        )
    }
}

// ── Desktop — split 40/60 ─────────────────────────────────────────────────────

@Composable
private fun DesktopRegistrationLayout(
    state: RegistrationState,
    onAction: (RegistrationAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize()) {

        // Panneau gauche — info colonie
        Box(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight()
                .background(brush = Brush.verticalGradient(listOf(PrimaryDark, Primary)))
                .padding(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Box(
                    modifier         = Modifier.size(72.dp).background(Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("MC", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }
                Text(
                    "My Colo",
                    fontSize   = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = Color.White,
                    textAlign  = TextAlign.Center
                )
                Text(
                    "Inscription à la colonie",
                    style     = MaterialTheme.typography.titleSmall,
                    color     = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))

                listOf(
                    "Remplissez le formulaire en quelques minutes",
                    "Un code de référence vous sera attribué",
                    "Votre inscription sera examinée par l'administration"
                ).forEach { info ->
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier          = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier         = Modifier.size(20.dp).background(Color.White.copy(alpha = 0.25f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(12.dp))
                        }
                        Text(
                            text      = info,
                            style     = MaterialTheme.typography.bodySmall,
                            color     = Color.White.copy(alpha = 0.9f),
                            modifier  = Modifier.padding(start = 10.dp)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))
                Text(
                    stringResource(Res.string.powered_by),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.45f)
                )
            }
        }

        // Panneau droit — formulaire
        Column(
            modifier              = Modifier
                .weight(0.6f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 48.dp, vertical = 32.dp),
            horizontalAlignment   = Alignment.CenterHorizontally,
            verticalArrangement   = Arrangement.Center
        ) {
            // Bouton retour discret
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onNavigateBack) {
                    Icon(Icons.Filled.ArrowBack, null, modifier = Modifier.size(16.dp), tint = Primary)
                    Text(" Retour", style = MaterialTheme.typography.labelMedium, color = Primary)
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                stringResource(Res.string.form_title),
                style      = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "Complétez tous les champs pour soumettre votre candidature.",
                style    = MaterialTheme.typography.bodySmall,
                color    = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
            )

            RegistrationFormContent(
                state    = state,
                onAction = onAction,
                modifier = Modifier.widthIn(max = 520.dp).fillMaxWidth()
            )
        }
    }
}

// ── Contenu du formulaire (partagé mobile + desktop) ─────────────────────────

@Composable
private fun RegistrationFormContent(
    state: RegistrationState,
    onAction: (RegistrationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MyColoTextField(
            label         = stringResource(Res.string.form_full_name),
            value         = state.form.fullName,
            onValueChange = { onAction(RegistrationAction.OnFullNameChange(it)) }
        )
        MyColoTextField(
            label         = stringResource(Res.string.form_age),
            value         = state.form.age,
            onValueChange = { onAction(RegistrationAction.OnAgeChange(it)) },
            keyboardType  = KeyboardType.Number
        )
        MyColoTextField(
            label         = stringResource(Res.string.form_family_name),
            value         = state.form.familyName,
            onValueChange = { onAction(RegistrationAction.OnFamilyNameChange(it)) }
        )

        Text(
            text       = stringResource(Res.string.form_job_status),
            style      = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color      = MaterialTheme.colorScheme.onSurface
        )
        JobStatusGrid(
            selectedStatus   = state.form.jobStatus,
            onStatusSelected = { onAction(RegistrationAction.OnJobStatusChange(it)) }
        )

        Spacer(Modifier.height(4.dp))
        SecurityCodeField(
            value         = state.form.securityCode,
            onValueChange = { onAction(RegistrationAction.OnSecurityCodeChange(it)) }
        )
        Spacer(Modifier.height(4.dp))

        if (state.isLoading) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Primary)
            }
        } else {
            MyColoFilledButton(
                text    = stringResource(Res.string.form_submit),
                onClick = { onAction(RegistrationAction.OnSubmitClick) },
                enabled = !state.isLoading
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

// ── Grille de statuts professionnels ─────────────────────────────────────────

@Composable
private fun JobStatusGrid(
    selectedStatus: JobStatus?,
    onStatusSelected: (JobStatus) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            JobStatusCard(
                label      = stringResource(Res.string.form_job_student_school),
                isSelected = selectedStatus == JobStatus.STUDENT_SCHOOL,
                onClick    = { onStatusSelected(JobStatus.STUDENT_SCHOOL) },
                modifier   = Modifier.weight(1f)
            )
            JobStatusCard(
                label      = stringResource(Res.string.form_job_student_higher),
                isSelected = selectedStatus == JobStatus.STUDENT_HIGHER,
                onClick    = { onStatusSelected(JobStatus.STUDENT_HIGHER) },
                modifier   = Modifier.weight(1f)
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            JobStatusCard(
                label      = stringResource(Res.string.form_job_worker),
                isSelected = selectedStatus == JobStatus.WORKER,
                onClick    = { onStatusSelected(JobStatus.WORKER) },
                modifier   = Modifier.weight(1f)
            )
            JobStatusCard(
                label      = stringResource(Res.string.form_job_seeking),
                isSelected = selectedStatus == JobStatus.SEEKING_WORK,
                onClick    = { onStatusSelected(JobStatus.SEEKING_WORK) },
                modifier   = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun JobStatusCard(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = if (isSelected) Primary else MaterialTheme.colorScheme.outlineVariant
    val bgColor     = if (isSelected) PrimaryContainer else MaterialTheme.colorScheme.surface
    val textColor   = if (isSelected) Primary else MaterialTheme.colorScheme.onSurface

    Card(
        modifier  = modifier
            .height(72.dp)
            .clickable(onClick = onClick)
            .border(if (isSelected) 2.dp else 1.dp, borderColor, RoundedCornerShape(12.dp)),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier              = Modifier.fillMaxSize().padding(horizontal = 12.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text       = label,
                style      = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color      = textColor,
                modifier   = Modifier.weight(1f)
            )
            if (isSelected) {
                Box(
                    modifier         = Modifier.size(20.dp).background(Primary, RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}
