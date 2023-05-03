package pl.malirz.vshop.product.query

import java.math.BigDecimal

data class SearchProductsView(
    val id: String,
    val code: String,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val quantity: Int
)