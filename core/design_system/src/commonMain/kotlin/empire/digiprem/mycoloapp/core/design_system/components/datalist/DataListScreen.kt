package empire.digiprem.mycoloapp.core.design_system.components.datalist

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import empire.digiprem.mycoloapp.core.design_system.DeviceConfiguration
import empire.digiprem.mycoloapp.core.design_system.currentDeviceConfigure
import empire.digiprem.mycoloapp.core.design_system.components.datalist.components.DefaultEmptyContent
import empire.digiprem.mycoloapp.core.design_system.components.datalist.layouts.DesktopDataListLayout
import empire.digiprem.mycoloapp.core.design_system.components.datalist.layouts.TabletDataListLayout
import empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ColumnDef
import empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction
import empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListState

@Composable
fun <T> DataListScreen(
    modifier: Modifier = Modifier,
    title: String,
    columns: List<ColumnDef<T>>,
    state: ListState<T>,
    itemKey: (T) -> String,
    onAction: (ListAction<T>) -> Unit,
    detailContent: @Composable (T) -> Unit,
    emptyContent: @Composable () -> Unit = { DefaultEmptyContent() },
    newButtonLabel: String = "Nouveau",
) {
    val device = currentDeviceConfigure()

    when (device) {
        DeviceConfiguration.MOBILE_PORTRAIT,
        DeviceConfiguration.MOBILE_LANDSCAPE -> DesktopDataListLayout(
            title = title, columns = columns, state = state, itemKey = itemKey,
            onAction = onAction, detailContent = detailContent,
            emptyContent = emptyContent, newButtonLabel = newButtonLabel, modifier = modifier
        )

        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE -> TabletDataListLayout(
            title = title,
            columns = columns,
            state = state,
            itemKey = itemKey,
            onAction = onAction,
            detailContent = detailContent,
            emptyContent = emptyContent,
            newButtonLabel = newButtonLabel,
            modifier = modifier
        )

        DeviceConfiguration.DESKTOP -> DesktopDataListLayout(
            title = title, columns = columns, state = state, itemKey = itemKey,
            onAction = onAction, detailContent = detailContent,
            emptyContent = emptyContent, newButtonLabel = newButtonLabel, modifier = modifier
        )
    }
}
