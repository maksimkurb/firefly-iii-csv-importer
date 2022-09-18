package ru.cubly.firefly.importer.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import ru.cubly.firefly.importer.model.Import

@Repository
interface ImportRepository : ReactiveCrudRepository<Import, Long> {
    fun findAllByUserIdOrderByCreatedAtDesc(userId: Long): Flux<Import>
}