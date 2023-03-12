pluginManagement {
    plugins {
        kotlin("jvm") version "1.8.10"
        kotlin("plugin.spring") version "1.8.10"
        id("org.springframework.boot") version "3.0.4"
        id("io.spring.dependency-management") version "1.1.0"
    }
}


rootProject.name = "vshop"

include("vshop-docker", "vshop-webapp")
