package ru.cubly.firefly.importer.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.ZonedDateTime

@Table("import")
data class Import(
    @Id
    @Column("import_id")
    var importId: Long,

    @Column("filename")
    var filename: String,

    @Column("user_id")
    var userId: Long,

    @Column("mapping_config_id")
    var mappingConfigId: Long,

    @CreatedDate
    @Column("created_at")
    var createdAt: ZonedDateTime?,

    @LastModifiedDate
    @Column("updated_at")
    var updatedAt: ZonedDateTime?,

    @Column("finished_at")
    var finishedAt: ZonedDateTime?,
)