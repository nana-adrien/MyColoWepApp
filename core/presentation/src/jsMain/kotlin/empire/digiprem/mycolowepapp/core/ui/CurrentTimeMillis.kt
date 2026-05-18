package empire.digiprem.mycolowepapp.core.ui

actual fun currentTimeMillis(): Long = js("Date.now()").unsafeCast<Double>().toLong()
