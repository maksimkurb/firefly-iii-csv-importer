import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    val kotlin_version = "1.7.22"

    id("org.springframework.boot") version "3.0.0"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version kotlin_version
    kotlin("kapt") version kotlin_version
    kotlin("plugin.spring") version kotlin_version

    id("org.graalvm.buildtools.native") version "0.9.18"

    id("org.openapi.generator") version "6.2.1"
}

group = "ru.cubly.firefly"
version = "0.0.1-SNAPSHOT"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

kapt {
    correctErrorTypes = true
    arguments {
        // Set Mapstruct Configuration options here
        // https://kotlinlang.org/docs/reference/kapt.html#annotation-processor-arguments
        // https://mapstruct.org/documentation/stable/reference/html/#configuration-options
        arg("mapstruct.defaultComponentModel", "spring")
    }
}

kotlin {
    jvmToolchain(17)
}

repositories {
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/release") }
    mavenCentral()
}

extra["springCloudVersion"] = "2021.0.3"
extra["testcontainersVersion"] = "1.17.3"

val ktor_version = "2.1.2"
val mapstructVersion = "1.5.2.Final"
val lombokVersion = "1.18.24"
val lombokMapstructBindingVersion = "0.2.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")
    implementation("org.springframework.session:spring-session-core")
    implementation("org.springframework.session:spring-session-data-redis")
    implementation("org.springframework.security:spring-security-oauth2-client:6.0.0")

    implementation("com.squareup.okhttp3:okhttp:4.10.0")

//    implementation("javax.servlet:javax.servlet-api:4.0.1")

    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-client-jackson:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson:$ktor_version")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.14.0")

    developmentOnly("io.projectreactor:reactor-tools")

    implementation("io.github.wimdeblauwe:error-handling-spring-boot-starter:4.0.0")

    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("org.springdoc:springdoc-openapi-starter-webflux-api:2.0.0")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.0.0")
    implementation("org.springdoc:springdoc-openapi-starter-common:2.0.0")

    implementation("com.eclipsesource.j2v8:j2v8_linux_x86_64:4.6.0")
    implementation("com.eclipsesource.j2v8:j2v8_win32_x86_64:4.6.0")

    // MapStruct & Lombok
    implementation("org.mapstruct:mapstruct:${mapstructVersion}")
    implementation("org.projectlombok:lombok:${lombokVersion}")
    kapt("org.mapstruct:mapstruct-processor:${mapstructVersion}")
    kapt("org.projectlombok:lombok:${lombokVersion}")
    kapt("org.projectlombok:lombok-mapstruct-binding:${lombokMapstructBindingVersion}")

    implementation("io.github.daggerok:liquibase-r2dbc-spring-boot-starter:2.1.4")
    implementation("com.github.blagerweij:liquibase-sessionlock:1.6.0")

    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

//    runtimeOnly("org.postgresql:postgresql")
//    runtimeOnly("org.postgresql:r2dbc-postgresql")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:3.1.0")
    runtimeOnly("org.mariadb:r2dbc-mariadb:1.1.2")
    runtimeOnly("io.r2dbc:r2dbc-pool:1.0.0.RELEASE")

    kapt("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.batch:spring-batch-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mariadb")
    testImplementation("org.testcontainers:r2dbc")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set("$rootDir/specs/firefly-iii-1.5.6.yaml")
    outputDir.set("$buildDir/openapi")
    apiPackage.set("ru.cubly.firefly.api")
    packageName.set("ru.cubly.firefly.client")
    modelPackage.set("ru.cubly.firefly.model")
    configOptions.set(mapOf(
            "java8" to "true",
            "dateLibrary" to "java8",
            "library" to "jvm-ktor",
            "serializationLibrary" to "jackson",
            "enumPropertyNaming" to "PascalCase"
    ))
}

tasks.named("compileKotlin") {
    dependsOn(":openApiGenerate")
}
tasks.named("processResources") {
    dependsOn(":openApiGenerate")
}

configure<SourceSetContainer> {
    named("main") {
        java.srcDir("$buildDir/openapi/src/main/kotlin")
    }
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xemit-jvm-type-annotations")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<BootBuildImage> {
    builder.set("paketobuildpacks/builder:tiny")
    environment.set(mapOf("BP_NATIVE_IMAGE" to "true"))
}

tasks.withType<org.jetbrains.kotlin.gradle.internal.KaptWithoutKotlincTask>()
        .configureEach {
            kaptProcessJvmArgs.add("-Xmx512m")
        }