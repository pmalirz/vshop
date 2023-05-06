package pl.malirz.vshop.product.command.infrastructure.mongo

import jakarta.persistence.Id
import jakarta.persistence.Version
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import pl.malirz.vshop.product.command.AddProductRepository
import pl.malirz.vshop.product.command.Product
import java.math.BigDecimal

@Repository
@Profile("MONGO")
private class AddProductMongoRepository(
    private val internalRepository: ProductMongoInternalRepository
) : AddProductRepository {

    override fun add(product: Product) {
        internalRepository.save(product.toProductMongo())
    }

    override fun add(products: Collection<Product>) {
        internalRepository.saveAll(products.toProductMongo())
    }

    // Below functions could be moved to the Mongo model as a factory methods.
    // However, I like to keep mappers in the managed beans.
    // Notice how nice the higher-order function (toProductMongo) works.

    private fun Collection<Product>.toProductMongo(): Collection<ProductMongo> {
        return this.map { product -> product.toProductMongo() }
    }

    private fun Product.toProductMongo(): ProductMongo {
        return ProductMongo(
            id = this.id,
            code = this.code,
            name = this.name,
            description = this.description,
            price = this.price,
            quantity = this.quantity,
            revision = this.revision
        )
    }
}

@Repository
@Profile("MONGO")
private interface ProductMongoInternalRepository : MongoRepository<ProductMongo, String>

/**
 * Mongo similarly to JPA has its own model. Although it is similar to the JSON and SODA use cases.
 */
@Document("products")
private class ProductMongo(
    @Id
    val id: String,
    val code: String,
    val name: String,
    val description: String?,
    val price: BigDecimal,
    val quantity: Int,
    @Version
    val revision: Long? = 1L
)