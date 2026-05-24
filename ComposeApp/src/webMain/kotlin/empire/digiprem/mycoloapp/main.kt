package commonWebMain.kotlin.empire.digiprem.mycoloapp

import androidx.compose.ui.ExperimentalComposeUiApi
import empire.digiprem.mycoloapp.App
import empire.digiprem.mycoloapp.PlatformComposeViewport

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    PlatformComposeViewport {
        App()
    }
}