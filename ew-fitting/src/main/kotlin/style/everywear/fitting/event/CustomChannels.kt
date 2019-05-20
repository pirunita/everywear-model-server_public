package style.everywear.fitting.event

import org.springframework.cloud.stream.annotation.Input
import org.springframework.cloud.stream.annotation.Output
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.SubscribableChannel

interface CustomChannels {
    companion object {
        const val INPUT = "inboundSynthesisComplete"
        const val OUTPUT = "outboundSynthesisRequest"
    }

    @Input(INPUT)
    fun inboundSynthesisComplete(): SubscribableChannel

    @Output(OUTPUT)
    fun outboundSynthesisRequest(): MessageChannel
}