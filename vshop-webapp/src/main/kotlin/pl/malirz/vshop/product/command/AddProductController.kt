package pl.malirz.vshop.product.command

import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.malirz.vshop.product.PRODUCTS
import pl.malirz.vshop.shared.domain.utils.IdGenerator
import java.math.BigDecimal
import java.util.*
import java.util.function.Consumer

@RestController
@RequestMapping("$PRODUCTS")
private class AddProductController(
    private val handler: AddProductCommandHandler,
    private val idGenerator: IdGenerator
) : Consumer<AddProductRequest> {

    @PostMapping
    override fun accept(@Valid @RequestBody request: AddProductRequest) {
        val id = idGenerator.generate()
        handler.accept(request.toCommand(id))
    }
}

data class AddProductRequest(
    @field:NotBlank
    val code: String,
    @field:NotBlank
    val name: String,
    val description: String?,
    @field:Min(1)
    val quantity: Int,
    @field:Min(0)
    val price: BigDecimal,
    @field:Min(0)
    val revision: Long?
) {
    fun toCommand(id: String) = AddProductCommand(id, code, name, description, price, quantity, revision)
}

