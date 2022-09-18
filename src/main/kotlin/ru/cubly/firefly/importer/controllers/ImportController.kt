package ru.cubly.firefly.importer.controllers

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Encoding
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import ru.cubly.firefly.importer.model.Import
import ru.cubly.firefly.importer.service.ImportService
import ru.cubly.firefly.importer.service.RawTransactionService
import ru.cubly.firefly.importer.service.UserService
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/imports")
class ImportController(
    private val importService: ImportService,
    private val rawTransactionService: RawTransactionService,
    private val userService: UserService
) {
    @GetMapping
    fun list() =
        userService.getUserId()
            .flatMapMany(importService::list)

    @GetMapping("/{importId}")
    fun item(
        @Valid @NotNull @PathVariable importId: Long,
    ) =
        userService.getUserId()
            .flatMap { userId -> importService.item(userId, importId) }


    @GetMapping("/{importId}/transactions")
    fun itemTransactions(
        @Valid @NotNull @PathVariable importId: Long,
    ) =
        userService.getUserId()
            .flatMapMany { userId -> importService.itemTransactions(userId, importId) }


    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = [
            Content(
                encoding = [Encoding(name = "import", contentType = MediaType.APPLICATION_JSON_VALUE)],
                mediaType = MediaType.MULTIPART_FORM_DATA_VALUE
            )
        ]
    )
    @PostMapping(consumes = ["multipart/form-data"])
    fun create(
        @Valid @NotNull @RequestPart import: Import,
        @NotNull @RequestPart file: Mono<FilePart>
    ) =
        userService.getUserId()
            .flatMap { userId -> importService.create(userId, import, file) }

    @PostMapping("/{importId}")
    fun update(
        @Valid @NotNull @PathVariable importId: Long,
        @Valid @NotNull @RequestBody import: Import
    ) =
        userService.getUserId()
            .flatMap { userId -> importService.update(userId, importId, import) }

    @DeleteMapping("/{importId}")
    fun delete(@Valid @NotNull @PathVariable importId: Long) =
        userService.getUserId()
            .flatMap { userId -> importService.delete(userId, importId) }
            .map { ResponseEntity.noContent().build<Void>() }
}