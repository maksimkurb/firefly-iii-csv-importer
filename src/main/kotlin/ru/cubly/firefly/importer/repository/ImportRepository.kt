package ru.cubly.firefly.importer.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.cubly.firefly.importer.model.Import

@Repository
interface ImportRepository : CrudRepository<Import, Long> {
}