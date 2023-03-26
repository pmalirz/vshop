package pl.malirz.vshop.product.command

internal interface AddProductRepository {
    fun add(product: Product)
    fun add(products: Collection<Product>)
}
