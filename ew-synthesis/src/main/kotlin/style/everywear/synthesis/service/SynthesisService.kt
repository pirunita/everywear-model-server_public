package style.everywear.synthesis.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.stereotype.Service
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session
import org.tensorflow.Tensor
import style.everywear.synthesis.EwSynthesisApplication
import style.everywear.synthesis.event.CustomChannels
import style.everywear.synthesis.event.model.SynthesisRequestModel
import style.everywear.synthesis.event.source.SynthesisCompleteSource
import java.io.File
import java.nio.file.Files
import java.util.*

@Service
class SynthesisService {

    @Autowired
    lateinit var synthesisCompleteSource: SynthesisCompleteSource

    val logger: Logger = LoggerFactory.getLogger(EwSynthesisApplication::class.java)

    @StreamListener(CustomChannels.INPUT)
    fun loggerSink(synthesisRequest: SynthesisRequestModel) {
        logger.debug("Received an event for User Id {}", synthesisRequest.userId)

        val result: ByteArray = runSavedModel()

        // TODO 이미지 저장

        publishSynthesisComplete()
    }

    fun runSavedModel(): ByteArray {
        val session: Session = SavedModelBundle.load("/Users/jun097kim/dev/CycleGAN-TensorFlow/savedmodel/1", "serve").session()

        val file = File("/Users/jun097kim/dev/CycleGAN-TensorFlow/samples/real_apple2orange_1.jpg")
        val fileBytes = Files.readAllBytes(file.toPath())

        val inputBytes = Tensor.create(fileBytes)

        val output = session
                .runner()
                .feed("input_bytes", inputBytes)
                .fetch("output_bytes")
                .run()[0]

        return Base64.getEncoder().encode(output.bytesValue())
    }

    fun publishSynthesisComplete() {
        synthesisCompleteSource.publishSynthesisComplete("COMPLETE", UUID.randomUUID().toString())
    }
}