package empire.digiprem.mycoloapp

import androidx.compose.ui.window.ComposeUIViewController
import empire.digiprem.mycoloapp.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }