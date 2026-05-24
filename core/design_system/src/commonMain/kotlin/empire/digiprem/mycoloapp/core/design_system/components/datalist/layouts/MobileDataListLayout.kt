package empire.digiprem.mycoloapp.core.design_system.components.datalist.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import empire.digiprem.mycoloapp.core.design_system.components.datalist.components.DataListDetailSheet
import empire.digiprem.mycoloapp.core.design_system.components.datalist.components.DataListHeader
import empire.digiprem.mycoloapp.core.design_system.components.datalist.components.DataListPagination
import empire.digiprem.mycoloapp.core.design_system.components.datalist.components.DataListTable
import empire.digiprem.mycoloapp.core.design_system.components.datalist.components.DataListToolbar
import empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ColumnDef
import empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction
import empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MobileDataListLayout(
    title: String,
    columns: List< ColumnDef<T>>,
    state:  ListState<T>,
    itemKey: (T) -> String,
    onAction: ( ListAction<T>) -> Unit,
    detailContent: @Composable (T) -> Unit,
    emptyContent: @Composable () -> Unit,
    newButtonLabel: String,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(modifier = modifier.fillMaxSize()) {
        DataListHeader(
            title = title,
            tabs = state.tabs,
            activeTab = state.activeTab,
            newButtonLabel = newButtonLabel,
            onTabChange = {
                onAction(
                     ListAction.ChangeTab(
                        it
                    )
                )
            },
            onNewClick = { onAction( ListAction.New()) }
        )
        DataListToolbar(
            columns = columns,
            visibleColumns = state.visibleColumns,
            onExport = {
                onAction(
                     ListAction.Export(
                        it
                    )
                )
            },
            onImport = {
                onAction(
                    ListAction.Import(
                        it
                    )
                )
            },
            onToggleColumn = {
                onAction(
                     ListAction.ToggleColumn(
                        it
                    )
                )
            }
        )
        HorizontalDivider()
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            DataListTable(
                items = state.items,
                columns = columns,
                visibleColumns = state.visibleColumns,
                sortBy = state.sortBy,
                filters = state.filters,
                selectedItem = state.selectedItem,
                selectedItems = state.selectedItems,
                showCheckboxes = false,
                itemKey = itemKey,
                onAction = onAction,
                emptyContent = emptyContent
            )
        }
        HorizontalDivider()
        DataListPagination(
            currentPage = state.currentPage,
            totalPages = state.totalPages,
            totalItems = state.totalItems,
            itemsPerPage = state.itemsPerPage,
            onPageChange = {
                onAction(
                    ListAction.ChangePage(
                        it
                    )
                )
            },
            onPageSizeChange = {
                onAction(
                     ListAction.ChangePageSize(
                        it
                    )
                )
            }
        )
    }
    DataListDetailSheet(
        item = state.selectedItem,
        isVisible = state.isDetailPanelOpen,
        sheetState = sheetState,
        onClose = { onAction( ListAction.CloseDetail()) },
        content = detailContent
    )
}
