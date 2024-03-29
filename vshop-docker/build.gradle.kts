plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly("com.oracle.database.jdbc:ojdbc11:21.9.0.0")
}

// This task downloads Oracle JDBC and places it in the Infinispan's /server/lib folder
tasks.register<Copy>("initDocker") {
    println("Copying Oracle JDBC to ${project.projectDir}/infinispan/server/lib")
    into("${project.projectDir}/infinispan/server/lib")
    from(configurations.runtimeClasspath)

    group = "init"
    description = "Copy Oracle JDBC to Infinispan's /server/lib folder"
}
