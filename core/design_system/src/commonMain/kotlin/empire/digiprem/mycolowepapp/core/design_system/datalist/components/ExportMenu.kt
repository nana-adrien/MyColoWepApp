package empire.digiprem.mycolowepapp.core.design_system.datalist.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
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
import empire.digiprem.mycolowepapp.core.design_system.datalist.model.ExportFormat

@Composable
fun ExportMenu(
    onExport: (ExportFormat) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        OutlinedButton(onClick = { expanded = true }, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)) {
            Icon(Icons.Outlined.FileDownload, null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Exporter", style = MaterialTheme.typography.labelMedium)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Exporter en PDF") }, onClick = { expanded = false; onExport(ExportFormat.PDF) })
            DropdownMenuItem(text = { Text("Exporter en CSV") }, onClick = { expanded = false; onExport(ExportFormat.CSV) })
            DropdownMenuItem(text = { Text("Exporter en XLSX") }, onClick = { expanded = false; onExport(ExportFormat.XLSX) })
        }
    }
}
