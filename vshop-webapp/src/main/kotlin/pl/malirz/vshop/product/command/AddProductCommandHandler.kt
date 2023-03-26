package pl.malirz.vshop.product.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*
import java.util.function.Consumer

@Service
internal class AddProductCommandHandler(private val repository: AddProductRepository) :
    Consumer<AddProductCommand> {

    @Transactional
    override fun accept(command: AddProductCommand) {
        val product = Product(
            id = UUID.randomUUID().toString(),
            code = command.code,
            name = command.name,
            description = command.description,
            price = command.price,
            quantity = command.quantity,
            revision = command.revision
        )

        repository.add(product)
    }
}

data class AddProductCommand(
    val code: String,
    val name: String,
    val description: String?,
    val price: BigDecimal?,
    val quantity: Int?,
    val revision: Long?
)
