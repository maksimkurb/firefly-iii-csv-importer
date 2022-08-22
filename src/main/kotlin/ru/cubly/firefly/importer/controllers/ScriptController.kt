package ru.cubly.firefly.importer.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.cubly.firefly.importer.entity.ScriptEvaluationRequest
import ru.cubly.firefly.importer.entity.ScriptEvaluationResultType
import ru.cubly.firefly.importer.service.ScriptEvaluationService

@RestController
@RequestMapping("/scripts")
class ScriptController(
    val scriptEvaluationService: ScriptEvaluationService
) {

    @PostMapping("/eval")
    fun getCurrentUser(@RequestBody scriptEvaluationRequest: ScriptEvaluationRequest): Mono<ResponseEntity<Any>> {
        val result: Mono<out Any> =  when (scriptEvaluationRequest.resultType) {
            ScriptEvaluationResultType.String -> scriptEvaluationService.evaluateString(scriptEvaluationRequest.script, scriptEvaluationRequest.context)
            ScriptEvaluationResultType.Integer -> scriptEvaluationService.evaluateInteger(scriptEvaluationRequest.script, scriptEvaluationRequest.context)
            ScriptEvaluationResultType.Double -> scriptEvaluationService.evaluateDouble(scriptEvaluationRequest.script, scriptEvaluationRequest.context)
            ScriptEvaluationResultType.Boolean -> scriptEvaluationService.evaluateBoolean(scriptEvaluationRequest.script, scriptEvaluationRequest.context)
            else -> throw IllegalArgumentException("Illegal return type provided")
        }
        return result
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty( ResponseEntity.noContent().build() )
    }
}