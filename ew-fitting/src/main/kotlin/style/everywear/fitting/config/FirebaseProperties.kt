package style.everywear.fitting.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "jib.extras.firebase")
@Component
class FirebaseProperties {
    lateinit var serviceAccountFile: String
}