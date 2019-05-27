package style.everywear.synthesis.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
import javax.annotation.PreDestroy

@Service
class SynthesisService {

    private val logger: Logger = LoggerFactory.getLogger(SynthesisService::class.java)

    private val productPath: String = "/Users/jun097kim/dev/everywear-model-server/uploads/products/"
    private val stage1OutputPath: String = "/Users/jun097kim/dev/everywear-model-server/uploads/users/jun097kim/output/stage1/"

    private val prodImagePath = productPath + "102001_1.png"
    private val maskOutputPath = stage1OutputPath + "000001_0_102001_1_mask.png"
    private val coarseImagePath = stage1OutputPath + "000001_0_102001_1.png"
    private val tpsImagePath = stage1OutputPath + "000001_0_102001_1_gmm.png"

    @Value("\${jib.extras.saved-model.rm-path}")
    lateinit var rmSavedModelPath: String

    private val rmSession: Session by lazy {
        SavedModelBundle
                .load(rmSavedModelPath,
                        "serve")
                .session()
    }

    @Value("\${jib.extras.upload-output-path}")
    lateinit var uploadOutputPath: String

    @Autowired
    lateinit var synthesisCompleteSource: SynthesisCompleteSource
    
    fun runSavedModel(filePath: String): ByteArray {
        val startTime = System.nanoTime()
        val tensorList: List<Tensor<*>> = runRm()
        val endTime = System.nanoTime()

        val duration = (endTime - startTime) / 1000000
        logger.info("duration: $duration ms")

        return tensorList[0].bytesValue()
    }

    private fun runGmm() {}

    private fun runEdm() {}

    private fun runRm(): List<Tensor<*>> {
        val prodImageTensor = fileToTensor(prodImagePath)
        val maskOutputTensor = fileToTensor(maskOutputPath)
        val coarseImageTensor = fileToTensor(coarseImagePath)
        val tpsImageTensor = fileToTensor(tpsImagePath)

        return rmSession
                .runner()
                .feed("prod_image_holder_bytes", prodImageTensor)
                .feed("prod_mask_holder_bytes", maskOutputTensor)
                .feed("coarse_image_holder_bytes", coarseImageTensor)
                .feed("tps_image_holder_bytes", tpsImageTensor)
                .fetch("model_image_outputs_bytes")
                .fetch("select_mask_bytes")
                .run()
    }

    private fun fileToTensor(path: String): Tensor<String> {
        val file = File(path)
        val fileBytes = Files.readAllBytes(file.toPath())
        return Tensor.create(fileBytes, String::class.java)
    }

    fun uploadOutput(originalFilename: String, bytesArray: ByteArray) {
        val path = Paths.get(uploadOutputPath + originalFilename)

        Files.createDirectories(path.parent)
        Files.write(path, bytesArray)
    }

    fun publishSynthesisComplete() {
        synthesisCompleteSource.publishSynthesisComplete("COMPLETE", UUID.randomUUID().toString())
    }

    @PreDestroy
    fun closeSession() {
        rmSession.close()
    }
}