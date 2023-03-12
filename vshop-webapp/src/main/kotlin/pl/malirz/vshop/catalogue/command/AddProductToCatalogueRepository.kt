package pl.malirz.vshop.catalogue.command

import org.springframework.stereotype.Repository
import pl.malirz.vshop.shared.utils.OracleSODACollection

internal sealed interface AddProductToCatalogueRepository {
    fun add(productInCatalogue: ProductInCatalogue)
    fun add(productInCatalogues: Collection<ProductInCatalogue>)

}

@Repository
private class SODARepository(
    var oracleSODACollection: OracleSODACollection
) : AddProductToCatalogueRepository {
    override fun add(productInCatalogue: ProductInCatalogue) {
        oracleSODACollection.storeObject("Catalogue1", productInCatalogue)
    }

    override fun add(productInCatalogues: Collection<ProductInCatalogue>) {
        oracleSODACollection.storeObjects("Catalogue1", productInCatalogues)
    }

}
