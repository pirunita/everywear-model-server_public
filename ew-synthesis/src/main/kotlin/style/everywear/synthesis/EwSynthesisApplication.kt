package style.everywear.synthesis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.annotation.EnableBinding
import style.everywear.synthesis.event.CustomChannels

@SpringBootApplication
@EnableBinding(CustomChannels::class)   // 메시지 브로커와 바인딩
open class EwSynthesisApplication

fun main(args: Array<String>) {
    runApplication<EwSynthesisApplication>(*args)
}
