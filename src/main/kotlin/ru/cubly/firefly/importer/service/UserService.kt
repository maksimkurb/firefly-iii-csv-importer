package ru.cubly.firefly.importer.service

import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.cubly.firefly.importer.entity.FireflyUser

@Service
class UserService {
    fun getUserId(): Mono<Long> = ReactiveSecurityContextHolder.getContext()
        .map { ctx: SecurityContext -> (ctx.authentication.principal as FireflyUser).getUserId() }
}