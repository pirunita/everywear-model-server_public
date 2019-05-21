package style.everywear.fitting.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.stereotype.Service
import style.everywear.fitting.EwFittingApplication
import style.everywear.fitting.event.CustomChannels
import style.everywear.fitting.event.model.SynthesisCompleteModel
import style.everywear.fitting.event.source.SynthesisRequestSource
import java.util.*

@Service
class FittingService {

    @Autowired
    lateinit var synthesisRequestSource: SynthesisRequestSource

    @Autowired
    lateinit var fcmClient: FirebaseService

    val logger: Logger = LoggerFactory.getLogger(EwFittingApplication::class.java)

    fun publishSynthesisRequest(filePath: String) {
        synthesisRequestSource.publishSynthesisRequest("REQUEST", filePath, UUID.randomUUID().toString())
    }

    @StreamListener(CustomChannels.INPUT)
    fun loggerSink(synthesisComplete: SynthesisCompleteModel) {
        logger.debug("Received an event for Synthesis Id {}", synthesisComplete.synthesisId)
        fcmClient.sendMessage()
    }
}