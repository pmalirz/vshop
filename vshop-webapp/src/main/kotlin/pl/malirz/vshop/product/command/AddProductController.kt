package pl.malirz.vshop.product.command

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/catalogue/products")
internal class AddProductToCatalogueController(private val  handler: AddProductToCatalogueCommandHandler) {

    @PostMapping
    fun accept(@RequestBody request: AddProductToCatalogueRequest) {
        handler.accept(request.toCommand())
    }
}

internal data class AddProductToCatalogueRequest(
    val code: String,
    val name: String,
    val description: String?,
    val quantity: Int?,
    val price: BigDecimal?
) {
    fun toCommand(): AddProductToCatalogueCommand =
        AddProductToCatalogueCommand(code, name, description, price, quantity)
}

