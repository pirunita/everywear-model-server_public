package style.everywear.fitting.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.sql.Timestamp

@Service
class FileSystemUploadService {
    companion object {
        const val UPLOAD_PATH = "/Users/jun097kim/dev/everywear-model-server/uploads/"
    }

    fun upload(file: MultipartFile) {
        val bytes: ByteArray = file.bytes

        val timestamp = Timestamp(System.currentTimeMillis())
        val filename = "${timestamp.time}_${file.originalFilename}"
        val path = Paths.get(UPLOAD_PATH + filename)

        Files.createDirectories(path.parent)
        Files.write(path, bytes)
    }
}