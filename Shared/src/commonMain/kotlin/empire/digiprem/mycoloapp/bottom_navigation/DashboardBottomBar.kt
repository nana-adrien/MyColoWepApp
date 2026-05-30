package empire.digiprem.mycoloapp.bottom_navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import empire.digiprem.mycoloapp.core.design_system.extension.asString
import empire.digiprem.mycoloapp.core.domain.util.Platform
import empire.digiprem.mycoloapp.core.domain.util.getCurrentPlatform
import empire.digiprem.mycoloapp.home.IAppBottomBarNavItem

@Composable
fun<T: IAppBottomBarNavItem> DashboardBottomBar(
    selectedItem: T,
    items: List<T>,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val rowModifier= when(getCurrentPlatform()) {
        Platform.IOS -> {
            Modifier
                .padding(horizontal = 26.dp,)
                .padding(vertical = 12.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .then(modifier)
        }
        else-> Modifier.fillMaxWidth().then(modifier)
    }
    BottomAppBar(
        windowInsets = WindowInsets(0.dp),
        contentPadding = PaddingValues(0.dp),
        containerColor = MaterialTheme.colorScheme.surface,
      modifier = rowModifier,
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                //.navigationBarsPadding(),
              .height(72.dp),
            //.border(width = 1.dp, color = BottomBorder, shape = RoundedCornerShape(0.dp)),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment     = Alignment.CenterVertically,
        ) {
            items.forEach { item ->
                BottomNavItemView(
                    label      = item.label.asString(),
                    icon=item.icon,
                    isSelected = item == selectedItem,
                    onClick    = { onItemSelected(item) },
                )
            }
        }
    }

}
