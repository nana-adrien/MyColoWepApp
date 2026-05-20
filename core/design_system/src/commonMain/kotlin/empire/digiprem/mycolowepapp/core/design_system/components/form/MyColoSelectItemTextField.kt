package empire.digiprem.mycolowepapp.core.design_system.components.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class SelectableField<T>(
    val id: T,
    val text: String,
    val icon: ImageVector? = null,
)


@Composable
fun <T> MyColoSelectItemTextField(
    modifier: Modifier,
    title: String,
    placeholder: String,
    enabled: Boolean,
    selectItem: T?,
    items: List<SelectableField<T>>,
    onSelectItem: (T) -> Unit
) {
    var expensiveMenu by rememberSaveable { mutableStateOf(false) }
    val selectedField = selectItem?.let { items.firstOrNull { it.id == selectItem } }
    Box(
        modifier = Modifier.wrapContentSize(),
    ) {
        Column(
            modifier = Modifier
        ) {
            FieldHeader(
                title = title,
            )
            FieldBoxDecorator(
                modifier = modifier.clip(RoundedCornerShape(8.dp)).clickable(
                    enabled = enabled,
                    onClick = {
                        expensiveMenu = true
                    }
                ),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selectedField?.text ?: placeholder,
                        style = if (selectedField == null) MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.5f
                            )
                        ) else MaterialTheme.typography.bodyMedium,
                    )
                    Icon(
                        imageVector = if (expensiveMenu) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropUp,
                        null, tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    )

                }
            }
        }

        DropdownMenu(
            containerColor = MaterialTheme.colorScheme.surface,
            expanded = expensiveMenu,
            onDismissRequest = {
                expensiveMenu = false
            },
        ) {
            items.forEach { item ->
                val isSelected = item.id == selectedField?.id
                DropdownMenuItem(
                    modifier = Modifier.background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent),
                    colors = MenuDefaults.itemColors().copy(
                        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onBackground,
                    ),
                    text = {
                        Text(
                            text = item.text.lowercase().replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    onClick = {
                        onSelectItem(item.id)
                        expensiveMenu = false
                    }
                )
            }
        }
    }
}
