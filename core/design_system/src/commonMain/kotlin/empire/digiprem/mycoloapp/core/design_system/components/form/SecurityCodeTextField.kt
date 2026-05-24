package empire.digiprem.mycoloapp.core.design_system.components.form


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import empire.digiprem.mycoloapp.core.domain.util.Tools

@Composable
fun SecurityCodeTextField(
    modifier: Modifier = Modifier,
    title: String = "Code de sécurité",
    placeholder: String = "Entrez votre code",
    enabled: Boolean = true,
    maxLength: Int = 8,
    state: TextFieldState = rememberTextFieldState(),
    errorMessage: String? = null,
) {

    // Force majuscules, filtre et bloque au maxLength
    LaunchedEffect(Unit) {
        // Initialise le champ avec le préfixe dès l'ouverture
        if (!state.text.startsWith(Tools.securityCodePrefix)) {
            state.edit {
                replace(0, length, Tools.securityCodePrefix)
                placeCursorAtEnd()
            }
        }

        snapshotFlow { state.text }
            .collect { text ->
                val full = text.toString()

                // Empêche la suppression du préfixe
                if (!full.startsWith(Tools.securityCodePrefix)) {
                    state.edit {
                        replace(0, length, Tools.securityCodePrefix)
                        placeCursorAtEnd()
                    }
                    return@collect
                }

                // Filtre la partie utilisateur
                val userPart = full.removePrefix(Tools.securityCodePrefix)
                    .uppercase()
                    .filter { it.isLetterOrDigit() }
                    .take(maxLength)

                val result = Tools.securityCodePrefix + userPart

                if (result != full) {
                    state.edit {
                        replace(0, length, result)
                        placeCursorAtEnd()
                    }
                }
            }
    }

    Column(modifier = Modifier.wrapContentSize()) {
        FieldHeader(title = title)

        FieldBoxDecorator(
            modifier = modifier.clip(RoundedCornerShape(8.dp)),
        ) {
            BasicTextField(
                state = state,
                enabled = enabled,
                inputTransformation = InputTransformation {
                    val full = this.asCharSequence().toString()

                    // Assure que le préfixe est toujours présent
                    val withPrefix = if (full.startsWith(Tools.securityCodePrefix)) full else Tools.securityCodePrefix + full.removePrefix(Tools.securityCodePrefix)

                    // Partie saisie par l'utilisateur (après le préfixe)
                    val userPart = withPrefix.removePrefix(Tools.securityCodePrefix)
                        .uppercase()
                        .filter { it.isLetterOrDigit() }
                        .take(maxLength) // maxLength = longueur de la partie utilisateur uniquement

                    val result = Tools.securityCodePrefix + userPart

                    if (result != this.asCharSequence().toString()) {
                        this.replace(0, this.length, result)
                        this.placeCursorAtEnd()
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Characters,
                    imeAction = ImeAction.Done
                ),
                lineLimits = TextFieldLineLimits.SingleLine,
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    color = if (errorMessage != null)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp
                ),
                decorator = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            if (state.text.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                )
                            }
                            innerTextField()
                        }
                        Text(
                            text = "${(state.text.toString().removePrefix(Tools.securityCodePrefix)).length}/$maxLength",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (state.text.length == maxLength)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        )
                    }
                }
            )
        }

        // Message d'erreur
        AnimatedVisibility(
            visible = errorMessage != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Row(
                modifier = Modifier.padding(start = 4.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = errorMessage ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}