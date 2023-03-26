package pl.malirz.vshop.product.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*
import java.util.function.Consumer

@Service
internal class AddProductToCatalogueCommandHandler(var repository: AddProductRepository) :
    Consumer<AddProductToCatalogueCommand> {

    @Transactional
    override fun accept(command: AddProductToCatalogueCommand) {
        val product = Product(
            id = UUID.randomUUID().toString(),
            code = command.code,
            name = command.name,
            description = command.description,
            price = command.price,
            quantity = command.quantity
        )

        repository.add(product)
    }
}

data class AddProductToCatalogueCommand(
    val code: String,
    val name: String,
    val description: String?,
    val price: BigDecimal?,
    val quantity: Int?,
)
