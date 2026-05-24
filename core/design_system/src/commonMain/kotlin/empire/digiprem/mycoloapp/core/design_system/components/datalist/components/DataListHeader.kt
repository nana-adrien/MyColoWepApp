package empire.digiprem.mycoloapp.core.design_system.components.datalist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DataListHeader(
    title: String,
    tabs: List<empire.digiprem.mycoloapp.core.design_system.components.datalist.model.TabItem>,
    activeTab: String,
    newButtonLabel: String,
    onTabChange: (String) -> Unit,
    onNewClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Button(onClick = onNewClick, contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                Icon(Icons.Filled.Add, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(newButtonLabel, style = MaterialTheme.typography.labelMedium)
            }
        }
        if (tabs.isNotEmpty()) {
            ScrollableTabRow(
                selectedTabIndex = tabs.indexOfFirst { it.id == activeTab }.coerceAtLeast(0),
                edgePadding = 16.dp,
                divider = {}
            ) {
                tabs.forEach { tab ->
                    Tab(
                        selected = tab.id == activeTab,
                        onClick = { onTabChange(tab.id) },
                        text = { Text(tab.label, style = MaterialTheme.typography.labelMedium) },
                        icon = tab.icon?.let { icon -> { Icon(icon, null, modifier = Modifier.size(18.dp)) } }
                    )
                }
            }
            HorizontalDivider()
        }
    }
}
