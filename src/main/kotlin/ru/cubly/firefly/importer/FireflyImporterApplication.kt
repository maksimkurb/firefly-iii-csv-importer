package ru.cubly.firefly.importer

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

@SpringBootApplication(
    exclude = [
        DataSourceAutoConfiguration::class,
        ReactiveUserDetailsServiceAutoConfiguration::class,
    ]
)
@EnableWebFlux
class FireflyImporterApplication

fun main(args: Array<String>) {
    runApplication<FireflyImporterApplication>(*args) {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.from(ZoneOffset.UTC)))
        this.webApplicationType = WebApplicationType.REACTIVE
    }
}
