package ru.cubly.firefly.importer.service

import com.eclipsesource.v8.*
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Service
class ScriptEvaluationService {
    fun evaluateString(script: String, context: Map<String, String>) = evaluate<String>(script, context)
    fun evaluateInteger(script: String, context: Map<String, String>) = evaluate<Int>(script, context)
    fun evaluateDouble(script: String, context: Map<String, String>) = evaluate<Double>(script, context)
    fun evaluateBoolean(script: String, context: Map<String, String>) = evaluate<Boolean>(script, context)

    private inline fun <reified T : Any> evaluate(script: String, context: Map<String, String>): Mono<T> {
        var v8Runtime: V8? = null
        var v8Context: V8Array? = null
        try {
            v8Runtime = V8.createV8Runtime()
            v8Context = V8Array(v8Runtime)

            for (entry in context.entries) {
                v8Context.add(entry.key, entry.value)
            }

            v8Runtime.add("ctx", v8Context)
            v8Runtime.registerJavaMethod(this::parseDate, "parseDate")
            v8Runtime.registerJavaMethod(this::parseDateTime, "parseDateTime")

            val value = v8Runtime.executeScript(script) ?: return Mono.empty()
            if (value !is T) {
                throw IllegalStateException("Invalid return type: expected ${T::class.simpleName}, but returned ${value::class.simpleName}")
            }
            return Mono.just(value)
        } finally {
            v8Context?.release()
            v8Runtime?.release()
        }
    }

    /**
     * Converts date to ISO string
     */
    private fun parseDate(receiver: V8Object, parameters: V8Array): String {
        if (parameters.length() < 2) {
            throw IllegalArgumentException("Two arguments are required for parseDate(dateString, pattern) method")
        }
        val dateString = parameters.getString(0)
        val pattern = parameters.getString(1)

        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)

        return LocalDate.parse(dateString, formatter).atStartOfDay(ZoneId.systemDefault())
            .format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * Converts datetime to ISO string
     */
    private fun parseDateTime(receiver: V8Object, parameters: V8Array): String {
        if (parameters.length() < 2) {
            throw IllegalArgumentException("Two arguments are required for parseDate(dateString, pattern) method")
        }
        val dateString = parameters.getString(0)
        val pattern = parameters.getString(1)

        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)

        return ZonedDateTime.parse(dateString, formatter).format(DateTimeFormatter.ISO_DATE_TIME)
    }
}