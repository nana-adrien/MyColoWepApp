package empire.digiprem.mycoloapp.core.design_system.components.datalist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> DataListActions(
    item: T,
    onView: (T) -> Unit,
    onEdit: (T) -> Unit,
    onDelete: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        IconButton(onClick = { onView(item) }, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Filled.Visibility, "Afficher", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
        }
        IconButton(onClick = { onEdit(item) }, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Filled.Edit, "Modifier", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
        }
        IconButton(onClick = { showDeleteDialog = true }, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Filled.Delete, "Supprimer", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
        }
    }

    if (showDeleteDialog) {
        DeleteConfirmDialog(
            onConfirm = { showDeleteDialog = false; onDelete(item) },
            onDismiss = { showDeleteDialog = false }
        )
    }
}
