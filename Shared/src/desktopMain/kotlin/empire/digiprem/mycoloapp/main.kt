package empire.digiprem.mycoloapp

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import empire.digiprem.mycoloapp.di.initKoin
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource
import java.awt.Dimension

fun main() {
    initKoin()
    application {
        Window(
            state = rememberWindowState(),
            onCloseRequest = ::exitApplication,
            title = stringResource(Res.string.app_name),
        ) {
            LaunchedEffect(Unit) {
                window.minimumSize = Dimension(900, 600)
            }
            App()
        }
    }
}
