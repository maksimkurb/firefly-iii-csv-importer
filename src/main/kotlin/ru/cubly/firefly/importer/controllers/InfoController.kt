package ru.cubly.firefly.importer.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.cubly.firefly.importer.config.FireflyProperties

@RestController
@RequestMapping("/api/info")
class InfoController(
    val fireflyProperties: FireflyProperties
) {
    @GetMapping("/firefly-instance")
    fun getFireflyInstanceUrl(): Mono<ResponseEntity<String>> {
        return Mono.just(ResponseEntity.ok(fireflyProperties.baseUrl))
    }
}