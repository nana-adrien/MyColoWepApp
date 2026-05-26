package empire.digiprem.mycoloapp.core.design_system.components.datalist.components

//import androidx.compose.foundation.HorizontalScrollbar
//import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ResizableTable(
    modifier: Modifier = Modifier,
    columns: List<ResizableColumn>,
    rows: List<List<@Composable () -> Unit>>,
    minColumnWidth: Dp = 80.dp,
    headerHeight: Dp = 48.dp,
    rowHeight: Dp = 48.dp,
    dividerColor: Color = MaterialTheme.colorScheme.outlineVariant,
    headerBackground: Color = MaterialTheme.colorScheme.surfaceVariant,
    rowBackground: Color = MaterialTheme.colorScheme.surface,
    alternateRowBackground: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
) {
    val density = LocalDensity.current
    val minWidthPx = with(density) { minColumnWidth.toPx() }

    // ✅ Largeurs en pixels — directement modifiables et observables
    val columnWidthsPx = remember(columns.size) {
        mutableStateListOf<Float>().apply {
            columns.forEach { col ->
                add(0f) // sera initialisé au premier layout
            }
        }
    }

    // Largeurs initiales en Dp converties en Px
    val initialWidths = remember(columns) {
        columns.map { it.initialWidth }
    }

    LaunchedEffect(initialWidths) {
        initialWidths.forEachIndexed { index, width ->
            if (columnWidthsPx[index] == 0f) {
                columnWidthsPx[index] = with(density) { width.toPx() }
            }
        }
    }

    val totalWidthPx = columnWidthsPx.sum()

    Box(modifier = modifier) {
       /* HorizontalScrollbar(
            adapter = rememberScrollbarAdapter(rememberScrollState()),
            modifier = Modifier.align(Alignment.BottomCenter)
        )*/

        val scrollState = rememberScrollState()
        val verticalScrollState = rememberScrollState()

        Column(modifier = Modifier.fillMaxSize()) {

            // ============================================
            // EN-TÊTE — colonnes redimensionnables
            // ============================================
            Box(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .height(headerHeight)
                    .background(headerBackground)
            ) {
                Row(
                    modifier = Modifier
                        .width(with(density) { totalWidthPx.toDp() })
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    columns.forEachIndexed { colIndex, column ->
                        val widthPx = columnWidthsPx.getOrElse(colIndex) { 0f }
                        if (widthPx <= 0f) return@forEachIndexed

                        // Cellule d'en-tête
                        Box(
                            modifier = Modifier
                                .width(with(density) { widthPx.toDp() })
                                .fillMaxHeight()
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            column.header()
                        }

                        // Diviseur redimensionnable entre colonnes
                        if (colIndex < columns.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .fillMaxHeight()
                                    .background(dividerColor)
                                    .pointerHoverIcon(
                                        PointerIcon.Hand,
                                    )
                                    .pointerInput(colIndex, columnWidthsPx.toList()) {
                                        awaitEachGesture {
                                            val down = awaitFirstDown(requireUnconsumed = false)
                                            down.consume()

                                            var totalDragX = 0f

                                            while (true) {
                                                val event = awaitPointerEvent(
                                                    pass = PointerEventPass.Main
                                                )
                                                val change = event.changes
                                                    .firstOrNull { it.id == down.id }
                                                    ?: break

                                                if (!change.pressed) break

                                                val dragX = change.positionChange().x
                                                if (dragX == 0f) {
                                                    change.consume()
                                                    continue
                                                }

                                                totalDragX += dragX
                                                change.consume()

                                                val currentWidth = columnWidthsPx[colIndex]
                                                val nextWidth = columnWidthsPx[colIndex + 1]

                                                val newCurrentWidth = (currentWidth + dragX)
                                                    .coerceAtLeast(minWidthPx)
                                                val newNextWidth = (nextWidth - dragX)
                                                    .coerceAtLeast(minWidthPx)

                                                // ✅ Applique uniquement si les deux
                                                //    respectent le minimum
                                                if (newCurrentWidth >= minWidthPx &&
                                                    newNextWidth >= minWidthPx
                                                ) {
                                                    columnWidthsPx[colIndex] = newCurrentWidth
                                                    columnWidthsPx[colIndex + 1] = newNextWidth
                                                }
                                            }
                                        }
                                    }
                            )
                        }
                    }
                }

                // Ligne de séparation bas de l'en-tête
                Divider(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    color = dividerColor,
                    thickness = 1.dp
                )
            }

            // ============================================
            // CORPS — lignes de données
            // ============================================
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .horizontalScroll(scrollState) // ✅ même scroll que l'en-tête
                        .verticalScroll(verticalScrollState)
                        .width(with(density) { totalWidthPx.toDp() })
                ) {
                    rows.forEachIndexed { rowIndex, rowCells ->
                        val bg = if (rowIndex % 2 == 0) rowBackground
                                 else alternateRowBackground

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(rowHeight)
                                .background(bg),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            rowCells.forEachIndexed { colIndex, cell ->
                                val widthPx = columnWidthsPx.getOrElse(colIndex) { 0f }
                                if (widthPx <= 0f) return@forEachIndexed

                                // Cellule de données
                                Box(
                                    modifier = Modifier
                                        .width(with(density) { widthPx.toDp() })
                                        .fillMaxHeight()
                                        .padding(horizontal = 12.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    cell()
                                }

                                // Diviseur vertical entre cellules
                                if (colIndex < rowCells.size - 1) {
                                    Box(
                                        modifier = Modifier
                                            .width(4.dp)
                                            .fillMaxHeight()
                                            .background(dividerColor)
                                    )
                                }
                            }
                        }

                        // Diviseur horizontal entre lignes
                        Divider(color = dividerColor, thickness = 0.5.dp)
                    }
                }

                // Scrollbar verticale
               /* VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(verticalScrollState),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                )*/
            }
        }
    }
}

// ============================================
// MODÈLE DE COLONNE
// ============================================
data class ResizableColumn(
    val key: String,
    val initialWidth: Dp = 150.dp,
    val header: @Composable () -> Unit,
)