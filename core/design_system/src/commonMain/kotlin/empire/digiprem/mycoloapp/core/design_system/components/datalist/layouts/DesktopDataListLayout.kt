package empire.digiprem.mycoloapp.core.design_system.components.datalist.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import empire.digiprem.mycoloapp.core.design_system.components.datalist.components.DataListDetailPanel
import empire.digiprem.mycoloapp.core.design_system.components.datalist.components.DataListHeader
import empire.digiprem.mycoloapp.core.design_system.components.datalist.components.DataListPagination
import empire.digiprem.mycoloapp.core.design_system.components.datalist.components.DataListTable
import empire.digiprem.mycoloapp.core.design_system.components.datalist.components.DataListToolbar
import empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ColumnDef
import empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction
import empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListState
import kotlinx.coroutines.delay

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
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            DataListHeader(
                title = title, tabs = state.tabs, activeTab = state.activeTab,
                newButtonLabel = newButtonLabel,
                onTabChange = {
                    onAction(
                        ListAction.ChangeTab(
                            it
                        )
                    )
                },
                onNewClick = { onAction(ListAction.New()) }
            )
            DataListToolbar(
                columns = columns, visibleColumns = state.visibleColumns,
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

        // 1. On garde le Dialog ouvert tant que le panneau est visible OU que l'animation de sortie est en cours
        val shouldShowDialog = state.isDetailPanelOpen && state.selectedItem != null

        if (shouldShowDialog) {
            Box(modifier = Modifier.fillMaxSize().blur(40.dp).background(Color.Black.copy(alpha = 0.7f)))
            Dialog(
                onDismissRequest = {
                    onAction(ListAction.CloseDetail())
                                   },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                // État interne pour déclencher l'animation d'entrée après le rendu du Dialog
                var isAnimatedVisible by remember { mutableStateOf(false) }

                var isAnimatedVisible2 by remember { mutableStateOf(false) }

                // Dès que le Dialog apparaît, on active l'animation d'entrée
                LaunchedEffect(Unit) {
                    isAnimatedVisible = true
                    isAnimatedVisible2 = true
                }
                LaunchedEffect(isAnimatedVisible) {
                    if (!isAnimatedVisible) {
                        isAnimatedVisible2 = false
                    }
                }
                LaunchedEffect(isAnimatedVisible2) {
                    if (!isAnimatedVisible2) {
                        delay(300)
                        onAction(ListAction.CloseDetail())
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                        //.blur(radius = 40.dp) // Fond assombri du Dialog
                        .clickable (
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ){
                            //onAction(ListAction.CloseDetail())
                            isAnimatedVisible = false
                         }, // Ferme si on clique à côté
                    contentAlignment = Alignment.CenterEnd
                ) {
                    AnimatedVisibility(
                        visible = isAnimatedVisible, // Contrôlé par l'état interne
                        enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                        exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut(),
                        modifier = modifier
                    ) {
                        // Bloque le clic à l'intérieur du panneau pour éviter de fermer le Dialog
                        Box(modifier = Modifier) {
                            DataListDetailPanel(
                                item = state.selectedItem,
                                isVisible = state.isDetailPanelOpen,
                                panelWidth = 400.dp,
                                onClose = {
                                    // Étape 1 : On lance d'abord l'animation de sortie
                                    isAnimatedVisible = false
                                },
                                content = detailContent,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(600.dp)
                            )
                        }
                    }
                }



               /* // Étape 2 : Une fois l'animation de sortie terminée, on ferme définitivement le Dialog
                val transition = updateTransition(targetState = isAnimatedVisible, label = "DialogTransition")
                if (!transition.currentState && !transition.targetState && !isAnimatedVisible) {
                    // On informe le ViewModel/State de détruire le Dialog
                    LaunchedEffect(Unit) {
                        onAction(ListAction.CloseDetail())
                    }
                }*/
            }
        }


    }

}
