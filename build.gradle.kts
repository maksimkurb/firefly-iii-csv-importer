import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    id("org.springframework.experimental.aot") version "0.12.0-SNAPSHOT"

    id("org.openapi.generator") version "5.4.0"
}

group = "ru.cubly.firefly"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://repo.spring.io/milestone") }
    mavenCentral()
}

extra["springCloudVersion"] = "2021.0.3-SNAPSHOT"
extra["testcontainersVersion"] = "1.17.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")
    implementation("org.springframework.session:spring-session-core")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5")

    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly("io.r2dbc:r2dbc-h2")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.postgresql:r2dbc-postgresql")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.batch:spring-batch-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
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
    outputDir.set("$buildDir/generated")
    apiPackage.set("ru.cubly.firefly.api")
    invokerPackage.set("ru.cubly.firefly.invoker")
    modelPackage.set("ru.cubly.firefly.model")
    library.set("jvm-okhttp4")
    configOptions.put("serializationLibrary", "jackson")
    configOptions.put("dateLibrary", "java8")
}

configure<SourceSetContainer> {
    named("main") {
        java.srcDir("$buildDir/generated/src/main/kotlin")
    }
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
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
