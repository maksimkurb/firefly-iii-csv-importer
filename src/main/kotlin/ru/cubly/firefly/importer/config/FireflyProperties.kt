package ru.cubly.firefly.importer.config

import org.hibernate.validator.constraints.URL
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.stereotype.Component
import javax.validation.constraints.NotEmpty

@Component
@ConstructorBinding
@ConfigurationProperties(prefix = "firefly-iii")
class FireflyProperties {
    private val baseUrl: @URL @NotEmpty String? = null
}