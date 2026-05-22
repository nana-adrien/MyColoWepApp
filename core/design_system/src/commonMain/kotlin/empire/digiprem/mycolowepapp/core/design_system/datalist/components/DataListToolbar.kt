package empire.digiprem.mycolowepapp.core.design_system.datalist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import empire.digiprem.mycolowepapp.core.design_system.datalist.model.ColumnDef
import empire.digiprem.mycolowepapp.core.design_system.datalist.model.ExportFormat
import empire.digiprem.mycolowepapp.core.design_system.datalist.model.ImportFormat

@Composable
fun <T> DataListToolbar(
    columns: List<ColumnDef<T>>,
    visibleColumns: Set<String>,
    onExport: (ExportFormat) -> Unit,
    onImport: (ImportFormat) -> Unit,
    onToggleColumn: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ExportMenu(onExport = onExport)
        ImportMenu(onImport = onImport)
        Spacer(modifier = Modifier.weight(1f))
        DataListColumnToggle(columns = columns, visibleColumns = visibleColumns, onToggle = onToggleColumn)
    }
}
