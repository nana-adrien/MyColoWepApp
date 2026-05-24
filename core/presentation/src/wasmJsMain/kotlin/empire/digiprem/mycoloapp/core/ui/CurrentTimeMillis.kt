package empire.digiprem.mycoloapp.core.ui

import kotlin.time.Clock

@OptIn(ExperimentalWasmJsInterop::class)
actual fun currentTimeMillis(): Long = Clock.System.now().toEpochMilliseconds()
