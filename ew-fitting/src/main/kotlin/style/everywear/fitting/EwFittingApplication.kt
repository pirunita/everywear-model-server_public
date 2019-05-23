package style.everywear.fitting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.annotation.EnableBinding
import style.everywear.fitting.event.CustomChannels

@SpringBootApplication
@EnableBinding(CustomChannels::class)
open class EwFittingApplication

fun main(args: Array<String>) {
    runApplication<EwFittingApplication>(*args)
}
