package ru.cubly.firefly.importer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FireflyImporterApplication

fun main(args: Array<String>) {
    runApplication<FireflyImporterApplication>(*args)
}
