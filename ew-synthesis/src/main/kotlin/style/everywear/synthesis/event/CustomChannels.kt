package style.everywear.synthesis.event

import org.springframework.cloud.stream.annotation.Input
import org.springframework.cloud.stream.annotation.Output
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.SubscribableChannel

interface CustomChannels {
    companion object {
        const val INPUT = "inboundSynthesisRequest"
        const val OUTPUT = "outboundSynthesisComplete"
    }

    @Input(INPUT)
    fun inboundSynthesisRequest(): SubscribableChannel

    @Output(OUTPUT)
    fun outboundSynthesisComplete(): MessageChannel
}
