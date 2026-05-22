package empire.digiprem.mycolowepapp.core.design_system.datalist.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import empire.digiprem.mycolowepapp.core.design_system.datalist.components.DataListDetailPanel
import empire.digiprem.mycolowepapp.core.design_system.datalist.components.DataListHeader
import empire.digiprem.mycolowepapp.core.design_system.datalist.components.DataListPagination
import empire.digiprem.mycolowepapp.core.design_system.datalist.components.DataListTable
import empire.digiprem.mycolowepapp.core.design_system.datalist.components.DataListToolbar
import empire.digiprem.mycolowepapp.core.design_system.datalist.components.DefaultEmptyContent
import empire.digiprem.mycolowepapp.core.design_system.datalist.model.ColumnDef
import empire.digiprem.mycolowepapp.core.design_system.datalist.model.ListAction
import empire.digiprem.mycolowepapp.core.design_system.datalist.model.ListState

@Composable
fun <T> DesktopDataListLayout(
    title: String,
    columns: List<ColumnDef<T>>,
    state: ListState<T>,
    itemKey: (T) -> String,
    onAction: (ListAction<T>) -> Unit,
    detailContent: @Composable (T) -> Unit,
    emptyContent: @Composable () -> Unit,
    newButtonLabel: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(0.65f).fillMaxHeight()) {
            DataListHeader(
                title = title, tabs = state.tabs, activeTab = state.activeTab,
                newButtonLabel = newButtonLabel,
                onTabChange = { onAction(ListAction.ChangeTab(it)) },
                onNewClick = { onAction(ListAction.New()) }
            )
            DataListToolbar(
                columns = columns, visibleColumns = state.visibleColumns,
                onExport = { onAction(ListAction.Export(it)) },
                onImport = { onAction(ListAction.Import(it)) },
                onToggleColumn = { onAction(ListAction.ToggleColumn(it)) }
            )
            HorizontalDivider()
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                DataListTable(
                    items = state.items, columns = columns, visibleColumns = state.visibleColumns,
                    sortBy = state.sortBy, filters = state.filters, selectedItem = state.selectedItem,
                    selectedItems = state.selectedItems, showCheckboxes = true,
                    itemKey = itemKey, onAction = onAction, emptyContent = emptyContent
                )
            }
            HorizontalDivider()
            DataListPagination(
                currentPage = state.currentPage, totalPages = state.totalPages,
                totalItems = state.totalItems, itemsPerPage = state.itemsPerPage,
                onPageChange = { onAction(ListAction.ChangePage(it)) },
                onPageSizeChange = { onAction(ListAction.ChangePageSize(it)) }
            )
        }
        VerticalDivider()
        Box(modifier = Modifier.weight(0.35f).fillMaxHeight()) {
            if (state.isDetailPanelOpen) {
                DataListDetailPanel(
                    item = state.selectedItem, isVisible = state.isDetailPanelOpen,
                    panelWidth = 600.dp, onClose = { onAction(ListAction.CloseDetail()) },
                    content = detailContent, modifier = Modifier.fillMaxSize()
                )
            } else {
                DefaultEmptyContent()
            }
        }
    }
}
