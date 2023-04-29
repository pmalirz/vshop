package pl.malirz.vshop

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

}
