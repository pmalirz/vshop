package pl.malirz.vshop.product.query

/**
 * Search products by text (full text search).
 */
data class SearchProductsQuery(
    val textContains: String,
)
