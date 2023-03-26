package pl.malirz.vshop.product.command

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/catalogue/products")
internal class AddProductController(private val handler: AddProductCommandHandler) {

    @PostMapping
    fun accept(@RequestBody request: AddProductRequest) {
        handler.accept(request.toCommand())
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
    fun toCommand(): AddProductCommand =
        AddProductCommand(code, name, description, price, quantity, revision)
}

