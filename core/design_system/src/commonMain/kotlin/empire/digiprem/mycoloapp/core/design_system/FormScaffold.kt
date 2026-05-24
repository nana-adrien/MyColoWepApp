package empire.digiprem.mycoloapp.core.design_system

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import empire.digiprem.mycoloapp.core.design_system.extension.asString
import empire.digiprem.mycoloapp.core.domain.util.UiText

@Composable
fun FormScaffold(
    modifier: Modifier = Modifier,
    errorMessage: UiText? = null,
    onCleanErrorClick: (() -> Unit)? = null,
    footer: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
){
    val animeBoxError by animateDpAsState(
        targetValue = if (errorMessage == null) 0.dp else 46.dp
    )
    Column(
        modifier = modifier.widthIn(max = 500.dp)
            .padding(top = 20.dp)
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.End
    ){
        errorMessage?.let {
            Row(
                modifier = Modifier.fillMaxWidth().heightIn(min = animeBoxError)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.errorContainer).padding(vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    modifier = Modifier.padding(horizontal = 10.dp).size(24.dp),
                    onClick = {
                        onCleanErrorClick?.invoke()
                    }) {
                    Icon(Icons.Filled.Close, null, tint = Color.White)
                }
                Text(
                    text = it.asString(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Spacer(Modifier.height(16.dp))
        }
        content()

        Spacer(Modifier.height(4.dp))
        footer?.invoke()
        Spacer(Modifier.height(6.dp))
    }

}