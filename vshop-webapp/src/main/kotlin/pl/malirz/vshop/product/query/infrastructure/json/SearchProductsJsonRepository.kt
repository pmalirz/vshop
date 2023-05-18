package pl.malirz.vshop.product.query.infrastructure.json

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import pl.malirz.vshop.product.query.SearchProductsQuery
import pl.malirz.vshop.product.query.SearchProductsRepository
import pl.malirz.vshop.product.query.SearchProductsView
import pl.malirz.vshop.product.query.asView
import pl.malirz.vshop.shared.infrastructure.repository.utils.OracleJsonQuery

private const val TABLE = "PRODUCT_JSON"

/**
 * Search products using JSON.
 */
@Repository
@Profile("JSON")
private class SearchProductsJsonRepository(
    private val oracleJsonQuery: OracleJsonQuery
) : SearchProductsRepository {

    override fun apply(searchProductsQuery: SearchProductsQuery) =
        oracleJsonQuery.search(TABLE, searchProductsQuery.textContains, SearchProductsView::class).asView()

}

