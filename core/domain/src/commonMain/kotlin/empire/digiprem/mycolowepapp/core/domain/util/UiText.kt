package octopusfx.client.mobile.core.shared.util

import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString


sealed interface UiText {
    data class DynamicString(val value: String): UiText
    data class Resource(val id: StringResource, val args: Array<Any> = emptyArray()): UiText

    suspend fun asStringAsync():String{
      return  when(this){
            is DynamicString->value
            is Resource -> getString(
                id,
                *args
            )
        }
    }
}