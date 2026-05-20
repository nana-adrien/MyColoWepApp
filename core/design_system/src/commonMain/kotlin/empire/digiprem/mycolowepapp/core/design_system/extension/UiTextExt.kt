package octopusfx.client.mobile.core.design_system.extension

import androidx.compose.runtime.Composable
import octopusfx.client.mobile.core.shared.util.UiText
import org.jetbrains.compose.resources.stringResource

@Composable
    fun UiText.asString():String{
       return when(this){
            is UiText.DynamicString ->value
            is UiText.Resource -> stringResource(
                id,
                *args
            )
        }
    }

