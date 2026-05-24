package empire.digiprem.mycoloapp.core.design_system.components.datalist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun <T> DataListRow(
    item: T,
    columns: List<empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ColumnDef<T>>,
    visibleColumns: Set<String>,
    isEven: Boolean,
    isSelected: Boolean,
    showCheckbox: Boolean,
    isChecked: Boolean,
    onAction: (empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val bgColor = when {
        isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        isEven -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        else -> Color.Transparent
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor)
            .clickable { onAction(_root_ide_package_.empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction.View(item)) }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showCheckbox) {
            Checkbox(checked = isChecked, onCheckedChange = onCheckedChange, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(8.dp))
        }

        val visibleCols = columns.filter { it.key in visibleColumns }
        visibleCols.forEach { col ->
            Box(modifier = if (col.width != null) _root_ide_package_.androidx.compose.ui.Modifier.Companion.width(col.width) else Modifier.weight(1f)) {
                col.render(this@Row, item)
            }
        }

        _root_ide_package_.empire.digiprem.mycolowepapp.core.design_system.components.datalist.components.DataListActions(
            item = item,
            onView = {
                onAction(
                    _root_ide_package_.empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction.View(
                        it
                    )
                )
            },
            onEdit = {
                onAction(
                    _root_ide_package_.empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction.Edit(
                        it
                    )
                )
            },
            onDelete = {
                onAction(
                    _root_ide_package_.empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction.Delete(
                        it
                    )
                )
            },
            modifier = Modifier.wrapContentWidth()
        )
    }
}
