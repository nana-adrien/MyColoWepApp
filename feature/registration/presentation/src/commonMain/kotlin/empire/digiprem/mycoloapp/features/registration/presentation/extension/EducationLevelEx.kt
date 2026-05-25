package empire.digiprem.mycoloapp.features.registration.presentation.extension

import empire.digiprem.mycoloapp.core.domain.util.UiText
import empire.digiprem.mycoloapp.features.registration.domain.model.EducationLevel
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.education_level_higher_worker
import mycolowepapp.shared.generated.resources.education_level_kindergarten
import mycolowepapp.shared.generated.resources.education_level_primary
import mycolowepapp.shared.generated.resources.education_level_secondary

fun EducationLevel.toUiText(): UiText = UiText.Resource(
    when (this) {
        EducationLevel.KINDERGARTEN  -> Res.string.education_level_kindergarten
        EducationLevel.PRIMARY       -> Res.string.education_level_primary
        EducationLevel.SECONDARY     -> Res.string.education_level_secondary
        EducationLevel.HIGHER_WORKER -> Res.string.education_level_higher_worker
    }
)

fun EducationLevel.toLabel(): String = when (this) {
    EducationLevel.KINDERGARTEN  -> "Maternelle"
    EducationLevel.PRIMARY       -> "Primaire"
    EducationLevel.SECONDARY     -> "Collège / Lycée"
    EducationLevel.HIGHER_WORKER -> "Étudiant / Travailleur"
}
