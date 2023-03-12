package pl.malirz.vshop.catalogue.command

import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*
import java.util.function.Consumer
import java.util.stream.IntStream
import kotlin.system.measureTimeMillis

@Service
internal class AddProductToCatalogueHandler(var repository: AddProductToCatalogueRepository) :
    Consumer<AddProductToCatalogueCommand> {

    private val logger = KotlinLogging.logger {}

    @Transactional
    override fun accept(command: AddProductToCatalogueCommand) {

        val productInCatalogues = mutableListOf<ProductInCatalogue>()

        IntStream.rangeClosed(1, 100_000).forEach {
            with(command) {
                val productInCatalogue = ProductInCatalogue(
                    id = UUID.randomUUID().toString(),
                    code = it.toString(),
                    name = name + org.apache.commons.lang3.RandomStringUtils.random(5, true, true),
                    description = description + org.apache.commons.lang3.RandomStringUtils.random(5, true, true),
                    price = BigDecimal.valueOf(Math.random())
                        .setScale(2, java.math.RoundingMode.HALF_UP),
                    quantity = (Math.random() * 100).toInt()
                )

                productInCatalogues.add(productInCatalogue)
            }
        }

        /*        val timeInMillis = measureTimeMillis {
                    productInCatalogues.forEach {
                        repository.add(it)
                    }
                }*/

        val timeInMillis = measureTimeMillis {
            repository.add(productInCatalogues)
        }

        logger.info { "100 000 documents inserted one by one (in one transaction): $timeInMillis ms" }
    }

}

data class AddProductToCatalogueCommand(
    val code: String,
    val name: String,
    val description: String,
    val quantity: Int?,
    val price: BigDecimal?
)