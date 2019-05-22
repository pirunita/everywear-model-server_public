package style.everywear.synthesis.service

import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session
import org.tensorflow.Tensor
import style.everywear.synthesis.event.source.SynthesisCompleteSource
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

private val session: Session = SavedModelBundle
        .load("/Users/jun097kim/dev/CycleGAN-TensorFlow/savedmodel/1",
                "serve")
        .session()

@Service
class SynthesisService {
    companion object {
        const val UPLOAD_OUTPUT_PATH = "/Users/jun097kim/dev/everywear-model-server/uploads/output/"
    }

    @Autowired
    lateinit var synthesisCompleteSource: SynthesisCompleteSource

    fun runSavedModel(filePath: String): ByteArray {
        val file = File(filePath)
        val fileBytes = Files.readAllBytes(file.toPath())

        val inputBytes = Tensor.create(fileBytes)

        val output = session
                .runner()
                .feed("input_bytes", inputBytes)
                .fetch("output_bytes")
                .run()[0]

        println(String(Base64.encodeBase64(output.bytesValue())))

        return output.bytesValue()
    }

    fun uploadOutput(originalFilename: String, bytesArray: ByteArray) {
        val path = Paths.get(UPLOAD_OUTPUT_PATH + originalFilename)

        Files.createDirectories(path.parent)
        Files.write(path, bytesArray)
    }

    fun publishSynthesisComplete() {
        synthesisCompleteSource.publishSynthesisComplete("COMPLETE", UUID.randomUUID().toString())
    }
}