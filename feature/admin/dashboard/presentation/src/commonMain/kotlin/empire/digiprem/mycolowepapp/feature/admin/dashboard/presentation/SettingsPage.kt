package empire.digiprem.mycolowepapp.feature.admin.dashboard.presentation

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import empire.digiprem.mycolowepapp.core.theme.Primary
import empire.digiprem.mycolowepapp.core.theme.Secondary
import empire.digiprem.mycolowepapp.core.ui.MyColoFilledButton

@Composable
internal fun SettingsPage(
    state: AdminDashboardState,
    onAction: (AdminDashboardAction) -> Unit,
) {
    Box(
        modifier        = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 640.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "Paramètres",
                style      = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.onSurface
            )

            AdminProfileCard(adminEmail = state.adminEmail)
            ChangePasswordCard(form = state.passwordForm, onAction = onAction)

            Spacer(Modifier.height(16.dp))
        }
    }
}

// ── Carte profil admin ────────────────────────────────────────────────────────

@Composable
private fun AdminProfileCard(adminEmail: String) {
    SettingsCard(title = "Mon compte") {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier         = Modifier.size(52.dp).background(Primary.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.AccountCircle, null, tint = Primary, modifier = Modifier.size(32.dp))
            }
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text("Administrateur", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text(
                    text  = adminEmail.ifEmpty { "Email non disponible" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            "Votre rôle : Administrateur My Colo",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ── Carte changement de mot de passe ─────────────────────────────────────────

@Composable
private fun ChangePasswordCard(
    form: PasswordFormData,
    onAction: (AdminDashboardAction) -> Unit,
) {
    var showNew     by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    SettingsCard(title = "Changer le mot de passe") {

        // Feedback succès
        form.successMessage?.let { msg ->
            FeedbackBanner(message = msg, isSuccess = true) {
                onAction(AdminDashboardAction.OnDismissPasswordFeedback)
            }
            Spacer(Modifier.height(8.dp))
        }

        // Feedback erreur
        form.errorMessage?.let { msg ->
            FeedbackBanner(message = msg, isSuccess = false) {
                onAction(AdminDashboardAction.OnDismissPasswordFeedback)
            }
            Spacer(Modifier.height(8.dp))
        }

        // Nouveau mot de passe
        OutlinedTextField(
            value         = form.newPassword,
            onValueChange = { onAction(AdminDashboardAction.OnPasswordNewChange(it)) },
            label         = { Text("Nouveau mot de passe") },
            placeholder   = { Text("8 caractères minimum") },
            leadingIcon   = { Icon(Icons.Filled.Lock, null, tint = Primary) },
            trailingIcon  = {
                IconButton(onClick = { showNew = !showNew }) {
                    Icon(if (showNew) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, null, tint = Primary)
                }
            },
            visualTransformation = if (showNew) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine           = true,
            isError              = form.newPasswordError != null,
            supportingText       = form.newPasswordError?.let { { Text(it) } },
            modifier             = Modifier.fillMaxWidth(),
            colors               = passwordFieldColors()
        )

        // Confirmer le mot de passe
        OutlinedTextField(
            value         = form.confirmPassword,
            onValueChange = { onAction(AdminDashboardAction.OnPasswordConfirmChange(it)) },
            label         = { Text("Confirmer le mot de passe") },
            leadingIcon   = { Icon(Icons.Filled.Lock, null, tint = Primary) },
            trailingIcon  = {
                IconButton(onClick = { showConfirm = !showConfirm }) {
                    Icon(if (showConfirm) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, null, tint = Primary)
                }
            },
            visualTransformation = if (showConfirm) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine           = true,
            isError              = form.confirmPasswordError != null,
            supportingText       = form.confirmPasswordError?.let { { Text(it) } },
            modifier             = Modifier.fillMaxWidth(),
            colors               = passwordFieldColors()
        )

        Spacer(Modifier.height(4.dp))

        if (form.isSubmitting) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Primary, modifier = Modifier.size(32.dp))
            }
        } else {
            MyColoFilledButton(
                text    = "Enregistrer le nouveau mot de passe",
                onClick = { onAction(AdminDashboardAction.OnSubmitPasswordChange) },
                enabled = form.isValid
            )
        }
    }
}

// ── Composants utilitaires ────────────────────────────────────────────────────

@Composable
private fun SettingsCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier            = Modifier.fillMaxWidth().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Primary)
            content()
        }
    }
}

@Composable
private fun FeedbackBanner(message: String, isSuccess: Boolean, onDismiss: () -> Unit) {
    val bgColor   = if (isSuccess) Secondary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.errorContainer
    val textColor = if (isSuccess) Secondary else MaterialTheme.colorScheme.error

    Row(
        modifier          = Modifier.fillMaxWidth().background(bgColor, RoundedCornerShape(8.dp)).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(message, style = MaterialTheme.typography.bodySmall, color = textColor, modifier = Modifier.weight(1f))
        TextButton(onClick = onDismiss) {
            Text("OK", color = textColor, fontSize = 12.sp)
        }
    }
}

@Composable
private fun passwordFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor  = Primary,
    focusedLabelColor   = Primary,
    cursorColor         = Primary
)
