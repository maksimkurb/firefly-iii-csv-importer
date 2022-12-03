package ru.cubly.firefly.importer.entity

import com.fasterxml.jackson.annotation.JsonProperty
import ru.cubly.firefly.model.TransactionTypeProperty
import jakarta.validation.Valid
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.NotNull

data class MappingConfigSpec(
    @field:NotNull @field:Valid var applyRules: Boolean?,
    @field:NotNull @field:Valid var fireWebhooks: Boolean?,

    @field:NotNull @field:Valid var type: ValueSpec<TransactionTypeProperty>?,
    @field:NotNull @field:Valid var date: ValueSpec<String>?,
    @field:NotNull @field:Valid var amount: ValueSpec<String>?,
    @field:NotNull @field:Valid var description: ValueSpec<String>?,

    @field:Valid var currencyId: ValueSpec<Int>?,
    @field:Valid var currencyCode: ValueSpec<String>?,
    @field:Valid var foreignAmount: ValueSpec<String>?,
    @field:Valid var foreignCurrencyId: ValueSpec<Int>?,
    @field:Valid var foreignCurrencyCode: ValueSpec<String>?,
    @field:Valid var budgetId: ValueSpec<Int>?,
    @field:Valid var categoryId: ValueSpec<Int>?,
    @field:Valid var categoryName: ValueSpec<String>?,
    @field:Valid var sourceId: ValueSpec<Int>?,
    @field:Valid var sourceName: ValueSpec<String>?,
    @field:Valid var destinationId: ValueSpec<String>?,
    @field:Valid var destinationName: ValueSpec<String>?,
    @field:Valid var piggyBankId: ValueSpec<Int>?,
    @field:Valid var piggyBankName: ValueSpec<String>?,
    @field:Valid var billId: ValueSpec<Int>?,
    @field:Valid var billName: ValueSpec<String>?,
    @field:Valid var tags: ValueSpec<String>?,
    @field:Valid var notes: ValueSpec<String>?,
    @field:Valid var internalReference: ValueSpec<String>?,
    @field:Valid var externalId: ValueSpec<String>?,
    @field:Valid var externalUrl: ValueSpec<String>?,
    @field:Valid var bunqPaymentId: ValueSpec<Int>?,
    @field:Valid var sepaCc: ValueSpec<String>?,
    @field:Valid var sepaCtOp: ValueSpec<String>?,
    @field:Valid var sepaCtId: ValueSpec<String>?,
    @field:Valid var sepaDb: ValueSpec<String>?,
    @field:Valid var sepaCountry: ValueSpec<String>?,
    @field:Valid var sepaEp: ValueSpec<String>?,
    @field:Valid var sepaCi: ValueSpec<String>?,
    @field:Valid var sepaBatchId: ValueSpec<String>?,
    @field:Valid var interestDate: ValueSpec<String>?,
    @field:Valid var bookDate: ValueSpec<String>?,
    @field:Valid var processDate: ValueSpec<String>?,
    @field:Valid var dueDate: ValueSpec<String>?,
    @field:Valid var paymentDate: ValueSpec<String>?,
    @field:Valid var invoiceDate: ValueSpec<String>?,
) {
    class ValueSpec<T>(
        var constant: T?,
        var field: String?,
        var fieldTrimming: Boolean?,
        var script: String?,
    ) {
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @AssertTrue(message = "You must provide either a 'constant' or 'field' and 'fieldTrimming' or 'script' for value spec")
        fun getValid(): Boolean = constant != null || (field != null && fieldTrimming != null) || script != null
    }
}