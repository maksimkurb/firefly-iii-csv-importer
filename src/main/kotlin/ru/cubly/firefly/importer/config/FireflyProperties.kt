package ru.cubly.firefly.importer.config

import org.hibernate.validator.constraints.URL
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import javax.validation.constraints.NotEmpty

@Configuration
@ConfigurationProperties(prefix = "firefly-iii")
data class FireflyProperties(
    @field:URL @field:NotEmpty
    var baseUrl: String? = null
)