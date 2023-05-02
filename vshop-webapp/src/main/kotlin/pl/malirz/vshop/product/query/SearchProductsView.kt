package pl.malirz.vshop.product.query

data class SearchProductsView(
    val id: String,
    val code: String,
    val name: String,
    val description: String,
    val price: String,
    val quantity: String
)