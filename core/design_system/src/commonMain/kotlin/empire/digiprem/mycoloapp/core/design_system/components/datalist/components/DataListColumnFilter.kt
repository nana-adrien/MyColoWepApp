package empire.digiprem.mycoloapp.core.design_system.components.datalist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp

@Composable
fun DataListColumnFilter(
    columnKey: String,
    currentValue: String,
    onFilter: (key: String, value: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by remember(currentValue) { mutableStateOf(currentValue) }

    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        BasicTextField(
            value = text,
            onValueChange = { text = it; onFilter(columnKey, it) },
            modifier = Modifier.weight(1f),
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            textStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface),
            decorationBox = { inner ->
                Box(Modifier.fillMaxWidth().padding(4.dp)) {
                    if (text.isEmpty()) {
                        Text("Filtrer…", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    inner()
                }
            }
        )
        if (text.isNotEmpty()) {
            IconButton(onClick = { text = ""; onFilter(columnKey, "") }, modifier = Modifier.size(20.dp)) {
                Icon(Icons.Filled.Close, "Effacer", modifier = Modifier.size(12.dp))
            }
        }
    }
}
