package empire.digiprem.mycolowepapp.core.design_system.datalist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DataListPagination(
    currentPage: Int,
    totalPages: Int,
    totalItems: Int,
    itemsPerPage: Int,
    onPageChange: (Int) -> Unit,
    onPageSizeChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pageSizes = listOf(10, 25, 50, 100)
    var showPageSizeMenu by remember { mutableStateOf(false) }

    val startItem = if (totalItems == 0) 0 else (currentPage - 1) * itemsPerPage + 1
    val endItem = minOf(currentPage * itemsPerPage, totalItems)

    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = if (totalItems == 0) "Aucun élément" else "$startItem–$endItem sur $totalItems",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            IconButton(
                onClick = { if (currentPage > 1) onPageChange(currentPage - 1) },
                enabled = currentPage > 1,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Page précédente", modifier = Modifier.size(18.dp))
            }

            buildPageList(currentPage, totalPages).forEach { page ->
                if (page == -1) {
                    Text("…", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(horizontal = 4.dp))
                } else {
                    val isSelected = page == currentPage
                    FilledIconButton(
                        onClick = { onPageChange(page) },
                        modifier = Modifier.size(32.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary
                                             else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                           else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text(page.toString(), style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            IconButton(
                onClick = { if (currentPage < totalPages) onPageChange(currentPage + 1) },
                enabled = currentPage < totalPages,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Page suivante", modifier = Modifier.size(18.dp))
            }
        }

        Box {
            TextButton(onClick = { showPageSizeMenu = true }) {
                Text("$itemsPerPage / page", style = MaterialTheme.typography.bodySmall)
            }
            DropdownMenu(expanded = showPageSizeMenu, onDismissRequest = { showPageSizeMenu = false }) {
                pageSizes.forEach { size ->
                    DropdownMenuItem(
                        text = { Text("$size / page") },
                        onClick = { showPageSizeMenu = false; onPageSizeChange(size) }
                    )
                }
            }
        }
    }
}

private fun buildPageList(current: Int, total: Int): List<Int> {
    if (total <= 7) return (1..total).toList()
    val pages = mutableListOf<Int>()
    pages.add(1)
    if (current > 3) pages.add(-1)
    for (i in maxOf(2, current - 1)..minOf(total - 1, current + 1)) pages.add(i)
    if (current < total - 2) pages.add(-1)
    pages.add(total)
    return pages
}
