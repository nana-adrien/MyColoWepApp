package empire.digiprem.mycolowepapp.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import empire.digiprem.mycolowepapp.core.theme.Primary
import empire.digiprem.mycolowepapp.core.theme.PrimaryContainer
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.form_security_code
import mycolowepapp.shared.generated.resources.form_security_code_hint
import org.jetbrains.compose.resources.stringResource

@Composable
fun SecurityCodeField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.form_security_code),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.length <= 10) onValueChange(newValue.uppercase())
            },
            label = { Text(stringResource(Res.string.form_security_code)) },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = PrimaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null,
                    tint = Primary
                )
            },
            isError = isError,
            supportingText = {
                if (isError && errorMessage != null) {
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                } else {
                    Text(
                        text = stringResource(Res.string.form_security_code_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                focusedLabelColor = Primary,
                cursorColor = Primary,
                unfocusedContainerColor = PrimaryContainer,
                focusedContainerColor = PrimaryContainer
            ),
            textStyle = MaterialTheme.typography.titleMedium.copy(
                textAlign = TextAlign.Center,
                letterSpacing = 6.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            ),
            singleLine = true
        )
    }
}
