package empire.digiprem.mycolowepapp.core.design_system.datalist.model

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

class ColumnDef<T>(
    val key: String,
    val header: String,
    val sortable: Boolean = true,
    val filterable: Boolean = true,
    val visible: Boolean = true,
    val width: Dp? = null,
    val render: @Composable RowScope.(T) -> Unit
)
