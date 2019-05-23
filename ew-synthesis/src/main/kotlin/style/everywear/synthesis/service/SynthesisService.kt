package style.everywear.synthesis.service

import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session
import org.tensorflow.Tensor
import style.everywear.synthesis.event.source.SynthesisCompleteSource
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
class SynthesisService {

    @Value("\${jib.extras.saved-model-path}")
    lateinit var savedModelPath: String

    private val session: Session by lazy {
        SavedModelBundle
                .load(savedModelPath,
                        "serve")
                .session()
    }

    @Value("\${jib.extras.upload-output-path}")
    lateinit var uploadOutputPath: String

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
        val path = Paths.get(uploadOutputPath + originalFilename)

        Files.createDirectories(path.parent)
        Files.write(path, bytesArray)
    }

    fun publishSynthesisComplete() {
        synthesisCompleteSource.publishSynthesisComplete("COMPLETE", UUID.randomUUID().toString())
    }
}