package style.everywear.fitting.service

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import style.everywear.fitting.config.FirebaseProperties
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service
class FirebaseService(settings: FirebaseProperties) {

    private val logger: Logger = LoggerFactory.getLogger(FirebaseService::class.java)

    init {
        val p: Path = Paths.get(settings.serviceAccountFile)
        try {
            val serviceAccount: InputStream = Files.newInputStream(p)

            val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build()

            FirebaseApp.initializeApp(options)

        } catch (e: IOException) {
            logger.error("Init firebase", e)
        }
    }

    fun sendMessage() {
        val registrationToken = "f4lnMxMzuJI:APA91bEuYdcTlRJY5xBVzqV8B3CYiykMzxvOV7d2aJbiAcwcjj6R6h79QImaLKB9NG0TwzU2lKFiJxO-7xT0QYyKVGEsstFxMJQgJ3duqJqbrllpQ3oQuy782Gk5FwTHGYAylP7GCKZr"

        val message: Message = Message.builder()
                .setNotification(Notification("피팅룸","합성이 완료되었습니다."))
                .putData("message", "This is a Firebase Cloud Messaging Topic Message!")
                .setToken(registrationToken)
                .build()

        val response: String = FirebaseMessaging.getInstance().send(message)
        logger.debug("Successfully sent message: {}", response)
    }
}