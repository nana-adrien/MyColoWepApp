package empire.digiprem.mycolowepapp.core.design_system.datalist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import empire.digiprem.mycolowepapp.core.design_system.datalist.model.ColumnDef
import empire.digiprem.mycolowepapp.core.design_system.datalist.model.ColumnSort
import empire.digiprem.mycolowepapp.core.design_system.datalist.model.ListAction
import empire.digiprem.mycolowepapp.core.design_system.datalist.model.SortOrder

@Composable
fun <T> DataListTable(
    items: List<T>,
    columns: List<ColumnDef<T>>,
    visibleColumns: Set<String>,
    sortBy: ColumnSort?,
    filters: Map<String, String>,
    selectedItem: T?,
    selectedItems: Set<String>,
    showCheckboxes: Boolean,
    itemKey: (T) -> String,
    onAction: (ListAction<T>) -> Unit,
    emptyContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    var activeFilterColumn by remember { mutableStateOf<String?>(null) }
    val visibleCols = columns.filter { it.key in visibleColumns }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showCheckboxes) {
                Checkbox(
                    checked = selectedItems.size == items.size && items.isNotEmpty(),
                    onCheckedChange = { checked ->
                        if (checked) onAction(ListAction.SelectAll())
                        else onAction(ListAction.UnselectAll())
                    },
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
            }

            visibleCols.forEach { col ->
                Row(
                    modifier = if (col.width != null) Modifier.width(col.width) else Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        col.header,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    if (col.sortable) {
                        val sortIcon = when {
                            sortBy?.columnKey == col.key && sortBy.order == SortOrder.ASC -> Icons.Filled.ArrowUpward
                            sortBy?.columnKey == col.key && sortBy.order == SortOrder.DESC -> Icons.Filled.ArrowDownward
                            else -> Icons.Filled.UnfoldMore
                        }
                        IconButton(
                            onClick = {
                                val nextOrder = when {
                                    sortBy?.columnKey != col.key -> SortOrder.ASC
                                    sortBy.order == SortOrder.ASC -> SortOrder.DESC
                                    else -> SortOrder.NONE
                                }
                                onAction(ListAction.SortBy(ColumnSort(col.key, nextOrder)))
                            },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(sortIcon, "Trier", modifier = Modifier.size(14.dp))
                        }
                    }
                    if (col.filterable) {
                        IconButton(
                            onClick = { activeFilterColumn = if (activeFilterColumn == col.key) null else col.key },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                Icons.Filled.FilterList,
                                "Filtrer",
                                modifier = Modifier.size(14.dp),
                                tint = if (!filters[col.key].isNullOrEmpty()) MaterialTheme.colorScheme.primary
                                       else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Text(
                "Actions",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(100.dp)
            )
        }

        activeFilterColumn?.let { colKey ->
            columns.find { it.key == colKey && it.filterable }?.let {
                DataListColumnFilter(
                    columnKey = colKey,
                    currentValue = filters[colKey] ?: "",
                    onFilter = { key, value -> onAction(ListAction.FilterBy(key, value)) },
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                )
            }
        }

        HorizontalDivider()

        if (items.isEmpty()) {
            emptyContent()
        } else {
            items.forEachIndexed { index, item ->
                val key = itemKey(item)
                DataListRow(
                    item = item,
                    columns = columns,
                    visibleColumns = visibleColumns,
                    isEven = index % 2 == 0,
                    isSelected = selectedItem?.let { itemKey(it) } == key,
                    showCheckbox = showCheckboxes,
                    isChecked = key in selectedItems,
                    onAction = onAction,
                    onCheckedChange = { checked ->
                        if (checked) onAction(ListAction.SelectItem(item))
                        else onAction(ListAction.UnselectItem(item))
                    }
                )
                HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
    }
}
