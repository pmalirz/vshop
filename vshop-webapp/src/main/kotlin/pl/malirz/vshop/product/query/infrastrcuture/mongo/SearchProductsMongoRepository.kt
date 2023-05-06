package pl.malirz.vshop.product.query.infrastrcuture.mongo

import jakarta.persistence.Id
import jakarta.persistence.Version
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import pl.malirz.vshop.product.command.Product
import pl.malirz.vshop.product.query.SearchProductsQuery
import pl.malirz.vshop.product.query.SearchProductsRepository
import pl.malirz.vshop.product.query.SearchProductsView
import java.math.BigDecimal
import java.util.stream.Stream

@Repository
@Profile("MONGO")
private class SearchProductsMongoRepository(
    private val internalRepository: ProductProjectionMongoInternalRepository
) : SearchProductsRepository {

    override fun apply(searchProductsQuery: SearchProductsQuery): List<SearchProductsView> {
        return internalRepository.findAllTextContains( searchProductsQuery.textContains ).map {
            SearchProductsView(
                id = it.id,
                code = it.code,
                name = it.name,
                description = it.description.orEmpty(),
                price = it.price,
                quantity = it.quantity
            )
        }.toList()
    }

    // Below functions could be moved to the Mongo model as a factory methods.
    // However, I like to keep mappers in the managed beans.
    // Notice how nice the higher-order function (toProductProjectionMongo) works.

    private fun Collection<Product>.toProductProjectionMongo(): Collection<ProductProjectionMongo> {
        return this.map { product -> product.toProductProjectionMongo() }
    }

    private fun Product.toProductProjectionMongo(): ProductProjectionMongo {
        return ProductProjectionMongo(
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
private interface ProductProjectionMongoInternalRepository : MongoRepository<ProductProjectionMongo, String> {

    @Query("{ \$text: { \$search: ?0 } }")
    fun findAllTextContains(textFragment: String): Stream<ProductProjectionMongo>

}

/**
 * Mongo similarly to JPA has its own model. Although it is similar to the JSON and SODA use cases.
 */
@Document("products")
internal class ProductProjectionMongo(
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