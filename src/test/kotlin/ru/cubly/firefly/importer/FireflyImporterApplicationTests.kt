package ru.cubly.firefly.importer

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.junit.jupiter.Container

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FireflyImporterApplicationTests {
    // TODO: https://rieckpil.de/testing-spring-boot-applications-with-kotlin-and-testcontainers/
    companion object {
        @Container
        val mariaDBContainer = MariaDBContainer<Nothing>("mariadb:10").apply {
            withUsername("firefly-import")
            withPassword("firefly-import")
            withDatabaseName("firefly-import")
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.r2dbc.url") {
                "r2dbc:mariadb://${mariaDBContainer.host}:${mariaDBContainer.getMappedPort(3306)}/${mariaDBContainer.databaseName}"
            }
            registry.add("spring.r2dbc.username", mariaDBContainer::getUsername)
            registry.add("spring.r2dbc.password", mariaDBContainer::getPassword)
        }
    }

    @Test
    fun contextLoads() {
    }

}
