package empire.digiprem.mycolowepapp.core.design_system.components.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.Key.Companion.Calendar
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthDatePickerField(
    modifier: Modifier = Modifier,
    title: String = "Date de naissance",
    placeholder: String = "JJ/MM/AAAA",
    enabled: Boolean = true,
    birthDate: LocalDate? = null,
    onDateSelected: (LocalDate?) -> Unit
) {
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val initialMillis = birthDate?.let { date ->
        LocalDateTime(date.year, date.monthNumber, date.dayOfMonth, 0, 0, 0)
            .toInstant(TimeZone.UTC)
            .toEpochMilliseconds()
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis
    )

    val formattedDate = birthDate?.let { date ->
        "${date.dayOfMonth.toString().padStart(2, '0')}/" +
                "${date.monthNumber.toString().padStart(2, '0')}/" +
                "${date.year}"
    }

    Column(modifier = Modifier.wrapContentSize()) {
        FieldHeader(title = title)

        FieldBoxDecorator(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(
                    enabled = enabled,
                    onClick = { showDatePicker = true }
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formattedDate ?: placeholder,
                    style = if (birthDate == null)
                        MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    else
                        MaterialTheme.typography.bodyMedium,
                )
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val instant = Instant.fromEpochMilliseconds(millis)
                        val localDate = instant.toLocalDateTime(TimeZone.UTC).date
                        onDateSelected(localDate)
                    }
                    showDatePicker = false
                }) {
                    Text("Confirmer")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Annuler")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = true,
            )
        }
    }
}
*/


@Composable
fun BirthDatePickerField(
    modifier: Modifier = Modifier,
    title: String = "Date de naissance",
    placeholder: String = "JJ/MM/AAAA",
    enabled: Boolean = true,
    birthDate: LocalDate? = null,
    yearRange: IntRange = 1970..2026,
    onDateSelected: (LocalDate?) -> Unit
) {
    var input by rememberSaveable {
        mutableStateOf(
            birthDate?.let {
                it.dayOfMonth.toString().padStart(2, '0') +
                        it.monthNumber.toString().padStart(2, '0') +
                        "${it.year}"
            } ?: ""
        )
    }

    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    // Formatte l'affichage : 12051967 -> 12/05/1967
    val displayText = buildString {
        input.forEachIndexed { i, c ->
            if (i == 2 || i == 4) append('/')
            append(c)
        }
    }

    LaunchedEffect(input) {
        if (input.length == 8) {
            try {
                val day = input.substring(0, 2).toInt()
                val month = input.substring(2, 4).toInt()
                val year = input.substring(4, 8).toInt()

                when {
                    day < 1 || day > 31 -> {
                        errorMessage = "Le jour doit être entre 01 et 31"
                        onDateSelected(null)
                    }

                    month < 1 || month > 12 -> {
                        errorMessage = "Le mois doit être entre 01 et 12"
                        onDateSelected(null)
                    }

                    year < yearRange.first -> {
                        errorMessage = "L'année doit être après ${yearRange.first}"
                        onDateSelected(null)
                    }

                    year > yearRange.last -> {
                        errorMessage = "L'année doit être avant ${yearRange.last}"
                        onDateSelected(null)
                    }

                    else -> {
                        // Vérifie aussi que le jour est valide pour ce mois/année
                        val maxDay = when (month) {
                            2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
                            4, 6, 9, 11 -> 30
                            else -> 31
                        }
                        if (day > maxDay) {
                            errorMessage = "Le mois $month n'a que $maxDay jours"
                            onDateSelected(null)
                        } else {
                            errorMessage = null
                            onDateSelected(LocalDate(year, month, day))
                        }
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Date invalide"
                onDateSelected(null)
            }
        } else {
            errorMessage = null
            onDateSelected(null)
        }
    }

    Column(modifier = Modifier.wrapContentSize()) {
        FieldHeader(title = title)
        FieldBoxDecorator(
            modifier = modifier.clip(RoundedCornerShape(8.dp)),
            //  isError = errorMessage != null
        ) {
            BasicTextField(
                value = TextFieldValue(
                    text = displayText,
                    selection = TextRange(displayText.length)
                ),
                onValueChange = { newValue ->
                    val digits = newValue.text.filter { it.isDigit() }.take(8)
                    input = digits
                },
                enabled = enabled,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = if (errorMessage != null)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.onSurface
                ),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            if (input.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                )
                            }
                            innerTextField()
                        }
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = null,
                            tint = if (errorMessage != null)
                                MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                            else
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
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