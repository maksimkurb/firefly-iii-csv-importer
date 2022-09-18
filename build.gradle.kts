import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot") version "2.7.3"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    kotlin("jvm") version "1.7.10"
    kotlin("kapt") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"
    id("org.springframework.experimental.aot") version "0.12.1"

    id("org.openapi.generator") version "6.0.1"
}

group = "ru.cubly.firefly"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

springAot {
    removeYamlSupport.set(true)
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

kapt {
    arguments {
        // Set Mapstruct Configuration options here
        // https://kotlinlang.org/docs/reference/kapt.html#annotation-processor-arguments
        // https://mapstruct.org/documentation/stable/reference/html/#configuration-options
        arg("mapstruct.defaultComponentModel", "spring")
    }
}

repositories {
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/release") }
    mavenCentral()
}

extra["springCloudVersion"] = "2021.0.3"
extra["testcontainersVersion"] = "1.17.3"

val ktor_version = "1.6.8"
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
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")
    implementation("org.springframework.session:spring-session-core")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.session:spring-session-data-redis")
    implementation("org.springframework.security:spring-security-oauth2-client:5.7.3")

    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    implementation("javax.servlet:javax.servlet-api:4.0.1")

    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-json-jvm:$ktor_version")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.13.4")

    developmentOnly("io.projectreactor:reactor-tools")

    implementation("io.github.wimdeblauwe:error-handling-spring-boot-starter:3.1.0")

    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("org.springdoc:springdoc-openapi-ui:1.6.11")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.6.11")
    implementation("org.springdoc:springdoc-openapi-security:1.6.11")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.11")
    implementation("org.springdoc:springdoc-openapi-native:1.6.11")

    implementation("com.eclipsesource.j2v8:j2v8_linux_x86_64:4.6.0")
    implementation("com.eclipsesource.j2v8:j2v8_win32_x86_64:4.6.0")

    // MapStruct & Lombok
    implementation("org.mapstruct:mapstruct:${mapstructVersion}")
    implementation("org.projectlombok:lombok:${lombokVersion}")
    kapt("org.mapstruct:mapstruct-processor:${mapstructVersion}")
    kapt("org.projectlombok:lombok:${lombokVersion}")
    kapt("org.projectlombok:lombok-mapstruct-binding:${lombokMapstructBindingVersion}")

    implementation("io.github.daggerok:liquibase-r2dbc-spring-boot-starter:2.1.1")
    implementation("com.github.blagerweij:liquibase-sessionlock:1.5.3")

    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

//    runtimeOnly("org.postgresql:postgresql")
//    runtimeOnly("org.postgresql:r2dbc-postgresql")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    runtimeOnly("org.mariadb:r2dbc-mariadb")
    runtimeOnly("io.r2dbc:r2dbc-pool")

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

tasks.named("generateTestAot") {
    enabled = false
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
    builder = "paketobuildpacks/builder:tiny"
    environment = mapOf("BP_NATIVE_IMAGE" to "true")
}

tasks.withType<org.jetbrains.kotlin.gradle.internal.KaptWithoutKotlincTask>()
    .configureEach {
        kaptProcessJvmArgs.add("-Xmx512m")
    }