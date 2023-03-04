package ru.cubly.firefly.importer.config

import jakarta.validation.constraints.NotEmpty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "cors")
data class CorsProperties(
    @field:NotEmpty
    var allowedOrigins: List<String> = emptyList()
)