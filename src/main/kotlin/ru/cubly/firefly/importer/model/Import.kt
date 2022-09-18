package ru.cubly.firefly.importer.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.ZonedDateTime
import javax.validation.constraints.NotNull

@Table("import")
data class Import(
    @field:Id
    @field:Column("import_id")
    @get:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var importId: Long?,

    @field:Column("filename")
    @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var filename: String?,

    @field:Column("user_id")
    @field:JsonIgnore
    var userId: Long?,

    @field:Column("mapping_config_id")
    @field:NotNull
    var mappingConfigId: Long?,

    @field:CreatedDate
    @field:Column("created_at")
    @get:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var createdAt: ZonedDateTime?,

    @field:LastModifiedDate
    @field:Column("updated_at")
    @get:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var updatedAt: ZonedDateTime?,

    @field:Column("finished_at")
    @get:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var finishedAt: ZonedDateTime?,
)