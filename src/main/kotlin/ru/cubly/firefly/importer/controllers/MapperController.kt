package ru.cubly.firefly.importer.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.cubly.firefly.importer.model.MappingConfig
import ru.cubly.firefly.importer.service.MappingConfigService
import ru.cubly.firefly.importer.service.UserService

@RestController
@RequestMapping("/mappers")
class MapperController(
    private val mappingConfigService: MappingConfigService,
    private val userService: UserService
) {
    @GetMapping
    fun list() =
        userService.getUserId()
            .flatMapMany(mappingConfigService::list)

    @PostMapping
    fun create(@RequestBody mappingConfig: MappingConfig) =
        userService.getUserId()
            .flatMap { userId -> mappingConfigService.create(userId, mappingConfig) }

    @PostMapping("/{mappingConfigId}")
    fun update(@PathVariable mappingConfigId: Long, @RequestBody mappingConfig: MappingConfig) =
        userService.getUserId()
            .flatMap { userId -> mappingConfigService.update(userId, mappingConfigId, mappingConfig) }

    @DeleteMapping("/{mappingConfigId}")
    fun delete(@PathVariable mappingConfigId: Long) =
        userService.getUserId()
            .flatMap { userId -> mappingConfigService.delete(userId, mappingConfigId) }
            .map { ResponseEntity.noContent().build<Void>() }
}