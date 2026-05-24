package empire.digiprem.mycoloapp.core.design_system.components.datalist.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileUpload
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ImportMenu(
    onImport: (empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ImportFormat) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        OutlinedButton(onClick = { expanded = true }, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)) {
            Icon(Icons.Outlined.FileUpload, null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Importer", style = MaterialTheme.typography.labelMedium)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Importer CSV") }, onClick = { expanded = false; onImport(_root_ide_package_.empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ImportFormat.CSV) })
            DropdownMenuItem(text = { Text("Importer XLSX") }, onClick = { expanded = false; onImport(_root_ide_package_.empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ImportFormat.XLSX) })
        }
    }
}
