package ru.cubly.firefly.importer.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("raw_transaction_id")
data class RawTransaction(
    @Id
    @Column("raw_transaction_id")
    var rawTransactionId: Long,

    @Column("import_id")
    var importId: Long,

    @Column("firefly_transaction_id")
    var fireflyTransactionId: Long,

    @Column("row_number")
    var rowNumber: Long,

    @Column("data")
    var data: String,
)