import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

group = "pl.malirz.vshop"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    implementation("org.springframework:spring-tx:6.0.6")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("com.oracle.database.soda:orajsoda:1.1.7.1")
    implementation("com.oracle.database.jdbc:ojdbc11:21.9.0.0")
    implementation("com.oracle.database.jdbc:ucp11:21.9.0.0")
    implementation("com.oracle.database.ha:ons:21.9.0.0")
    runtimeOnly("com.oracle.database.security:oraclepki:21.9.0.0")
    runtimeOnly("com.oracle.database.security:osdt_cert:21.9.0.0")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.3")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.0.3")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(kotlin("test")) // The Kotlin test library
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
