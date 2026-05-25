package empire.digiprem.mycoloapp

import androidx.compose.ui.ExperimentalComposeUiApi
import empire.digiprem.mycoloapp.di.initKoin
import kotlin.js.JsName


// wasmJsMain — quand l'app est prête
@JsName("hideLoader")
external fun hideLoader()

@OptIn(ExperimentalComposeUiApi::class)
fun main() {

    platformComposeViewport(
        configure = {
            initKoin()
        }
    ) {
        App(
            onNavHostReady = {
                onNavHostReady(it)
            }
        )
    }
    hideLoader()
}