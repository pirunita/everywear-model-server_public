package style.everywear.synthesis.event.model

data class SynthesisRequestModel(
        val type: String,
        val actionId: String,
        val userId: String,
        val correlationId: String
)