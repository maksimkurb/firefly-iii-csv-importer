package ru.cubly.firefly.importer.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import ru.cubly.firefly.importer.model.RawTransaction

@Repository
interface RawTransactionRepository : ReactiveCrudRepository<RawTransaction, Long> {
    fun findByImportId(importId: Long): Flux<RawTransaction>
}