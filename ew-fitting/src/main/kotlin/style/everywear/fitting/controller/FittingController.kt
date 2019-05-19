package style.everywear.fitting.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import style.everywear.fitting.service.FileSystemUploadService
import java.util.*

@RestController
@RequestMapping("/v1/fitting")
class FittingController {

    @Autowired
    val fileSystemUploadService = FileSystemUploadService()

    @RequestMapping(value = ["/"], method = [RequestMethod.POST], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun requestFitting(@RequestParam("profile") file: MultipartFile): ResponseEntity<Map<String, String>> {

        var entity: ResponseEntity<Map<String, String>>
        var response: Map<String, String>

        try {
            fileSystemUploadService.upload(file)
            response = Collections.singletonMap("message", "사진을 성공적으로 업로드하였습니다.")
            entity = ResponseEntity(response, HttpStatus.OK)

        } catch (e: Exception) {
            e.printStackTrace()
            response = Collections.singletonMap("message", "사진 업로드에 실패하였습니다.")
            entity = ResponseEntity(response, HttpStatus.BAD_REQUEST)
        }

        return entity
    }
}