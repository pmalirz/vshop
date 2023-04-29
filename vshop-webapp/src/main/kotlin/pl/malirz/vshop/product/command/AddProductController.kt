package pl.malirz.vshop.product.command

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.malirz.vshop.shared.domain.utils.IdGenerator
import java.math.BigDecimal
import java.util.*

@RestController
@RequestMapping("/products")
internal class AddProductController(
    private val handler: AddProductCommandHandler,
    private val idGenerator: IdGenerator
) {

    @PostMapping
    fun accept(@RequestBody request: AddProductRequest) {
        val id = idGenerator.generate()
        handler.accept(request.toCommand(id))
    }
}

internal data class AddProductRequest(
    val code: String,
    val name: String,
    val description: String?,
    val quantity: Int?,
    val price: BigDecimal?,
    val revision: Long?
) {
    fun toCommand(id: String): AddProductCommand =
        AddProductCommand(id, code, name, description, price, quantity, revision)
}

