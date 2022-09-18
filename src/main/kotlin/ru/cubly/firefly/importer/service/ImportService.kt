package ru.cubly.firefly.importer.service

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.cubly.firefly.importer.exception.ForbiddenException
import ru.cubly.firefly.importer.exception.NotFoundException
import ru.cubly.firefly.importer.model.Import
import ru.cubly.firefly.importer.model.RawTransaction
import ru.cubly.firefly.importer.repository.ImportRepository
import ru.cubly.firefly.importer.repository.RawTransactionRepository

@Service
class ImportService(
    val importRepository: ImportRepository,
    val rawTransactionRepository: RawTransactionRepository,
    val rawTransactionService: RawTransactionService,
    val importMapper: ImportMapper,
    val mappingConfigService: MappingConfigService
) {

    fun list(userId: Long) = importRepository.findAllByUserIdOrderByCreatedAtDesc(userId)

    fun create(userId: Long, import: Import, file: Mono<FilePart>): Mono<Import> {
        import.importId = null
        import.userId = userId

        return mappingConfigService.item(userId, import.mappingConfigId!!)
            .flatMap { file }
            .flatMap { fp ->
            import.filename = fp.filename()
            importRepository.save(import)
        }
            .flatMap { savedImport ->
                return@flatMap rawTransactionService.insertTransactions(savedImport, file)
                    .collectList()
                    .flatMap { importRepository.save(savedImport) }
            }
    }

    fun item(userId: Long, importId: Long) = importRepository.findById(importId)
        .switchIfEmpty(Mono.error{NotFoundException("Import with id=${importId} is not found")})
        .filter { it.userId == userId }
        .switchIfEmpty(Mono.error{ForbiddenException("This import is not belongs to you")})

    fun itemTransactions(userId: Long, importId: Long): Flux<RawTransaction> = item(userId, importId)
        .flatMapMany { rawTransactionRepository.findByImportId(importId) }

    fun update(userId: Long, importId: Long, import: Import): Mono<Import> {
        return mappingConfigService.item(userId, import.mappingConfigId!!)
            .flatMap { item(userId, importId) }
            .map { importMapper.update(import, it) }
            .flatMap { importRepository.save(it) }
    }

    fun delete(userId: Long, importId: Long): Mono<Void> {
        return item(userId, importId)
            .flatMap { importRepository.delete(it) }
    }

    @Mapper
    interface ImportMapper {
        @Mapping(target = "importId", ignore = true)
        @Mapping(target = "filename", ignore = true)
        @Mapping(target = "userId", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "finishedAt", ignore = true)
        fun update(newImport: Import, @MappingTarget import: Import): Import
    }
}