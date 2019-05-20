package style.everywear.synthesis.event.source

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import style.everywear.synthesis.event.CustomChannels
import style.everywear.synthesis.event.model.SynthesisCompleteModel

@Component
class SynthesisCompleteSource @Autowired constructor(private val source: CustomChannels) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SynthesisCompleteSource::class.java)
    }

    fun publishSynthesisComplete(action: String, synthesisId: String) {
        logger.debug("Sending Kafka message {} for Synthesis Id: {}", action, synthesisId)

        val complete = SynthesisCompleteModel(
                SynthesisCompleteSource::class.java.typeName,
                action,
                synthesisId,
                "ew-correlation-id"
        )

        source.outboundSynthesisComplete().send(MessageBuilder.withPayload(complete).build())
    }
}