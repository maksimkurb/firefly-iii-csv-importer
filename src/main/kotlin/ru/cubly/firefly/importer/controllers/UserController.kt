package ru.cubly.firefly.importer.controllers

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.cubly.firefly.importer.entity.FireflyUser

@RestController
@RequestMapping("/api/user")
class UserController {
    @GetMapping
    fun getCurrentUser(@AuthenticationPrincipal principal: FireflyUser): Mono<ResponseEntity<FireflyUser>> {
        return Mono.just(ResponseEntity.ok(principal))
    }
}