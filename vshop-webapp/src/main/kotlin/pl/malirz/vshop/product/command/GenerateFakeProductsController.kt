package pl.malirz.vshop.product.command

import mu.KotlinLogging
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.malirz.vshop.shared.domain.utils.IdGenerator
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import java.util.stream.IntStream
import kotlin.system.measureTimeMillis

@RestController
@RequestMapping("/products/generate")
internal class GenerateFakeProductsController(
    private val handler: AddProductCommandHandler,
    private val idGenerator: IdGenerator
) {

    private val logger = KotlinLogging.logger {}

    @PostMapping("/{numberOfProducts}")
    fun accept(@PathVariable numberOfProducts: Int) {

        val addProductCommands = mutableListOf<AddProductCommand>()

        if (numberOfProducts <= 0) {
            logger.error { "No products to generate. numberOfProducts is $numberOfProducts but must be greater than ZERO" }
            return
        }

        IntStream.rangeClosed(1, numberOfProducts).forEach {
            val addProductCommand = AddProductCommand(
                id = idGenerator.generate(),
                code = it.toString(),
                name = RandomStringUtils.random(5, true, true),
                description = RandomStringUtils.random(10, true, true),
                price = BigDecimal.valueOf(Math.random() * 1000).setScale(2, RoundingMode.HALF_UP),
                quantity = (Math.random() * 1000).toInt(),
                revision = 1L
            )
            addProductCommands.add(addProductCommand)
        }

        logger.info { "Start initialization of products ($numberOfProducts products)" }

        val timeInMillis = measureTimeMillis {
            addProductCommands.forEach { handler.accept(it) }
        }

        logger.info { "End initialization of products  ($numberOfProducts products): $timeInMillis ms" }

    }
}


