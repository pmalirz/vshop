package pl.malirz.vshop.product.command.infrastructure.soda

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import pl.malirz.vshop.product.command.AddProductRepository
import pl.malirz.vshop.product.command.Product
import pl.malirz.vshop.shared.utils.OracleSodaQuery

private const val TABLE = "PRODUCT_SODA"

@Repository
@Profile("SODA")
private class AddProductSodaRepository(
    private val oracleSodaQuery: OracleSodaQuery
) : AddProductRepository {
    override fun add(product: Product) {
        oracleSodaQuery.insert(TABLE, product)
    }

    override fun add(products: Collection<Product>) {
        oracleSodaQuery.insertBatch(TABLE, products)
    }

}