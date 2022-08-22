package ru.cubly.firefly.importer.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.cubly.firefly.importer.model.RawTransaction

@Repository
interface RawTransactionRepository : CrudRepository<RawTransaction, Long> {
}