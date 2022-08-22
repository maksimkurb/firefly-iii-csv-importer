package ru.cubly.firefly.importer.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import ru.cubly.firefly.importer.entity.MappingConfigSpec
import java.time.ZonedDateTime
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Table("mapping_config")
data class MappingConfig(
    @field:Id
    @field:Column("mapping_config_id")
    @get:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var mappingConfigId: Long?,

    @field:Column("name")
    @field:NotNull
    var name: String?,

    @field:Column("description")
    var description: String?,

    @field:Column("user_id")
    @get:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var userId: Long?,

    @field:Column("global")
    @field:NotNull
    var global: Boolean?,

    @field:Column("config")
    @field:NotNull
    @field:Valid
    var config: MappingConfigSpec?,

    @field:CreatedDate
    @field:Column("created_at")
    @get:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var createdAt: ZonedDateTime?,

    @field:LastModifiedDate
    @field:Column("updated_at")
    @get:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var updatedAt: ZonedDateTime?,
)