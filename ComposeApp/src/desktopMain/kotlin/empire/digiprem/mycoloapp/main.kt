package empire.digiprem.mycoloapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import empire.digiprem.mycoloapp.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "My Colo",
        ) {
            App()
        }
    }
}
