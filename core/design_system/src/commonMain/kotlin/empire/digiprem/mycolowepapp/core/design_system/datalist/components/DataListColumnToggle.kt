package empire.digiprem.mycolowepapp.core.design_system.datalist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ViewColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import empire.digiprem.mycolowepapp.core.design_system.datalist.model.ColumnDef

@Composable
fun <T> DataListColumnToggle(
    columns: List<ColumnDef<T>>,
    visibleColumns: Set<String>,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        OutlinedButton(onClick = { expanded = true }, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)) {
            Icon(Icons.Filled.ViewColumn, null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Colonnes", style = MaterialTheme.typography.labelMedium)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            columns.forEach { col ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Checkbox(checked = col.key in visibleColumns, onCheckedChange = null)
                            Text(col.header, style = MaterialTheme.typography.bodySmall)
                        }
                    },
                    onClick = { onToggle(col.key) }
                )
            }
        }
    }
}
