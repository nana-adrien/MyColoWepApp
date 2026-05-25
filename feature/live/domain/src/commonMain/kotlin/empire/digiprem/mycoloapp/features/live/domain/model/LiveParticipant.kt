package empire.digiprem.mycoloapp.features.live.domain.model

data class LiveParticipant(
    val id: String,
    val name: String,
    val isHost: Boolean,
    val isSpeaking: Boolean,
    val isMicEnabled: Boolean,
    val isCameraEnabled: Boolean,
)
