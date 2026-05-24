package empire.digiprem.mycoloapp.core.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SecurityCodeField(
    state: TextFieldState,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null
) {
  /*  Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = stringResource(Res.string.form_security_code),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Ba(

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
    }*/
}
