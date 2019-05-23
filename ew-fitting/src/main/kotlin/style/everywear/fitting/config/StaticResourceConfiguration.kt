package style.everywear.fitting.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class StaticResourceConfiguration: WebMvcConfigurer {

    @Value("\${jib.extras.upload-path}")
    lateinit var uploadPath: String

    // Serving static files
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
                .addResourceHandler("/uploads/**")
                .addResourceLocations("file:$uploadPath")
    }
}