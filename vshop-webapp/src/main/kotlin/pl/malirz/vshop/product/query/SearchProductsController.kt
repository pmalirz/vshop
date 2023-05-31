package pl.malirz.vshop.product.query

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import mu.KotlinLogging
import org.hibernate.validator.constraints.Length
import org.springframework.web.bind.annotation.*
import pl.malirz.cqrs.QueryHandler
import pl.malirz.vshop.product.PRODUCTS
import java.util.*
import java.util.function.Function

@RestController
@RequestMapping("$PRODUCTS")
@CrossOrigin
private class SearchProductsController(
    private val queryHandler: QueryHandler<SearchProductsQuery, SearchProductsListView>
) : Function<SearchProductsRequest, SearchProductsListView> {

    private val logger = KotlinLogging.logger {}

    @GetMapping
    override fun apply(@Valid @ModelAttribute request: SearchProductsRequest): SearchProductsListView {
        logger.debug { "Search products by: $request" }
        val result = queryHandler.apply(request.toQuery())
        logger.debug { "Found ${result.items.size} products by: $request" }
        return result
    }
}

private data class SearchProductsRequest(
    @field:NotBlank
    @field:Length(min = 3, max = 255)
    @field:Pattern(regexp = "[a-zA-Z0-9ÀÁÂÄĄÇĆČĎÈÉÊËĘĚÍÎÏŁŃŇÑÓÔÖŘßŚŠŤÙÚÛÜŮÝŸŹŻŽa-zàáâäąçćčďèéêëęěíîïłńňñóôöřßśšťùúûüůýÿźżž ]*")
    val textContains: String
) {
    fun toQuery() = SearchProductsQuery(textContains)
}

