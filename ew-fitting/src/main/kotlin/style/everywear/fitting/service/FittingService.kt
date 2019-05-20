package style.everywear.fitting.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import style.everywear.fitting.event.source.SynthesisRequestSource
import java.util.*

@Service
class FittingService {

    @Autowired
    lateinit var synthesisRequestSource: SynthesisRequestSource

    fun publishSynthesisRequest() {
        synthesisRequestSource.publishSynthesisRequest("REQUEST", UUID.randomUUID().toString())
    }
}