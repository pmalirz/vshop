package pl.malirz.vshop.product.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.malirz.cqrs.CommandHandler
import java.math.BigDecimal

@Service
private class AddProductCommandHandler(private val repository: AddProductRepository) :
    CommandHandler<AddProductCommand> {

    @Transactional
    override fun accept(command: AddProductCommand) {
        val product = Product(
            id = command.id,
            code = command.code,
            name = command.name,
            description = command.description,
            price = command.price,
            quantity = command.quantity,
            revision = command.revision
        )

        repository.accept(product)
    }
}

data class AddProductCommand(
    val id: String,
    val code: String,
    val name: String,
    val description: String?,
    val price: BigDecimal,
    val quantity: Int,
    val revision: Long?,
)
