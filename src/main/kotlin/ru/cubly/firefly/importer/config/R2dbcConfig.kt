package ru.cubly.firefly.importer.config

import com.fasterxml.jackson.core.type.TypeReference
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
import ru.cubly.firefly.importer.entity.MappingConfigSpec
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
    private val log: Logger = LoggerFactory.getLogger(R2dbcConfig::class.java)

    @Bean("dateTimeProvider")
    fun dateTimeProvider(): DateTimeProvider = DateTimeProvider {
        Optional.of(OffsetDateTime.now())
    }

    @Bean
    fun customConversions(objectMapper: ObjectMapper): R2dbcCustomConversions {
        return R2dbcCustomConversions.of(
            MySqlDialect.INSTANCE,
            listOf(
                TimeStampToOffsetDateTimeConverter(),
                OffsetDateTimeToTimeStampConverter(),
                jsonReadingConverter<MappingConfigSpec>(objectMapper),
                jsonWritingConverter<MappingConfigSpec>(objectMapper),
                jsonReadingConverter(objectMapper, object : TypeReference<Map<String, String>>() {}),
                jsonWritingConverter<Map<String, String>>(objectMapper),
            )
        )
    }

    private inline fun <reified T> jsonReadingConverter(objectMapper: ObjectMapper): Converter<String, T?> {
        return object : Converter<String, T?> {
            override fun convert(source: String): T? {
                try {
                    return objectMapper.readValue(source, T::class.java)
                } catch (e: IOException) {
                    log.error("Problem while deserializing JSON: {}", source, e)
                }
                return null
            }
        }
    }

    private inline fun <reified T> jsonReadingConverter(
        objectMapper: ObjectMapper,
        typeRef: TypeReference<T>
    ): Converter<String, T?> {
        return object : Converter<String, T?> {
            override fun convert(source: String): T? {
                try {
                    return objectMapper.readValue(source, typeRef)
                } catch (e: IOException) {
                    log.error("Problem while deserializing JSON: {}", source, e)
                }
                return null
            }
        }
    }

    private inline fun <reified T : Any> jsonWritingConverter(objectMapper: ObjectMapper): Converter<T, String?> {
        return object : Converter<T, String?> {
            override fun convert(source: T): String? {
                try {
                    return objectMapper.writeValueAsString(source)
                } catch (e: IOException) {
                    log.error("Problem while serializing JSON: {}", source, e)
                }
                return null
            }
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