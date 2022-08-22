package ru.cubly.firefly.importer.entity

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class ScriptEvaluationRequest(
    @NotEmpty
    val script: String,

    @NotNull
    val context: Map<String, String>,

    @NotNull
    val resultType: ScriptEvaluationResultType
)