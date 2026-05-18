package empire.digiprem.mycolowepapp.core.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

enum class  PortfolioIconButtonType{
    primary,
    secondary,
}
@Composable
fun  PortfolioIconButton(
    modifier: Modifier = Modifier,
    model: Any,
    type: PortfolioIconButtonType = PortfolioIconButtonType.primary,
    tint: Color=Color.White,
    onClick: () -> Unit,
) {

    val backgroundColor = when(type){
        PortfolioIconButtonType.primary -> { MaterialTheme.colorScheme.primary
        }
        PortfolioIconButtonType.secondary -> {
            Color.Transparent
        }
    }


    Box(
        modifier = modifier.size(35.dp)
            .pointerHoverIcon(PointerIcon.Hand)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable {
                onClick()
            }
            .padding(8.dp),
        contentAlignment = Alignment.Center) {
        PortfolioIcon(
            modifier = Modifier.fillMaxSize(),
            model = model,
        )
    }

}

@Composable
fun PortfolioIcon(
    modifier: Modifier = Modifier,
    model: Any,
    tint: Color?=Color.White,
){
    when (model) {
        is ImageVector->{
            Icon(
                modifier = modifier,
                imageVector = model,
                contentDescription = null,
                tint = tint?:Color.White,
            )
        }
        is Painter->{
            Icon(
                modifier = modifier,
                painter = model,
                contentDescription = null,
                tint = tint?:Color.White,
            )
        }
        else -> {
            AsyncImage(
                modifier = modifier,
                model = model,
                contentDescription = null,
                contentScale = ContentScale.Inside,
                colorFilter = tint?.let {ColorFilter.tint(tint)} ,
            )
        }
    }
}