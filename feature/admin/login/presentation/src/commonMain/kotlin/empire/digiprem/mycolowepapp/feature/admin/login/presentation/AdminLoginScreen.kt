package empire.digiprem.mycolowepapp.feature.admin.login.presentation

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycolowepapp.core.design_system.currentDeviceConfigure
import empire.digiprem.mycolowepapp.core.theme.Primary
import empire.digiprem.mycolowepapp.core.theme.PrimaryDark
import empire.digiprem.mycolowepapp.core.ui.MyColoFilledButton
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.admin_connect
import mycolowepapp.shared.generated.resources.admin_email
import mycolowepapp.shared.generated.resources.admin_email_hint
import mycolowepapp.shared.generated.resources.admin_feature_1
import mycolowepapp.shared.generated.resources.admin_feature_2
import mycolowepapp.shared.generated.resources.admin_feature_3
import mycolowepapp.shared.generated.resources.admin_feature_4
import mycolowepapp.shared.generated.resources.admin_login_subtitle
import mycolowepapp.shared.generated.resources.admin_login_title
import mycolowepapp.shared.generated.resources.admin_password
import mycolowepapp.shared.generated.resources.admin_password_hint
import mycolowepapp.shared.generated.resources.admin_secure_label
import mycolowepapp.shared.generated.resources.app_name
import mycolowepapp.shared.generated.resources.powered_by
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AdminLoginScreen(
    onNavigateBack: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: AdminLoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AdminLoginEvent.OnLoginSuccess -> onLoginSuccess()
            }
        }
    }

    val isMobile = currentDeviceConfigure().isMobileDevice()

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        if (isMobile) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(56.dp).background(Primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) { Text(text = "MC", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(Res.string.admin_login_title), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(Res.string.admin_login_subtitle), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(32.dp))
                AdminLoginForm(state = state, onAction = viewModel::onAction)
                Spacer(modifier = Modifier.height(24.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = stringResource(Res.string.admin_secure_label), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "· ${stringResource(Res.string.powered_by)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier.weight(0.4f).fillMaxHeight()
                        .background(brush = Brush.verticalGradient(colors = listOf(PrimaryDark, Primary)))
                        .padding(40.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                        Box(
                            modifier = Modifier.size(72.dp).background(Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) { Text(text = "MC", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }

                        Spacer(modifier = Modifier.height(24.dp))
                        Text(text = stringResource(Res.string.app_name), fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text(text = "Administration", style = MaterialTheme.typography.titleMedium, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(48.dp))

                        listOf(
                            stringResource(Res.string.admin_feature_1),
                            stringResource(Res.string.admin_feature_2),
                            stringResource(Res.string.admin_feature_3),
                            stringResource(Res.string.admin_feature_4)
                        ).forEach { feature ->
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 6.dp)) {
                                Box(modifier = Modifier.size(24.dp).background(Color.White.copy(alpha = 0.2f), CircleShape), contentAlignment = Alignment.Center) {
                                    Icon(imageVector = Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = feature, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.9f))
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier.weight(0.6f).fillMaxHeight().verticalScroll(rememberScrollState()).padding(40.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(Res.string.admin_login_title), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = stringResource(Res.string.admin_login_subtitle), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(40.dp))
                    AdminLoginForm(state = state, onAction = viewModel::onAction)
                    Spacer(modifier = Modifier.height(32.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = stringResource(Res.string.admin_secure_label), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "· ${stringResource(Res.string.powered_by)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminLoginForm(
    state: AdminLoginState,
    onAction: (AdminLoginAction) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = state.email,
                onValueChange = { onAction(AdminLoginAction.OnEmailChange(it)) },
                label = { Text(stringResource(Res.string.admin_email)) },
                placeholder = { Text(stringResource(Res.string.admin_email_hint)) },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = Primary) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                isError = state.errorMessage != null && state.email.isBlank(),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary, focusedLabelColor = Primary, cursorColor = Primary)
            )
            OutlinedTextField(
                value = state.password,
                onValueChange = { onAction(AdminLoginAction.OnPasswordChange(it)) },
                label = { Text(stringResource(Res.string.admin_password)) },
                placeholder = { Text(stringResource(Res.string.admin_password_hint)) },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = Primary) },
                trailingIcon = {
                    IconButton(onClick = { onAction(AdminLoginAction.OnTogglePasswordVisibility) }) {
                        Icon(imageVector = if (state.isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = null, tint = Primary)
                    }
                },
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = state.errorMessage != null && state.password.isBlank(),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary, focusedLabelColor = Primary, cursorColor = Primary)
            )
            state.errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            Spacer(modifier = Modifier.height(8.dp))
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Primary) }
            } else {
                MyColoFilledButton(text = stringResource(Res.string.admin_connect), onClick = { onAction(AdminLoginAction.OnLoginClick) })
            }
        }
    }
}
