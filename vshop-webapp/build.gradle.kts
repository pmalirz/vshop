import org.apache.tools.ant.Project
import org.apache.tools.ant.taskdefs.SQLExec
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.noarg")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

group = "pl.malirz.vshop"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

ext["tomcat.version"] = "10.1.7"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    implementation("org.springframework:spring-tx:6.0.6")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")

    implementation("com.oracle.database.soda:orajsoda:1.1.7.1")
    implementation("com.oracle.database.jdbc:ojdbc11:21.9.0.0")
    implementation("com.oracle.database.jdbc:ucp11:21.9.0.0")
    implementation("com.oracle.database.ha:ons:21.9.0.0")
    runtimeOnly("com.oracle.database.security:oraclepki:21.9.0.0")
    runtimeOnly("com.oracle.database.security:osdt_cert:21.9.0.0")

    implementation("org.hibernate:hibernate-core:6.1.7.Final")

    implementation("org.apache.tomcat:tomcat-jdbc:${project.ext["tomcat.version"]}")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.0.4")

    implementation("com.github.javafaker:javafaker:1.0.2")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(kotlin("test"))
    testImplementation("org.testcontainers:oracle-xe:1.18.0")
    testImplementation("org.testcontainers:mongodb:1.18.0")
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

tasks.register("initDB") {
    group = "init"
    description = "Initialize database structures"

    doLast {
        runScript("${project.projectDir}/src/main/resources/db/oracle/create-tables.sql")
    }
}

tasks.register("dropDB") {
    group = "init"
    description = "Drop all database structures"

    doLast {
        runScript("${project.projectDir}/src/main/resources/db/oracle/drop-tables.sql")
    }
}

fun runScript(scriptPath: String) {
    val sqlExec = SQLExec()
    sqlExec.project = Project()
    sqlExec.project.init()

    val delimiterType = SQLExec.DelimiterType()
    delimiterType.value = SQLExec.DelimiterType.ROW
    sqlExec.setDelimiterType(delimiterType)
    sqlExec.setDelimiter("/") // This is required as the script contains pl/sql blocks

    sqlExec.setDriver("oracle.jdbc.OracleDriver")
    sqlExec.createClasspath().setPath(project.sourceSets["main"].runtimeClasspath.asPath)
    sqlExec.setUrl("jdbc:oracle:thin:@//localhost:1521/xepdb1")
    sqlExec.setUserid("vshop")
    sqlExec.setPassword("vshop")
    sqlExec.setSrc(File(scriptPath))
    sqlExec.setPrint(true)
    sqlExec.execute()
}

