package empire.digiprem.mycoloapp.core.design_system.components.datalist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ColumnDef
import empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ColumnSort
import empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction
import empire.digiprem.mycoloapp.core.design_system.components.datalist.model.SortOrder

@Composable
fun <T> DataListTable2(
    items: List<T>,
    columns: List<empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ColumnDef<T>>,
    visibleColumns: Set<String>,
    sortBy: empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ColumnSort?,
    filters: Map<String, String>,
    selectedItem: T?,
    selectedItems: Set<String>,
    showCheckboxes: Boolean,
    itemKey: (T) -> String,
    onAction: (empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>) -> Unit,
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
        ){
           if (showCheckboxes) {
                    Checkbox(
                        checked = selectedItems.size == items.size && items.isNotEmpty(),
                        onCheckedChange = { checked ->
                            if (checked) onAction( ListAction.SelectAll())
                            else onAction( ListAction.UnselectAll())
                        },
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                }

            visibleCols.map { col ->
                Row(
                    modifier = if (col.width != null)  Modifier.Companion.width(col.width) else Modifier.weight(1f),
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
                            sortBy?.columnKey == col.key && sortBy.order ==  SortOrder.ASC -> Icons.Filled.ArrowUpward
                            sortBy?.columnKey == col.key && sortBy.order ==  SortOrder.DESC -> Icons.Filled.ArrowDownward
                            else -> Icons.Filled.UnfoldMore
                        }
                        IconButton(
                            onClick = {
                                val nextOrder = when {
                                    sortBy?.columnKey != col.key ->  SortOrder.ASC
                                    sortBy.order ==  SortOrder.ASC ->  SortOrder.DESC
                                    else ->  SortOrder.NONE
                                }
                                onAction(
                                    ListAction.SortBy(
                                        ColumnSort(
                                            col.key,
                                            nextOrder
                                        )
                                    ))
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
                    onFilter = { key, value ->
                        onAction(
                             ListAction.FilterBy(
                                key,
                                value
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
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
                        if (checked) onAction(
                             ListAction.SelectItem(
                                item
                            )
                        )
                        else onAction(
                             ListAction.UnselectItem(
                                item
                            )
                        )
                    }
                )
                HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
    }
}

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
    val visibleCols = remember(columns, visibleColumns) {
        columns.filter { it.key in visibleColumns }
    }
    val density = LocalDensity.current
    val minWidthPx = with(density) { 80.dp.toPx() }

    // ✅ Largeurs en px directement observables
    val columnWidthsPx = remember(visibleCols.size) {
        mutableStateListOf<Float>().apply {
            visibleCols.forEach { col ->
                add(with(density) { (col.width ?: 150.dp).toPx() })
            }
        }
    }

    // Largeur totale = somme colonnes + colonne Actions + checkbox
    val checkboxWidthPx = if (showCheckboxes) with(density) { 40.dp.toPx() } else 0f
    val actionsWidthPx = with(density) { 110.dp.toPx() }
    val totalWidthPx = columnWidthsPx.sum() + actionsWidthPx + checkboxWidthPx

    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    Column(modifier = modifier.fillMaxWidth()) {

        // ============================================
        // FILTRE ACTIF
        // ============================================
        activeFilterColumn?.let { colKey ->
            columns.find { it.key == colKey && it.filterable }?.let {
                DataListColumnFilter(
                    columnKey = colKey,
                    currentValue = filters[colKey] ?: "",
                    onFilter = { key, value ->
                        onAction(ListAction.FilterBy(key, value))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                )
            }
        }

        // ============================================
        // EN-TÊTE — colonnes redimensionnables
        // ============================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(horizontalScrollState)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .height(48.dp)
        ) {
            Row(
                modifier = Modifier
                    .width(with(density) { totalWidthPx.toDp() })
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Checkbox sélection tout
                if (showCheckboxes) {
                    Box(
                        modifier = Modifier
                            .width(with(density) { checkboxWidthPx.toDp() })
                            .fillMaxHeight()
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Checkbox(
                            checked = selectedItems.size == items.size && items.isNotEmpty(),
                            onCheckedChange = { checked ->
                                if (checked) onAction(ListAction.SelectAll())
                                else onAction(ListAction.UnselectAll())
                            },
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Colonnes redimensionnables
                visibleCols.forEachIndexed { colIndex, col ->
                    val widthPx = columnWidthsPx.getOrElse(colIndex) { 150f }

                    // Cellule en-tête
                    Box(
                        modifier = Modifier
                            .width(with(density) { widthPx.toDp() })
                            .fillMaxHeight()
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                col.header,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (col.sortable) {
                                val sortIcon = when {
                                    sortBy?.columnKey == col.key && sortBy.order == SortOrder.ASC ->
                                        Icons.Filled.ArrowUpward
                                    sortBy?.columnKey == col.key && sortBy.order == SortOrder.DESC ->
                                        Icons.Filled.ArrowDownward
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
                                    onClick = {
                                        activeFilterColumn =
                                            if (activeFilterColumn == col.key) null else col.key
                                    },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.FilterList,
                                        "Filtrer",
                                        modifier = Modifier.size(14.dp),
                                        tint = if (!filters[col.key].isNullOrEmpty())
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    // ✅ Diviseur redimensionnable entre colonnes
                    if (colIndex < visibleCols.size - 1) {
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                                .pointerHoverIcon(
                                    PointerIcon.Hand
                                )
                                .pointerInput(colIndex, columnWidthsPx.size) {
                                    awaitEachGesture {
                                        val down = awaitFirstDown(requireUnconsumed = false)
                                        down.consume()

                                        while (true) {
                                            val event = awaitPointerEvent(
                                                pass = PointerEventPass.Main
                                            )
                                            val change = event.changes
                                                .firstOrNull { it.id == down.id }
                                                ?: break

                                            if (!change.pressed) break

                                            val dragX = change.positionChange().x
                                            change.consume()

                                            if (dragX == 0f) continue

                                            val currentWidth = columnWidthsPx[colIndex]
                                            val nextWidth = columnWidthsPx[colIndex + 1]

                                            val newCurrentWidth = (currentWidth + dragX)
                                                .coerceAtLeast(minWidthPx)
                                            val newNextWidth = (nextWidth - dragX)
                                                .coerceAtLeast(minWidthPx)

                                            if (newCurrentWidth >= minWidthPx &&
                                                newNextWidth >= minWidthPx
                                            ) {
                                                columnWidthsPx[colIndex] = newCurrentWidth
                                                columnWidthsPx[colIndex + 1] = newNextWidth
                                            }
                                        }
                                    }
                                }
                        )
                    }
                }

                // Colonne Actions — fixe, non redimensionnable
                Box(
                    modifier = Modifier
                        .width(with(density) { actionsWidthPx.toDp() })
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        "Actions",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.align(Alignment.BottomCenter),
                color = MaterialTheme.colorScheme.outline
            )
        }

        // ============================================
        // CORPS — lignes de données
        // ============================================
        if (items.isEmpty()) {
            emptyContent()
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .horizontalScroll(horizontalScrollState) // ✅ même scrollState que l'en-tête
                        //.verticalScroll(verticalScrollState)
                        .heightIn(max = 800.dp)
                        .width(with(density) { totalWidthPx.toDp() })
                ) {
                    items.forEachIndexed { rowIndex, item ->
                        val key = itemKey(item)
                        val isSelected = selectedItem?.let { itemKey(it) } == key
                        val isEven = rowIndex % 2 == 0

                        Row(
                            modifier = Modifier
                                .width(with(density) { totalWidthPx.toDp() })
                                .height(48.dp)
                                .background(
                                    when {
                                        isSelected -> MaterialTheme.colorScheme.primaryContainer
                                        isEven -> MaterialTheme.colorScheme.surface
                                        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                    }
                                )
                                .clickable { onAction(ListAction.View(item)) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Checkbox par ligne
                            if (showCheckboxes) {
                                Box(
                                    modifier = Modifier
                                        .width(with(density) { checkboxWidthPx.toDp() })
                                        .fillMaxHeight()
                                        .padding(horizontal = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Checkbox(
                                        checked = key in selectedItems,
                                        onCheckedChange = { checked ->
                                            if (checked) onAction(ListAction.SelectItem(item))
                                            else onAction(ListAction.UnselectItem(item))
                                        },
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            // Cellules de données
                            visibleCols.forEachIndexed { colIndex, col ->
                                val widthPx = columnWidthsPx.getOrElse(colIndex) { 150f }

                                Box(
                                    modifier = Modifier
                                        .width(with(density) { widthPx.toDp() })
                                        .fillMaxHeight()
                                        .padding(horizontal = 8.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    col.render(this@Row, item)
                                }

                                // Diviseur vertical entre cellules
                                if (colIndex < visibleCols.size - 1) {
                                    Box(
                                        modifier = Modifier
                                            .width(4.dp)
                                            .fillMaxHeight()
                                            .background(
                                                MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                                            )
                                    )
                                }
                            }

                            // Actions par ligne
                            Box(
                                modifier = Modifier
                                    .width(with(density) { actionsWidthPx.toDp() })
                                    .fillMaxHeight()
                                    .padding(horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                DataListActions(
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
                                )
                            }
                        }

                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }

                // Scrollbar verticale
              /*  VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(verticalScrollState),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                )*/
            }
        }
    }
}