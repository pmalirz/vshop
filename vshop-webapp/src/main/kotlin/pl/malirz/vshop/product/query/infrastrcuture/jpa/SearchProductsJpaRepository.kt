package pl.malirz.vshop.product.query.infrastrcuture.jpa

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import org.springframework.context.annotation.Profile
import org.springframework.data.annotation.Immutable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import pl.malirz.vshop.product.query.SearchProductsQuery
import pl.malirz.vshop.product.query.SearchProductsRepository
import pl.malirz.vshop.product.query.SearchProductsView
import java.math.BigDecimal
import java.util.stream.Stream

/**
 * Search products using JPA.
 */
@Repository
@Profile("JPA")
private class SearchProductsSodaRepository(
    private val internalRepository: ProductProjectionJpaInternalRepository
) : SearchProductsRepository {

    override fun apply(searchProductsQuery: SearchProductsQuery): List<SearchProductsView> =
        internalRepository.findAllTextContains("%${searchProductsQuery.textContains}%").map {
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

@Repository
@Profile("JPA")
private interface ProductProjectionJpaInternalRepository : JpaRepository<ProductProjectionJpa, String> {
    @Query("SELECT p FROM PRODUCT_VIEW_JPA p WHERE p.description ilike ?1 OR p.name ilike ?1")
    fun findAllTextContains(textFragment: String): Stream<ProductProjectionJpa>
}

/**
 * JPA model belongs to the infrastructure. Do not combine a burden of JPA together with your domain model.
 * Sooner or later you will regret when JPA limitations start influence the decision in your domain tier.
 * This Entity is purely for mapping your domain model to the database.
 */
@Entity(name = "PRODUCT_VIEW_JPA")
@Table(name = "PRODUCT_JPA")
@Immutable
private data class ProductProjectionJpa(
    @Id
    val id: String,
    val code: String,
    val name: String,
    val description: String?,
    val price: BigDecimal,
    val quantity: Int,
    @Version
    val revision: Long
)
