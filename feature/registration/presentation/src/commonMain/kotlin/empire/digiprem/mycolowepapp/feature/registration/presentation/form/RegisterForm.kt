package empire.digiprem.mycolowepapp.feature.registration.presentation.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import empire.digiprem.mycolowepapp.core.design_system.FormScaffold
import empire.digiprem.mycolowepapp.core.design_system.MyColoButton
import empire.digiprem.mycolowepapp.core.design_system.components.form.BirthDatePickerField
import empire.digiprem.mycolowepapp.core.design_system.components.form.MyColoSelectItemTextField
import empire.digiprem.mycolowepapp.core.design_system.components.form.MyColoTextField
import empire.digiprem.mycolowepapp.core.design_system.components.form.SearchTextField
import empire.digiprem.mycolowepapp.core.design_system.components.form.SecurityCodeTextField
import empire.digiprem.mycolowepapp.core.design_system.components.form.SelectableField
import empire.digiprem.mycolowepapp.core.design_system.extension.asString
import empire.digiprem.mycolowepapp.feature.registration.domain.model.Genre
import empire.digiprem.mycolowepapp.feature.registration.domain.model.EducationLevel
import empire.digiprem.mycolowepapp.feature.registration.presentation.extension.toLabel
import empire.digiprem.mycolowepapp.feature.registration.presentation.RegistrationAction
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.form_family_name
import mycolowepapp.shared.generated.resources.form_full_name
import mycolowepapp.shared.generated.resources.form_security_code
import mycolowepapp.shared.generated.resources.form_submit
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterForm(
    modifier: Modifier = Modifier,
    viewModel: RegisterFormViewModel = koinViewModel(),
    onSuccess: () -> Unit = {},
    onError: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Écoute les événements one-shot
    /*  ObserveAsEvent(viewModel.events) { event ->
          when (event) {
              is RegisterFormEvent.OnSuccess -> onSuccess()
              is RegisterFormEvent.OnError -> onError(event.message)
          }
      }*/

    RegisterFormContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun RegisterFormContent(
    modifier: Modifier = Modifier,
    state: RegisterFormState,
    onAction: (RegisterFormAction) -> Unit,
) {
    FormScaffold(
        modifier = modifier,
        errorMessage = state.errorMessage,
        onCleanErrorClick = {onAction(RegisterFormAction.OnClearError) },
        footer = {
            MyColoButton(
                isLoading = state.isLoading,
                text = stringResource(Res.string.form_submit),
                onClick = { onAction(RegisterFormAction.OnSubmitClick) },
                enabled = !state.isLoading && state.userCanSendFrom
            )

        }
    ){
        MyColoTextField(
            state = state.fullNameTextFieldState,
            enabled = !state.isLoading,
            placeholder = stringResource(Res.string.form_full_name),
            title = stringResource(Res.string.form_full_name)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.weight(0.4f).wrapContentSize()
            ) {
                BirthDatePickerField(
                    modifier = Modifier.fillMaxWidth(),
                    birthDate = state.birthDate,
                    onDateSelected = { onAction(RegisterFormAction.OnBirthDateChange(it)) }
                )

            }
            Spacer(Modifier.weight(0.1f))
            Box(
                modifier = Modifier.weight(0.4f).wrapContentSize()
            ) {
                MyColoSelectItemTextField(
                    modifier = modifier.fillMaxWidth(),
                    title = "Genre",
                    placeholder = "ex.Homme",
                    enabled = !state.isLoading,
                    selectItem = state.genre,
                    items = Genre.entries.map {
                        SelectableField(
                            id = it,
                            text = it.name.lowercase(),
                        )
                    },
                    onSelectItem = { genre -> onAction(RegisterFormAction.OnGenreChange(genre)) },
                )
            }
        }

        MyColoTextField(
            placeholder = stringResource(Res.string.form_family_name),
            title = stringResource(Res.string.form_family_name),
            state = state.familyNameTextFieldState,
            enabled = !state.isLoading,
        )


        MyColoSelectItemTextField(
            modifier = modifier.fillMaxWidth(),
            title = "Niveau d'etude",
            placeholder = "ex.Maternelle",
            enabled = !state.isLoading,
            selectItem = state.educationLevel,
            items = EducationLevel.entries.map {
                SelectableField(
                    id = it,
                    text = it.toLabel(),
                )
            },
            onSelectItem = { level -> onAction(RegisterFormAction.OnEducationLevelChange(level)) },
        )
        Spacer(Modifier.height(4.dp))
        SecurityCodeTextField(
            modifier = Modifier.fillMaxWidth(),
            maxLength = 6,
            enabled = !state.isLoading,
            title = stringResource(Res.string.form_security_code),
            state = state.securityCodeTextFieldState,
        )
       /* val searchState = rememberTextFieldState()

        val professions = listOf(
            SelectableField(1, "Médecin"),
            SelectableField(2, "Ingénieur"),
            SelectableField(3, "Architecte"),
            SelectableField(4, "Avocat"),
        )

        SearchTextField(
            modifier = Modifier.fillMaxWidth(),
            title = "Profession",
            placeholder = "Rechercher une profession...",
            state = searchState,
            suggestions = professions,
            onSuggestionSelected = { selected ->
                // selected.id, selected.text
            }
        )*/


    }
}