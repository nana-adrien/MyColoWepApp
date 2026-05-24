package empire.digiprem.mycoloapp.core.design_system

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WebPageScaffold(
    header: @Composable ColumnScope.() -> Unit,
    footer: (@Composable () -> Unit)? = null,
    socialMedia: (@Composable () -> Unit)? = null,
    floatingButton: (@Composable () -> Unit)? = null,
    scrollState: LazyListState,
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().then(modifier),
            state = scrollState
        ) {
            content()
            item { footer?.invoke() }
        }

        Column(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter)) {
            header()
        }

        socialMedia?.let {
            Box(modifier = Modifier.wrapContentSize().align(Alignment.CenterStart)) {
                it.invoke()
            }
        }

        floatingButton?.let {
            Box(modifier = Modifier.wrapContentSize().align(Alignment.BottomEnd)) {
                it.invoke()
            }
        }

        VerticalScrollbar(
            modifier = Modifier.fillMaxHeight().align(Alignment.TopEnd).background(Color.Transparent),
            adapter = rememberScrollbarAdapter(scrollState),
            style = LocalScrollbarStyle.current.copy(
                minimalHeight = 100.dp,
                unhoverColor = MaterialTheme.colorScheme.primaryContainer,
                hoverColor = MaterialTheme.colorScheme.primary,
                hoverDurationMillis = 200
            )
        )
    }
}
