package ru.cubly.firefly.importer.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("raw_transaction_id")
data class RawTransaction(
    @field:Id
    @field:Column("raw_transaction_id")
    var rawTransactionId: Long?,

    @field:Column("import_id")
    var importId: Long?,

    @field:Column("firefly_transaction_id")
    var fireflyTransactionId: Long?,

    @field:Column("row_number")
    var rowNumber: Long?,

    @field:Column("data")
    var data: String?,
)