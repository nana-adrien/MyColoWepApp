package empire.digiprem.mycolowepapp.core.design_system.components.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import empire.digiprem.mycolowepapp.core.design_system.extension.normalizeText

@Composable
fun <T> SearchTextField(
    modifier: Modifier = Modifier,
    title: String,
    placeholder: String,
    enabled: Boolean = true,
    state: TextFieldState = rememberTextFieldState(),
    suggestions: List<SelectableField<T>>,
    onSuggestionSelected: (SelectableField<T>) -> Unit,
) {
    var showSuggestions by rememberSaveable { mutableStateOf(false) }

    val filteredSuggestions = remember(state.text) {
        val query = state.text.toString().trim().normalizeText()
        if (query.isEmpty()) emptyList()
        else suggestions.filter {
            it.text.normalizeText().contains(query)
        }
    }

    // Affiche les suggestions uniquement si résultats
    LaunchedEffect(filteredSuggestions) {
        showSuggestions = filteredSuggestions.isNotEmpty()
    }

    Box(modifier = Modifier.wrapContentSize()) {
        Column {
            FieldHeader(title = title)

            FieldBoxDecorator(modifier = modifier.clip(RoundedCornerShape(8.dp))) {
                BasicTextField(
                    state = state,
                    enabled = enabled,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    decorator = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            /*Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .size(18.dp)
                                    .padding(end = 4.dp)
                            )*/
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
                            // Bouton clear
                            AnimatedVisibility(
                                visible = state.text.isNotEmpty(),
                                enter = fadeIn() + scaleIn(),
                                exit = fadeOut() + scaleOut()
                            ) {
                                IconButton(
                                    onClick = {
                                        state.edit { replace(0, length, "") }
                                        showSuggestions = false
                                    },
                                    modifier = Modifier.size(18.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Effacer",
                                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }

        DropdownMenu(
            expanded = showSuggestions,
            onDismissRequest = { showSuggestions = false },
            containerColor = MaterialTheme.colorScheme.surface,
            properties = PopupProperties(focusable = false) // garde le clavier ouvert
        ) {
            filteredSuggestions.forEach { suggestion ->
                DropdownMenuItem(
                    text = {
                        // Highlight de la partie recherchée
                        HighlightedText(
                            fullText = suggestion.text,
                            query = state.text.toString(),
                        )
                    },
                    /*leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                            modifier = Modifier.size(16.dp)
                        )
                    },*/
                    onClick = {
                        state.edit { replace(0, length, suggestion.text) }
                        onSuggestionSelected(suggestion)
                        showSuggestions = false
                    }
                )
            }
        }
    }
}

// Met en gras la partie du texte qui correspond à la recherche
@Composable
private fun HighlightedText(
    fullText: String,
    query: String,
) {
    val startIndex = fullText.indexOf(query, ignoreCase = true)

    if (startIndex < 0) {
        Text(
            text = fullText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        return
    }

    val endIndex = startIndex + query.length

    val annotated = buildAnnotatedString {
        append(fullText.substring(0, startIndex))
        withStyle(
            SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(fullText.substring(startIndex, endIndex))
        }
        append(fullText.substring(endIndex))
    }

    Text(
        text = annotated,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface
    )
}