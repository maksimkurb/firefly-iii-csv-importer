package ru.cubly.firefly.importer.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import ru.cubly.firefly.importer.model.MappingConfig

@Repository
interface MappingConfigRepository : ReactiveCrudRepository<MappingConfig, Long> {
    fun findAllByUserIdOrGlobalIsTrue(userId: Long): Flux<MappingConfig>
}