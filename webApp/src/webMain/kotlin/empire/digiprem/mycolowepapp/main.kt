package empire.digiprem.mycolowepapp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    PlatformComposeViewport{
        App()
    }
}