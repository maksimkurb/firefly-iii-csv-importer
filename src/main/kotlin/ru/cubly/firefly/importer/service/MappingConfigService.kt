package ru.cubly.firefly.importer.service

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.cubly.firefly.importer.exception.ForbiddenException
import ru.cubly.firefly.importer.exception.NotFoundException
import ru.cubly.firefly.importer.model.MappingConfig
import ru.cubly.firefly.importer.repository.MappingConfigRepository

@Service
class MappingConfigService(
    val mappingConfigRepository: MappingConfigRepository,
    val mappingConfigMapper: MappingConfigMapper
) {

    fun list(userId: Long) = mappingConfigRepository.findAllByUserIdOrGlobalIsTrue(userId)

    fun item(userId: Long, mapperId: Long) = mappingConfigRepository.findById(mapperId)
        .switchIfEmpty(Mono.error(NotFoundException("Mapping config with id=${mapperId} is not found")))
        .filter { userId == it.userId || it.global == true }
        .switchIfEmpty(Mono.error(ForbiddenException("Mapping config with id=${mapperId} is not global or created by you")))

    fun create(userId: Long, mappingConfig: MappingConfig): Mono<MappingConfig> {
        mappingConfig.mappingConfigId = null
        mappingConfig.userId = userId
        return mappingConfigRepository.save(mappingConfig)
    }

    fun update(userId: Long, mapperId: Long, mappingConfig: MappingConfig): Mono<MappingConfig> {
        return mappingConfigRepository.findById(mapperId)
            .switchIfEmpty(Mono.error(NotFoundException("Mapping config with id=${mapperId} is not found")))
            .map { mappingConfigMapper.update(mappingConfig, it) }
            .flatMap { mappingConfigRepository.save(it) }
    }

    fun delete(userId: Long, mapperId: Long): Mono<Void> {
        return mappingConfigRepository.findById(mapperId)
            .switchIfEmpty(Mono.error(NotFoundException("Mapping config with id=${mapperId} is not found")))
            .filter { it.userId == userId }
            .switchIfEmpty(Mono.error(ForbiddenException("Could not remove mapping config with id=${mapperId} because it is not belongs to you")))
            .flatMap { mappingConfigRepository.delete(it) }
    }

    @Mapper
    interface MappingConfigMapper {
        @Mapping(target = "mappingConfigId", ignore = true)
        @Mapping(target = "userId", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        fun update(newConfig: MappingConfig, @MappingTarget mappingConfig: MappingConfig): MappingConfig
    }
}