package style.everywear.synthesis.event.model

data class SynthesisCompleteModel(
        val type: String,
        val actionId: String,
        val synthesisId: String,
        val correlationId: String
)