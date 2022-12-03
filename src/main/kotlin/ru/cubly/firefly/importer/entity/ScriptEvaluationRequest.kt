package ru.cubly.firefly.importer.entity

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class ScriptEvaluationRequest(
    @NotEmpty
    val script: String,

    @NotNull
    val context: Map<String, String>,

    @NotNull
    val resultType: ScriptEvaluationResultType
)