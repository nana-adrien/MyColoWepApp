package empire.digiprem.mycoloapp.features.security_code.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import empire.digiprem.mycoloapp.core.design_system.theme.PrimaryDark
import empire.digiprem.mycoloapp.core.design_system.theme.Secondary
import empire.digiprem.mycoloapp.features.security_code.domain.model.SecurityCode
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SecurityCodeScreen(
    onNavigateBack: () -> Unit,
    viewModel: SecurityCodeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Dialogs feedback
    state.successMessage?.let { msg ->
        AlertDialog(
            onDismissRequest = { viewModel.onAction(SecurityCodeAction.OnDismissSuccess) },
            title = { Text("Succès") },
            text = { Text(msg) },
            confirmButton = {
                TextButton(onClick = { viewModel.onAction(SecurityCodeAction.OnDismissSuccess) }) { Text("OK") }
            }
        )
    }
    state.actionError?.let { err ->
        AlertDialog(
            onDismissRequest = { viewModel.onAction(SecurityCodeAction.OnDismissError) },
            title = { Text("Erreur") },
            text = { Text(err) },
            confirmButton = {
                TextButton(onClick = { viewModel.onAction(SecurityCodeAction.OnDismissError) }) { Text("OK") }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Top bar
        Row(
            modifier          = Modifier.fillMaxWidth().background(PrimaryDark).padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Filled.ArrowBack, "Retour", tint = Color.White)
            }
            Text(
                text       = "Codes de sécurité",
                style      = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color      = Color.White,
                modifier   = Modifier.weight(1f)
            )
            if (state.isGenerating) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp).padding(end = 8.dp))
            } else {
                Button(
                    onClick = { viewModel.onAction(SecurityCodeAction.OnGenerateCode) },
                    colors  = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                    shape   = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Filled.Add, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Text("Générer", color = Color.White, style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Primary)
            }
            state.loadError != null -> Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(state.loadError!!, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(16.dp))
                Button(onClick = { viewModel.onAction(SecurityCodeAction.OnRetryLoad) }) { Text("Réessayer") }
            }
            else -> SecurityCodeList(
                codes    = state.codes,
                onToggle = { id, isActive -> viewModel.onAction(SecurityCodeAction.OnToggleActive(id, isActive)) }
            )
        }
    }
}

@Composable
private fun SecurityCodeList(codes: List<SecurityCode>, onToggle: (String, Boolean) -> Unit) {
    if (codes.isEmpty()) {
        Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("🔑", fontSize = 48.sp)
                Text("Aucun code généré", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("Utilisez le bouton + pour créer votre premier code",
                    style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        return
    }

    LazyColumn(
        modifier            = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 4.dp)) {
                Text("${codes.size} code(s) au total · ${codes.count { it.isActive }} actif(s)",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        items(codes, key = { it.id }) { code ->
            SecurityCodeCard(code = code, onToggle = { onToggle(code.id, code.isActive) })
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

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
                // Code + badge actif/inactif
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text       = code.code,
                            style      = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace,
                            color      = if (code.isActive) Primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                        Badge(
                            containerColor = if (code.isActive) Secondary else MaterialTheme.colorScheme.outline
                        ) {
                            Text(
                                text  = if (code.isActive) "Actif" else "Désactivé",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        }
                    }
                }

                // Toggle actif/inactif (désactiver seulement, pas supprimer)
                Switch(
                    checked        = code.isActive,
                    onCheckedChange = { onToggle() },
                    colors         = SwitchDefaults.colors(checkedTrackColor = Primary)
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outlineVariant)

            // Méta-informations
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                MetaInfo(label = "Créé par",        value = code.createdByEmail)
                MetaInfo(label = "Date",             value = code.createdAt)
                MetaInfo(label = "Utilisations",     value = code.usageCount.toString())
            }
        }
    }
}

@Composable
private fun MetaInfo(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
    }
}
