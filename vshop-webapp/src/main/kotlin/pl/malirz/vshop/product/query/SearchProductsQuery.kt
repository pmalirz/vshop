package pl.malirz.vshop.product.query

import pl.malirz.cqrs.Query
import java.math.BigDecimal
import java.util.stream.Stream

/**
 * Search products by text (full text search).
 */
data class SearchProductsQuery(
    val textContains: String,
) : Query<SearchProductsListView>

fun List<SearchProductsView>.asView() = SearchProductsListView(this)

fun Stream<SearchProductsView>.asView() = this.toList().asView()

data class SearchProductsListView(
    val items: List<SearchProductsView> = listOf()
)

data class SearchProductsView(
    val id: String,
    val code: String,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val quantity: Int
)
