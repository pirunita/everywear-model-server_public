package style.everywear.fitting

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.StreamListener
import style.everywear.fitting.event.CustomChannels
import style.everywear.fitting.event.model.SynthesisCompleteModel

@SpringBootApplication
@EnableBinding(CustomChannels::class)
class EwFittingApplication {

    val logger: Logger = LoggerFactory.getLogger(EwFittingApplication::class.java)

    @StreamListener(CustomChannels.INPUT)
    fun loggerSink(synthesisComplete: SynthesisCompleteModel) {
        logger.debug("Received an event for Synthesis Id {}", synthesisComplete.synthesisId)
    }
}

fun main(args: Array<String>) {
    runApplication<EwFittingApplication>(*args)
}
