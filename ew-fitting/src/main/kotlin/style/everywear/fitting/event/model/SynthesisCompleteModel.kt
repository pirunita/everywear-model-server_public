package style.everywear.fitting.event.model

data class SynthesisCompleteModel(
        val type: String,
        val actionId: String,
        val synthesisId: String,
        val correlationId: String
)