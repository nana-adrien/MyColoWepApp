package empire.digiprem.mycolowepapp.core.design_system.datalist

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import empire.digiprem.mycolowepapp.core.design_system.DeviceConfiguration
import empire.digiprem.mycolowepapp.core.design_system.currentDeviceConfigure
import empire.digiprem.mycolowepapp.core.design_system.datalist.components.DefaultEmptyContent
import empire.digiprem.mycolowepapp.core.design_system.datalist.layouts.DesktopDataListLayout
import empire.digiprem.mycolowepapp.core.design_system.datalist.layouts.MobileDataListLayout
import empire.digiprem.mycolowepapp.core.design_system.datalist.layouts.TabletDataListLayout
import empire.digiprem.mycolowepapp.core.design_system.datalist.model.ColumnDef
import empire.digiprem.mycolowepapp.core.design_system.datalist.model.ListAction
import empire.digiprem.mycolowepapp.core.design_system.datalist.model.ListState

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
        DeviceConfiguration.MOBILE_LANDSCAPE -> MobileDataListLayout(
            title = title, columns = columns, state = state, itemKey = itemKey,
            onAction = onAction, detailContent = detailContent,
            emptyContent = emptyContent, newButtonLabel = newButtonLabel, modifier = modifier
        )
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE -> TabletDataListLayout(
            title = title, columns = columns, state = state, itemKey = itemKey,
            onAction = onAction, detailContent = detailContent,
            emptyContent = emptyContent, newButtonLabel = newButtonLabel, modifier = modifier
        )
        DeviceConfiguration.DESKTOP -> DesktopDataListLayout(
            title = title, columns = columns, state = state, itemKey = itemKey,
            onAction = onAction, detailContent = detailContent,
            emptyContent = emptyContent, newButtonLabel = newButtonLabel, modifier = modifier
        )
    }
}
