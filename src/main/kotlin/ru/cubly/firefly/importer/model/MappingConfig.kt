package ru.cubly.firefly.importer.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.ZonedDateTime

@Table("mapping_config")
data class MappingConfig(
    @Id
    @Column("mapping_config_id")
    var mappingConfigId: Long?,

    @Column("name")
    var name: String,

    @Column("description")
    var description: String?,

    @Column("user_id")
    var userId: Long,

    @Column("global")
    var global: Boolean,

    @Column("config")
    var config: String,

    @CreatedDate
    @Column("created_at")
    var createdAt: ZonedDateTime?,

    @LastModifiedDate
    @Column("updated_at")
    var updatedAt: ZonedDateTime?,
)