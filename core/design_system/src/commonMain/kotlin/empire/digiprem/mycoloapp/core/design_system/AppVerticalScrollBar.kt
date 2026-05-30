package empire.digiprem.mycoloapp.core.design_system

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import empire.digiprem.mycoloapp.core.design_system.model.AppScrollbarStyle
import empire.digiprem.mycoloapp.core.design_system.remember.rememberScrollbarStyle


@Composable
expect fun AppHorizontalScrollBar(
    scrollState:ScrollState,
    modifier: Modifier = Modifier,
    reverseLayout: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    style:AppScrollbarStyle=rememberScrollbarStyle(),
)
@Composable
expect fun AppHorizontalScrollBar(
    scrollState: LazyListState,
    modifier: Modifier = Modifier,
    reverseLayout: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    style:AppScrollbarStyle=rememberScrollbarStyle(),)
@Composable
expect fun AppHorizontalScrollBar(
    scrollState: LazyGridState,
    modifier: Modifier = Modifier,
    reverseLayout: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    style:AppScrollbarStyle=rememberScrollbarStyle(),
    )