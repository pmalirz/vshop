package pl.malirz.vshop.product.query.infrastrcuture.soda

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import pl.malirz.vshop.product.query.*
import pl.malirz.vshop.shared.infrastructure.repository.utils.OracleSodaQuery

private const val TABLE = "PRODUCT_SODA"

/**
 * Search products using SODA.
 *
 * [Overview of QBE Operator $textContains](https://docs.oracle.com/en/database/oracle/simple-oracle-document-access/adsdi/overview-soda-filter-specifications-qbes.html#GUID-DFE6DEC3-ACF6-4C98-9F0A-C24FBE5CCB55)
 */
@Repository
@Profile("SODA")
private class SearchProductsSodaRepository(
    private val oracleSodaQuery: OracleSodaQuery
) : SearchProductsRepository {

    override fun apply(searchProductsQuery: SearchProductsQuery): SearchProductsListView {
        val textFragment = searchProductsQuery.textContains.lowercase()
        val textQBE = "{ \"description\" : { \"\$lower\": { \"\$instr\": \"$textFragment\"}}}"
        return oracleSodaQuery.search(TABLE, textQBE, SearchProductsView::class).asView()
    }


    /*
    !A huge limitation of SODA for Java is that it does not support $textContains!
    https://docs.oracle.com/en/database/oracle/simple-oracle-document-access/adsdi/overview-soda-filter-specifications-qbes.html#GUID-DFE6DEC3-ACF6-4C98-9F0A-C24FBE5CCB55

    override fun apply(searchProductsQuery: SearchProductsQuery): List<SearchProductsView> {
        val textQBE = "{ \"\$textContains\" : \"${searchProductsQuery.textContains}\"}"
        return oracleSodaQuery.search(TABLE, textQBE, SearchProductsView::class)
    }*/

}

