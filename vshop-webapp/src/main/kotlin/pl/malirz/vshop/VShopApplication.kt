package pl.malirz.vshop

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jsonMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.malirz.vshop.shared.domain.utils.IdGenerator
import pl.malirz.vshop.shared.domain.utils.UUIDIdGenerator

@SpringBootApplication
class VShopApplication

fun main(args: Array<String>) {
    runApplication<VShopApplication>(*args)
}

@Configuration
class SharedConfiguration {

    @Bean
    fun idGenerator(): IdGenerator {
        return UUIDIdGenerator()
    }

    @Bean
    fun objectMapper(): ObjectMapper =
        jsonMapper {
            addModule(
                KotlinModule.Builder()
                    .enable(KotlinFeature.StrictNullChecks)
                    .build()
            )
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }

}
