package empire.digiprem.mycoloapp.core.design_system.components.datalist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> DataListToolbar(
    columns: List<empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ColumnDef<T>>,
    visibleColumns: Set<String>,
    onExport: (empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ExportFormat) -> Unit,
    onImport: (empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ImportFormat) -> Unit,
    onToggleColumn: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        _root_ide_package_.empire.digiprem.mycolowepapp.core.design_system.components.datalist.components.ExportMenu(
            onExport = onExport
        )
        _root_ide_package_.empire.digiprem.mycolowepapp.core.design_system.components.datalist.components.ImportMenu(
            onImport = onImport
        )
        Spacer(modifier = Modifier.weight(1f))
        _root_ide_package_.empire.digiprem.mycolowepapp.core.design_system.components.datalist.components.DataListColumnToggle(
            columns = columns,
            visibleColumns = visibleColumns,
            onToggle = onToggleColumn
        )
    }
}
