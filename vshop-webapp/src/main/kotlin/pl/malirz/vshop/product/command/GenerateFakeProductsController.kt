package pl.malirz.vshop.product.command

import com.github.javafaker.Faker
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Positive
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.malirz.cqrs.CommandHandler
import pl.malirz.vshop.shared.domain.utils.IdGenerator
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import java.util.function.Consumer
import java.util.stream.IntStream
import kotlin.system.measureTimeMillis

@RestController
@RequestMapping("/products/generate")
private class GenerateFakeProductsController(
    private val handler: CommandHandler<AddProductCommand>,
    private val idGenerator: IdGenerator
) : Consumer<GenerateProductsRequest> {

    private val logger = KotlinLogging.logger {}
    private val faker = Faker()

    @PostMapping
    override fun accept(@RequestBody @Valid generateProductsRequest: GenerateProductsRequest) {
        val numberOfProducts = generateProductsRequest.numberOfProducts

        val addProductCommands = mutableListOf<AddProductCommand>()

        IntStream.rangeClosed(1, numberOfProducts).forEach {
            val productName = faker.commerce().productName()
            val manufacturer = faker.company().name()

            val addProductCommand = AddProductCommand(
                id = idGenerator.generate(),
                code = it.toString(),
                name = productName,
                description = "$productName produced by $manufacturer",
                price = BigDecimal.valueOf(Math.random() * 1000).setScale(2, RoundingMode.HALF_UP),
                quantity = (Math.random() * 1000).toInt(),
                revision = 1L
            )
            addProductCommands.add(addProductCommand)
        }

        logger.info { "Start initialization of products ($numberOfProducts products)" }

        val timeInMillis = measureTimeMillis {
            addProductCommands.forEach {
                handler.accept(it)
                logger.info { "Initialized product: $it" }
            }
        }

        logger.info { "End initialization of products ($numberOfProducts products): $timeInMillis ms" }
    }
}

private data class GenerateProductsRequest(
    @field:Positive
    @field:Max(1_000_000)
    val numberOfProducts: Int
)

