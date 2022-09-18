package ru.cubly.firefly.importer.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.cubly.firefly.importer.model.Import
import ru.cubly.firefly.importer.model.RawTransaction
import ru.cubly.firefly.importer.repository.RawTransactionRepository
import java.io.BufferedInputStream
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.StandardOpenOption

@Service
class RawTransactionService(
    val rawTransactionRepository: RawTransactionRepository,
) {
    private final val csvMapper: CsvMapper = CsvMapper()

    @Transactional
    fun insertTransactions(import: Import, file: Mono<FilePart>): Flux<RawTransaction> {
        val schema = CsvSchema.emptySchema().withHeader()

        val tempFile = Files.createTempFile("firefly-iii-csv-importer", ".csv")

        return file.flatMap { fp ->
            return@flatMap fp.transferTo(tempFile)
        }
            .flatMapMany {
                BufferedInputStream(
                    Files.newInputStream(
                        tempFile,
                        StandardOpenOption.DELETE_ON_CLOSE,
                        LinkOption.NOFOLLOW_LINKS
                    )
                ).use { inputStream ->

                    val reader: MappingIterator<JsonNode> = csvMapper.readerFor(JsonNode::class.java)
                        .with(schema)
                        .readValues(inputStream)

                    return@flatMapMany rawTransactionRepository.saveAll { subscriber ->
                        while (reader.hasNext()) {
                            val row = reader.next()

                            val rawTransaction = RawTransaction(
                                importId = import.importId,
                                rowNumber = reader.currentLocation.lineNr.toLong(),
                                data = row
                            )

                            subscriber.onNext(rawTransaction)
                        }
                    }
                }
            }
    }
}