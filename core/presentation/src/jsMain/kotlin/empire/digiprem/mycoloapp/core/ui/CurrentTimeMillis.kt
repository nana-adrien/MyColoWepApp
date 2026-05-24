package empire.digiprem.mycoloapp.core.ui

actual fun currentTimeMillis(): Long = js("Date.now()").unsafeCast<Double>().toLong()
