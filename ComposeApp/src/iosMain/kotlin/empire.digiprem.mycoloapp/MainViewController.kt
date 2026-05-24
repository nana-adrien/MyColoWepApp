package octopusfx.client.mobile.boxoffice

import androidx.compose.ui.window.ComposeUIViewController
import octopusfx.client.mobile.boxoffice.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {initKoin()}
) { App() }