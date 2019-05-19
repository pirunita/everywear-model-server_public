package style.everywear.synthesis.controller

import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session
import org.tensorflow.Tensor
import java.io.File
import java.nio.file.Files

@RestController
@RequestMapping("/v1/synthesis")
class SynthesisController {

    @PostMapping
    fun getSynthesisImage(): ByteArray {
        val session: Session = SavedModelBundle.load("/Users/jun097kim/dev/CycleGAN-TensorFlow/savedmodel/1", "serve").session()

        val file = File("/Users/jun097kim/dev/CycleGAN-TensorFlow/samples/real_apple2orange_1.jpg")
        val fileBytes = Files.readAllBytes(file.toPath())

        val inputBytes = Tensor.create(fileBytes)

        val output = session
                .runner()
                .feed("input_bytes", inputBytes)
                .fetch("output_bytes")
                .run()[0]

        val byteArray: ByteArray = Base64.encodeBase64(output.bytesValue())
        return byteArray
    }
}