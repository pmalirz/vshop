package pl.malirz.vshop.product.command.infrastructure.jpa

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Version
import org.springframework.context.annotation.Profile
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pl.malirz.vshop.product.command.AddProductRepository
import pl.malirz.vshop.product.command.Product
import java.math.BigDecimal

@Repository
@Profile("JPA")
private class AddProductJpaRepository(
    private val internalRepository: ProductJpaInternalRepository
) : AddProductRepository {

    override fun add(product: Product) {
        internalRepository.save(product.toProductJpa())
    }

    override fun add(products: Collection<Product>) {
        internalRepository.saveAll(products.toProductJpa())
    }

    // Below functions could be moved to JPA as a factory methods. However, I like to keep mappers in the managed beans.
    // Notice how nice the higher-order function (toProductJpa) works.

    private fun Collection<Product>.toProductJpa(): Collection<ProductJpa> {
        return this.map { product -> product.toProductJpa() }
    }

    private fun Product.toProductJpa(): ProductJpa {
        return ProductJpa(
            id = this.id,
            code = this.code,
            name = this.name,
            description = this.description,
            price = this.price,
            quantity = this.quantity,
            version = this.version
        )
    }

}

@Repository
@Profile("JPA")
private interface ProductJpaInternalRepository : CrudRepository<ProductJpa, String>

/**
 * JPA model belongs to the infrastructure. Do not combine a burden of JPA together with your domain model.
 * Sooner or later you will regret when JPA limitations start influence the decision in your domain tier.
 * This Entity is purely for mapping your domain model to the database.
 */
@Entity(name = "ProductJpa")
internal class ProductJpa(
    @Id
    val id: String,
    val code: String,
    val name: String,
    val description: String?,
    val price: BigDecimal?,
    val quantity: Int?,
    @Version
    val version: Long? = 1L
)