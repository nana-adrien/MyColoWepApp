package empire.digiprem.mycoloapp.core.design_system.components.datalist.model

import androidx.compose.ui.graphics.vector.ImageVector

data class TabItem(
    val id: String,
    val label: String,
    val icon: ImageVector? = null
)
