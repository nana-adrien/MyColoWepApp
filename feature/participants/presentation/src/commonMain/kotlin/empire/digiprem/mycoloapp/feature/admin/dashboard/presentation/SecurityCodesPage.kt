package empire.digiprem.mycoloapp.feature.admin.dashboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycoloapp.core.design_system.theme.Primary
import empire.digiprem.mycoloapp.core.design_system.theme.Secondary
import empire.digiprem.mycoloapp.feature.admin.security_code.domain.model.SecurityCode
import empire.digiprem.mycoloapp.feature.admin.security_code.presentation.SecurityCodeAction
import empire.digiprem.mycoloapp.feature.admin.security_code.presentation.SecurityCodeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun SecurityCodesPage() {
    val viewModel: SecurityCodeViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .widthIn(max = 800.dp)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ── En-tête ──────────────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Codes de sécurité",
                        style      = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Générez et gérez les codes d'accès pour les inscriptions",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (state.isGenerating) {
                    CircularProgressIndicator(color = Primary, modifier = Modifier.size(28.dp))
                } else {
                    Button(
                        onClick = { viewModel.onAction(SecurityCodeAction.OnGenerateCode) },
                        colors  = ButtonDefaults.buttonColors(containerColor = Primary),
                        shape   = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Filled.Add, null, modifier = Modifier.size(18.dp))
                        Text("Générer un code", modifier = Modifier.padding(start = 6.dp))
                    }
                }
            }

            // ── Feedback ─────────────────────────────────────────────────────
            state.successMessage?.let { msg ->
                FeedbackRow(msg, isSuccess = true) { viewModel.onAction(SecurityCodeAction.OnDismissSuccess) }
            }
            state.actionError?.let { err ->
                FeedbackRow(err, isSuccess = false) { viewModel.onAction(SecurityCodeAction.OnDismissError) }
            }

            // ── Contenu ──────────────────────────────────────────────────────
            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
                state.loadError != null -> Column(
                    modifier            = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("⚠️", fontSize = 36.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(state.loadError!!, color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { viewModel.onAction(SecurityCodeAction.OnRetryLoad) }) {
                        Text("Réessayer")
                    }
                }
                state.codes.isEmpty() -> EmptyCodesState()
                else -> {
                    Text(
                        "${state.codes.size} code(s) · ${state.codes.count { it.isActive }} actif(s)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(state.codes, key = { it.id }) { code ->
                            SecurityCodeCard(
                                code     = code,
                                onToggle = { viewModel.onAction(SecurityCodeAction.OnToggleActive(code.id, code.isActive)) }
                            )
                        }
                        item { Spacer(Modifier.height(24.dp)) }
                    }
                }
            }
        }
    }
}

// ── Composants ────────────────────────────────────────────────────────────────

@Composable
private fun SecurityCodeCard(code: SecurityCode, onToggle: () -> Unit) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(
            containerColor = if (code.isActive) MaterialTheme.colorScheme.surface
                             else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (code.isActive) 1.dp else 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier              = Modifier.weight(1f)
                ) {
                    Text(
                        text       = code.code,
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        color      = if (code.isActive) Primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                    Badge(containerColor = if (code.isActive) Secondary else MaterialTheme.colorScheme.outline) {
                        Text(
                            if (code.isActive) "Actif" else "Désactivé",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }
                Switch(
                    checked        = code.isActive,
                    onCheckedChange = { onToggle() },
                    colors         = SwitchDefaults.colors(checkedTrackColor = Primary)
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outlineVariant)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                MetaInfo("Créé par",      code.createdByEmail)
                MetaInfo("Date",          code.createdAt)
                MetaInfo("Utilisations",  code.usageCount.toString())
            }
        }
    }
}

@Composable
private fun EmptyCodesState() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("🔑", fontSize = 48.sp)
            Text("Aucun code généré", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("Utilisez le bouton « Générer un code » pour créer votre premier code d'accès.",
                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun FeedbackRow(message: String, isSuccess: Boolean, onDismiss: () -> Unit) {
    val bgColor   = if (isSuccess) Secondary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.errorContainer
    val textColor = if (isSuccess) Secondary else MaterialTheme.colorScheme.error

    Row(
        modifier              = Modifier.fillMaxWidth()
            .then(Modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Card(
            modifier  = Modifier.fillMaxWidth(),
            shape     = RoundedCornerShape(8.dp),
            colors    = CardDefaults.cardColors(containerColor = bgColor),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier              = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(message, style = MaterialTheme.typography.bodySmall, color = textColor, modifier = Modifier.weight(1f))
                TextButton(onClick = onDismiss) {
                    Text("OK", color = textColor, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
private fun MetaInfo(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, maxLines = 1)
    }
}
