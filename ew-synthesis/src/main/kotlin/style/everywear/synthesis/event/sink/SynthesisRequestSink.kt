package style.everywear.synthesis.event.sink

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.stereotype.Component
import style.everywear.core.event.model.SynthesisRequestModel
import style.everywear.synthesis.EwSynthesisApplication
import style.everywear.synthesis.event.CustomChannels
import style.everywear.synthesis.service.SynthesisService
import java.io.File

@Component
class SynthesisRequestSink {

    @Autowired
    lateinit var synthesisService: SynthesisService

    val logger: Logger = LoggerFactory.getLogger(EwSynthesisApplication::class.java)

    @StreamListener(CustomChannels.INPUT)
    fun loggerSink(synthesisRequest: SynthesisRequestModel) {
        val inputImagePath: String = synthesisRequest.inputImagePath
        val inputImageName: String = File(inputImagePath).name

        logger.debug("Received an event for User Id {}", synthesisRequest.userId)
        logger.debug("inputImageName: {}", inputImageName)

        val output: ByteArray = synthesisService.runSavedModel(inputImagePath)

        synthesisService.uploadOutput(inputImageName, output)

        synthesisService.publishSynthesisComplete()
    }
}