package empire.digiprem.mycoloapp.features.live.domain.model

data class LiveKitWebData(
    val livekitUrl: String,
    val token: String,
    val roomName: String,
    val isPublishing: Boolean = false,
)
