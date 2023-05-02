package pl.malirz.vshop.product.query

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import mu.KotlinLogging
import org.hibernate.validator.constraints.Length
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.malirz.vshop.product.PRODUCTS
import java.util.*

@RestController
@RequestMapping("$PRODUCTS")
internal class SearchProductsController(
    private val handler: SearchProductsQueryHandler
) {

    private val logger = KotlinLogging.logger {}

    @GetMapping
    fun accept(@Valid @ModelAttribute request: SearchProductsRequest): List<SearchProductsView> {
        logger.debug { "Search products by: $request" }
        val result = handler.apply(request.toQuery())
        logger.debug { "Found ${result.size} products by: $request" }
        return result
    }
}

internal data class SearchProductsRequest(
    @field:NotBlank
    @field:Length(min = 3, max = 255)
    @field:Pattern(regexp = "[a-zA-Z0-9ÀÁÂÄĄÇĆČĎÈÉÊËĘĚÍÎÏŁŃŇÑÓÔÖŘßŚŠŤÙÚÛÜŮÝŸŹŻŽa-zàáâäąçćčďèéêëęěíîïłńňñóôöřßśšťùúûüůýÿźżž ]*")
    val textContains: String
) {
    fun toQuery() = SearchProductsQuery(textContains)
}

