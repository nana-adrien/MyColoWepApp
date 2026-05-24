package empire.digiprem.mycoloapp.core.design_system.components.datalist.layouts

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
import empire.digiprem.mycoloapp.core.design_system.components.datalist.components.DataListDetailPanel
import empire.digiprem.mycoloapp.core.design_system.components.datalist.components.DataListHeader
import empire.digiprem.mycoloapp.core.design_system.components.datalist.components.DataListPagination
import empire.digiprem.mycoloapp.core.design_system.components.datalist.components.DataListTable
import empire.digiprem.mycoloapp.core.design_system.components.datalist.components.DataListToolbar

@Composable
fun <T> TabletDataListLayout(
    title: String,
    columns: List<empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ColumnDef<T>>,
    state: empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListState<T>,
    itemKey: (T) -> String,
    onAction: (empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>) -> Unit,
    detailContent: @Composable (T) -> Unit,
    emptyContent: @Composable () -> Unit,
    newButtonLabel: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            _root_ide_package_.empire.digiprem.mycolowepapp.core.design_system.components.datalist.components.DataListHeader(
                title = title, tabs = state.tabs, activeTab = state.activeTab,
                newButtonLabel = newButtonLabel,
                onTabChange = {
                    onAction(
                        _root_ide_package_.empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction.ChangeTab(
                            it
                        )
                    )
                },
                onNewClick = { onAction(_root_ide_package_.empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction.New()) }
            )
            _root_ide_package_.empire.digiprem.mycolowepapp.core.design_system.components.datalist.components.DataListToolbar(
                columns = columns, visibleColumns = state.visibleColumns,
                onExport = {
                    onAction(
                        _root_ide_package_.empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction.Export(
                            it
                        )
                    )
                },
                onImport = {
                    onAction(
                        _root_ide_package_.empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction.Import(
                            it
                        )
                    )
                },
                onToggleColumn = {
                    onAction(
                        _root_ide_package_.empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction.ToggleColumn(
                            it
                        )
                    )
                }
            )
            HorizontalDivider()
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                _root_ide_package_.empire.digiprem.mycolowepapp.core.design_system.components.datalist.components.DataListTable(
                    items = state.items, columns = columns, visibleColumns = state.visibleColumns,
                    sortBy = state.sortBy, filters = state.filters, selectedItem = state.selectedItem,
                    selectedItems = state.selectedItems, showCheckboxes = false,
                    itemKey = itemKey, onAction = onAction, emptyContent = emptyContent
                )
            }
            HorizontalDivider()
            _root_ide_package_.empire.digiprem.mycolowepapp.core.design_system.components.datalist.components.DataListPagination(
                currentPage = state.currentPage, totalPages = state.totalPages,
                totalItems = state.totalItems, itemsPerPage = state.itemsPerPage,
                onPageChange = {
                    onAction(
                        _root_ide_package_.empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction.ChangePage(
                            it
                        )
                    )
                },
                onPageSizeChange = {
                    onAction(
                        _root_ide_package_.empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction.ChangePageSize(
                            it
                        )
                    )
                }
            )
        }
        VerticalDivider()
        _root_ide_package_.empire.digiprem.mycolowepapp.core.design_system.components.datalist.components.DataListDetailPanel(
            item = state.selectedItem,
            isVisible = state.isDetailPanelOpen,
            panelWidth = 360.dp,
            onClose = { onAction(_root_ide_package_.empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction.CloseDetail()) },
            content = detailContent,
            modifier = Modifier.fillMaxHeight()
        )
    }
}
