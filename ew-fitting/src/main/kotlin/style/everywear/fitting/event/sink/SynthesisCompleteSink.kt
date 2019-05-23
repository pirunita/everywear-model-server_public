package style.everywear.fitting.event.sink

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.stereotype.Component
import style.everywear.core.event.model.SynthesisCompleteModel
import style.everywear.fitting.EwFittingApplication
import style.everywear.fitting.event.CustomChannels
import style.everywear.fitting.service.FirebaseService

@Component
class SynthesisCompleteSink {

    @Autowired
    lateinit var fcmClient: FirebaseService

    val logger: Logger = LoggerFactory.getLogger(EwFittingApplication::class.java)

    @StreamListener(CustomChannels.INPUT)
    fun loggerSink(synthesisComplete: SynthesisCompleteModel) {
        logger.debug("Received an event for Synthesis Id {}", synthesisComplete.synthesisId)
        fcmClient.sendMessage()
    }
}