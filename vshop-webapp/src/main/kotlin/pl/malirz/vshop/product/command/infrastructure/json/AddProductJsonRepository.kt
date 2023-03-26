package pl.malirz.vshop.product.command.infrastructure.json

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import pl.malirz.vshop.product.command.AddProductRepository
import pl.malirz.vshop.product.command.Product
import pl.malirz.vshop.shared.utils.OracleJsonQuery

private const val TABLE = "PRODUCT_JSON"

@Repository
@Profile("JSON")
private class AddProductJsonRepository(
    private val oracleJsonQuery: OracleJsonQuery
) : AddProductRepository {
    override fun add(product: Product) {
        oracleJsonQuery.insert(TABLE, product)
    }

    override fun add(products: Collection<Product>) {
        oracleJsonQuery.insertBatch(TABLE, products)
    }

}