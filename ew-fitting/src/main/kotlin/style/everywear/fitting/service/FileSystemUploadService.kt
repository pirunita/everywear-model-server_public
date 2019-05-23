package style.everywear.fitting.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.sql.Timestamp

@Service
class FileSystemUploadService {

    @Value("\${jib.extras.upload-path}")
    lateinit var uploadPath: String

    fun upload(file: MultipartFile): String {
        val bytes: ByteArray = file.bytes

        val timestamp = Timestamp(System.currentTimeMillis())
        val filename = "${timestamp.time}_${file.originalFilename}"
        val path = Paths.get(uploadPath + filename)

        Files.createDirectories(path.parent)
        Files.write(path, bytes)

        return path.toString()
    }
}