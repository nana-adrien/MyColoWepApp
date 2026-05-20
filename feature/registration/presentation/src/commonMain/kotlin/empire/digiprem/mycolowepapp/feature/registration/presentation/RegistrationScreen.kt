package empire.digiprem.mycolowepapp.feature.registration.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycolowepapp.core.design_system.MyColoButton
import empire.digiprem.mycolowepapp.core.design_system.WebFormPageScaffold
import empire.digiprem.mycolowepapp.core.design_system.WebPageScaffold
import empire.digiprem.mycolowepapp.core.design_system.components.MyColoFooter
import empire.digiprem.mycolowepapp.core.design_system.components.MyColoNavBar
import empire.digiprem.mycolowepapp.core.design_system.components.NavItem
import empire.digiprem.mycolowepapp.core.design_system.components.form.BirthDatePickerField
import empire.digiprem.mycolowepapp.core.design_system.components.form.FieldBoxDecorator
import empire.digiprem.mycolowepapp.core.design_system.components.form.FieldHeader
import empire.digiprem.mycolowepapp.core.design_system.components.form.MyColoSelectItemTextField
import empire.digiprem.mycolowepapp.core.design_system.components.form.MyColoTextField
import empire.digiprem.mycolowepapp.core.design_system.components.form.SearchTextField
import empire.digiprem.mycolowepapp.core.design_system.components.form.SecurityCodeTextField
import empire.digiprem.mycolowepapp.core.design_system.components.form.SelectableField
import empire.digiprem.mycolowepapp.core.design_system.currentDeviceConfigure
import empire.digiprem.mycolowepapp.core.design_system.extension.asString
import empire.digiprem.mycolowepapp.core.theme.Primary
import empire.digiprem.mycolowepapp.core.theme.PrimaryContainer
import empire.digiprem.mycolowepapp.core.theme.PrimaryDark
import empire.digiprem.mycolowepapp.core.ui.currentTimeMillis
import empire.digiprem.mycolowepapp.feature.registration.domain.model.Genre
import empire.digiprem.mycolowepapp.feature.registration.domain.model.JobStatus
import empire.digiprem.mycolowepapp.feature.registration.presentation.form.RegisterForm
import empire.digiprem.mycolowepapp.feature.registration.presentation.form.RegisterFormAction
import kotlinx.coroutines.launch
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.form_age
import mycolowepapp.shared.generated.resources.form_family_name
import mycolowepapp.shared.generated.resources.form_full_name
import mycolowepapp.shared.generated.resources.form_job_seeking
import mycolowepapp.shared.generated.resources.form_job_student_higher
import mycolowepapp.shared.generated.resources.form_job_student_school
import mycolowepapp.shared.generated.resources.form_job_worker
import mycolowepapp.shared.generated.resources.form_security_code
import mycolowepapp.shared.generated.resources.form_submit
import mycolowepapp.shared.generated.resources.form_title
import mycolowepapp.shared.generated.resources.landing_nav_contact
import mycolowepapp.shared.generated.resources.landing_nav_program
import mycolowepapp.shared.generated.resources.powered_by
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.enums.EnumEntries

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToConfirmation: (String) -> Unit,
    viewModel: RegistrationViewModel = koinViewModel<RegistrationViewModel>()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isMobile = currentDeviceConfigure().isCompact()
    val snackbarHostState = remember { SnackbarHostState() }
    val isMobileDevice = currentDeviceConfigure().isMobileDevice()
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
            snackbarHostState.showSnackbar(msg.asStringAsync())
        }
    }

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val navItems = listOf(
        NavItem("program", stringResource(Res.string.landing_nav_program)),
        NavItem("contact", stringResource(Res.string.landing_nav_contact)),
    )

    WebPageScaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize(),
        scrollState = scrollState,
        header = {
            MyColoNavBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                scrollState = scrollState,
                navItems = navItems,
                onNavItemClick = { item ->
                    coroutineScope.launch {
                        when (item.id) {
                            "program" -> scrollState.animateScrollToItem(1)
                            "contact" -> scrollState.animateScrollToItem(scrollState.layoutInfo.totalItemsCount - 1)
                        }
                    }
                },
                onAdminClick = onNavigateBack
            )
        },
        footer = { MyColoFooter(stringResource(Res.string.powered_by)) },
        floatingButton = {
            AnimatedVisibility(
                modifier = Modifier.padding(end = 16.dp, bottom = 24.dp),
                visible = scrollState.firstVisibleItemIndex > 0,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally(),
            ) {
                Box(
                    modifier = Modifier.size(48.dp).background(Primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) { Text("↑", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold) }
            }
        }
    ) {
        item {
            WebFormPageScaffold(
                modifier = Modifier.wrapContentHeight(),
                title = stringResource(Res.string.form_title),
                description =  "Complétez tous les champs pour soumettre votre candidature.",
            ){
                RegisterForm()
            }
        }
    }


}

// ── Mobile — Scaffold + TopAppBar ─────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MobileRegistrationLayout(
    modifier: Modifier = Modifier,
    state: RegistrationState,
    onAction: (RegistrationAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            modifier = Modifier,
            // .verticalScroll(rememberScrollState())
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            // Bouton retour discret

            Text(
                "",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              "" ,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
            )

            Spacer(Modifier.height(20.dp))


            RegistrationFormContent(
                state = state,
                onAction = onAction,
            )
        }
    }

}

// ── Desktop — split 40/60 ─────────────────────────────────────────────────────

@Composable
private fun DesktopRegistrationLayout(
    modifier: Modifier = Modifier,
    state: RegistrationState,
    onAction: (RegistrationAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
) {
    Row(modifier = modifier) {

        // Panneau gauche — info colonie
        Box(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight()
                .background(brush = Brush.verticalGradient(listOf(PrimaryDark, Primary)))
                .padding(40.dp),
            contentAlignment = Alignment.Center
        )
        {
            Column(
                modifier = Modifier.width(300.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Box(
                    modifier = Modifier.size(72.dp).background(Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("MC", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }
                Text(
                    "My Colo",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    "Inscription à la colonie",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White.copy(alpha = 0.85f),
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
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier.size(20.dp).background(Color.White.copy(alpha = 0.25f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(12.dp))
                        }
                        Text(
                            text = info,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.padding(start = 10.dp)
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
        Box(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxHeight().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        )
        {
            Column(
                modifier = Modifier,
                // .verticalScroll(rememberScrollState())
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            )
            {
             /*   // Bouton retour discret

                Text(
                    stringResource(Res.string.form_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Complétez tous les champs pour soumettre votre candidature.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
                )

                Spacer(Modifier.height(20.dp))*/


                RegistrationFormContent(
                    state = state,
                    onAction = onAction,
                )
            }
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

    val animeBoxError by animateDpAsState(
        targetValue = if (state.errorMessage == null) 0.dp else 46.dp
    )




}


@Composable
private fun JobStatusCard(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = if (isSelected) Primary else MaterialTheme.colorScheme.outlineVariant
    val bgColor = if (isSelected) PrimaryContainer else MaterialTheme.colorScheme.surface
    val textColor = if (isSelected) Primary else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = modifier
            .height(40.dp)
            .clickable(onClick = onClick)
            .border(if (isSelected) 2.dp else 1.dp, borderColor, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = textColor,
                modifier = Modifier.weight(1f)
            )
            if (isSelected) {
                Box(
                    modifier = Modifier.size(20.dp).background(Primary, RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}
