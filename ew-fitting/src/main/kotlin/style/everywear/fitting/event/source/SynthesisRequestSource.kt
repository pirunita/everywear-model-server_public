package style.everywear.fitting.event.source

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import style.everywear.fitting.event.CustomChannels
import style.everywear.fitting.event.model.SynthesisRequestModel

@Component
class SynthesisRequestSource @Autowired constructor(private val source: CustomChannels) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SynthesisRequestSource::class.java)
    }

    fun publishSynthesisRequest(action: String, filePath: String, synthesisId: String) {
        logger.debug("Sending Kafka message {} for Synthesis Id: {}", action, synthesisId)

        val request = SynthesisRequestModel(
                SynthesisRequestSource::class.java.typeName,
                action,
                synthesisId,
                filePath,
                "ew-correlation-id"
        )

        source.outboundSynthesisRequest().send(MessageBuilder.withPayload(request).build())
    }
}