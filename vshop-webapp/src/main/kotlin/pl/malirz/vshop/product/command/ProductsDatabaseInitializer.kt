package pl.malirz.vshop.product.command

import mu.KotlinLogging
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*
import java.util.stream.IntStream
import kotlin.system.measureTimeMillis

/**
 * Populating the products database with a random set of data.
 */
@Service
@Profile("INIT")
internal class CatalogueDataInitializer(
    private val repository: AddProductRepository,
    @Value("\${vshop.products.generate.initial.size:0}")
    private val numberOfItemsToGenerate: Int
) :
    ApplicationListener<ContextRefreshedEvent> {

    private val logger = KotlinLogging.logger {}


    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val products = mutableListOf<Product>()

        if(numberOfItemsToGenerate <= 0) {
            logger.info { "No products to generate. Try to set vshop.products.generate.initial.size" }
            return
        }

        IntStream.rangeClosed(1, numberOfItemsToGenerate).forEach {
            val product = Product(
                id = UUID.randomUUID().toString(),
                code = it.toString(),
                name = RandomStringUtils.random(5, true, true),
                description = RandomStringUtils.random(10, true, true),
                price = BigDecimal.valueOf(Math.random() * 1000).setScale(2, java.math.RoundingMode.HALF_UP),
                quantity = (Math.random() * 1000).toInt()
            )
            products.add(product)
        }

        logger.info { "Start initialization of products ($numberOfItemsToGenerate products)" }

        val timeInMillis = measureTimeMillis {
            repository.add(products)
        }

        logger.info { "End initialization of products  ($numberOfItemsToGenerate products): $timeInMillis ms" }
    }

}


