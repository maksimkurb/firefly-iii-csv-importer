package ru.cubly.firefly.importer.config

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.MySqlDialect
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import java.io.IOException
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*


@Configuration
@EnableR2dbcRepositories
@EnableR2dbcAuditing(dateTimeProviderRef = "dateTimeProvider")
class R2dbcConfig {
    @Bean("dateTimeProvider")
    fun dateTimeProvider(): DateTimeProvider = DateTimeProvider {
        Optional.of(OffsetDateTime.now())
    }

    @Bean
    fun customConversions(objectMapper: ObjectMapper): R2dbcCustomConversions {
        return R2dbcCustomConversions.of(
            MySqlDialect.INSTANCE,
            listOf(
                StringToJsonNodeConverter(objectMapper),
                JsonNodeToStringConverter(objectMapper),
                TimeStampToOffsetDateTimeConverter(),
                OffsetDateTimeToTimeStampConverter(),
            )
        )
    }

    @ReadingConverter
    internal class StringToJsonNodeConverter(objectMapper: ObjectMapper) : Converter<String, JsonNode> {
        private val log: Logger = LoggerFactory.getLogger(StringToJsonNodeConverter::class.java)

        private val objectMapper: ObjectMapper

        init {
            this.objectMapper = objectMapper
        }

        override fun convert(string: String): JsonNode {
            try {
                return objectMapper.readTree(string)
            } catch (e: IOException) {
                log.error("Problem while parsing JSON: {}", string, e)
            }
            return objectMapper.createObjectNode()
        }
    }

    @WritingConverter
    internal class JsonNodeToStringConverter(objectMapper: ObjectMapper) : Converter<JsonNode, String> {
        private val log: Logger = LoggerFactory.getLogger(JsonNodeToStringConverter::class.java)

        private val objectMapper: ObjectMapper

        init {
            this.objectMapper = objectMapper
        }

        override fun convert(source: JsonNode): String {
            try {
                return objectMapper.writeValueAsString(source)
            } catch (e: JsonProcessingException) {
                log.error("Error occurred while serializing map to JSON: {}", source, e)
            }
            return ""
        }
    }


    @WritingConverter
    class OffsetDateTimeToTimeStampConverter : Converter<ZonedDateTime, LocalDateTime> {
        override fun convert(source: ZonedDateTime): LocalDateTime {
            return source.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()
        }
    }

    @ReadingConverter
    class TimeStampToOffsetDateTimeConverter : Converter<LocalDateTime, ZonedDateTime> {
        override fun convert(source: LocalDateTime): ZonedDateTime {
            return source.atZone(ZoneOffset.UTC)
        }
    }
}