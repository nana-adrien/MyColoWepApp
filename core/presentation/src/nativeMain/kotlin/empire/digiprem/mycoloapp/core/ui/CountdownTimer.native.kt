package empire.digiprem.mycoloapp.core.ui

import kotlin.time.Clock

actual fun currentTimeMillis(): Long = Clock.System.now().toEpochMilliseconds()