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

plugins {
    id("com.gradle.enterprise") version("3.13")
}

rootProject.name = "vshop"

include("vshop-docker", "vshop-webapp")

gradleEnterprise {
    if (System.getenv("CI") != null) {
        buildScan {
            publishAlways()
            termsOfServiceUrl = "https://gradle.com/terms-of-service"
            termsOfServiceAgree = "yes"
        }
    }
}
