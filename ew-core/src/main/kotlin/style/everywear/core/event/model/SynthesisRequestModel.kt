package style.everywear.core.event.model

data class SynthesisRequestModel(
        val type: String,
        val actionId: String,
        val userId: String,
        val inputImagePath: String,
        val correlationId: String
)