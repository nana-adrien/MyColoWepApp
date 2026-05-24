package empire.digiprem.mycoloapp.feature.admin.dashboard.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import empire.digiprem.mycoloapp.core.design_system.theme.Primary
import empire.digiprem.mycoloapp.core.design_system.theme.Secondary
import empire.digiprem.mycoloapp.feature.admin.security_code.domain.model.SecurityCode
import empire.digiprem.mycoloapp.feature.admin.security_code.presentation.SecurityCodeAction
import empire.digiprem.mycoloapp.feature.admin.security_code.presentation.SecurityCodeState
import empire.digiprem.mycoloapp.feature.admin.security_code.presentation.SecurityCodeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun SecurityCodesListPage() {
    val viewModel: SecurityCodeViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    val listState = state.toSecurityCodeListState()

    _root_ide_package_.empire.digiprem.mycolowepapp.core.design_system.components.datalist.DataListScreen(
        title = "Codes de sécurité",
        columns = securityCodeColumns(),
        state = listState,
        itemKey = { it.id },
        onAction = { action -> action.toSecurityCodeAction(state)?.let { viewModel.onAction(it) } },
        newButtonLabel = "Générer un code",
        detailContent = { code ->
            SecurityCodeDetailContent(code = code)
        }
    )
}

private fun SecurityCodeState.toSecurityCodeListState(): empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListState<SecurityCode> =
    _root_ide_package_.empire.digiprem.mycolowepapp.core.design_system.components.datalist.model.ListState(
        items = codes,
        isLoading = isLoading || isGenerating,
        errorMessage = loadError ?: actionError,
        totalItems = codes.size,
        totalPages = 1,
        currentPage = 1,
        itemsPerPage = codes.size.coerceAtLeast(10),
        visibleColumns = setOf("code", "isActive", "usageCount", "createdByEmail", "createdAt")
    )

private fun empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<SecurityCode>.toSecurityCodeAction(state: SecurityCodeState): SecurityCodeAction? = when (this) {
    is empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction.New -> SecurityCodeAction.OnGenerateCode
    is empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction.View -> SecurityCodeAction.OnToggleActive(item.id, item.isActive)
    is empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction.Edit -> SecurityCodeAction.OnToggleActive(item.id, item.isActive)
    is empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction.Delete -> SecurityCodeAction.OnToggleActive(item.id, true)
    else -> null
}

@Composable
private fun securityCodeColumns(): List<empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ColumnDef<SecurityCode>> = listOf(
    _root_ide_package_.empire.digiprem.mycolowepapp.core.design_system.components.datalist.model.ColumnDef(
        key = "code",
        header = "Code",
        render = { code ->
            Text(
                code.code,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                color = if (code.isActive) Primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    ),
    _root_ide_package_.empire.digiprem.mycolowepapp.core.design_system.components.datalist.model.ColumnDef(
        key = "isActive",
        header = "Statut",
        render = { code ->
            Text(
                text = if (code.isActive) "Actif" else "Inactif",
                style = MaterialTheme.typography.bodySmall,
                color = if (code.isActive) Secondary else MaterialTheme.colorScheme.error
            )
        }
    ),
    _root_ide_package_.empire.digiprem.mycolowepapp.core.design_system.components.datalist.model.ColumnDef(
        key = "usageCount",
        header = "Utilisations",
        render = { code ->
            Text("${code.usageCount}", style = MaterialTheme.typography.bodySmall)
        }
    ),
    _root_ide_package_.empire.digiprem.mycolowepapp.core.design_system.components.datalist.model.ColumnDef(
        key = "createdByEmail",
        header = "Créé par",
        render = { code ->
            Text(code.createdByEmail, style = MaterialTheme.typography.bodySmall)
        }
    ),
    _root_ide_package_.empire.digiprem.mycolowepapp.core.design_system.components.datalist.model.ColumnDef(
        key = "createdAt",
        header = "Date",
        render = { code ->
            Text(code.createdAt, style = MaterialTheme.typography.bodySmall)
        }
    ),
)

@Composable
private fun SecurityCodeDetailContent(code: SecurityCode) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            code.code,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace,
            color = if (code.isActive) Primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
        Spacer(Modifier.height(16.dp))
        DetailRow("Statut", if (code.isActive) "Actif" else "Inactif")
        DetailRow("Utilisations", code.usageCount.toString())
        DetailRow("Créé par", code.createdByEmail)
        DetailRow("Créé le", code.createdAt)
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
