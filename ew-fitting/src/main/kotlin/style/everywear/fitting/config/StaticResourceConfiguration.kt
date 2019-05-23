package style.everywear.fitting.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class StaticResourceConfiguration: WebMvcConfigurer {

    // Serving static files
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
                .addResourceHandler("/uploads/**")
                .addResourceLocations("file:/Users/jun097kim/dev/everywear-model-server/uploads/")
    }
}