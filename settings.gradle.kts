pluginManagement {
    plugins {
        kotlin("jvm") version "1.8.20"
        kotlin("js") version "1.8.20"
        kotlin("plugin.spring") version "1.8.20"
        kotlin("plugin.noarg") version "1.8.20"
        kotlin("plugin.jpa") version "1.8.20"
        id("org.springframework.boot") version "3.0.6"
        id("io.spring.dependency-management") version "1.1.0"
    }
}


rootProject.name = "vshop"

include("vshop-docker", "vshop-webapp")
