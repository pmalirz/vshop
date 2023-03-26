package pl.malirz.vshop

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class VShopApplication

fun main(args: Array<String>) {
    runApplication<VShopApplication>(*args)
}

