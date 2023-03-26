package pl.malirz.vshop.product.query

import com.fasterxml.jackson.databind.ObjectMapper
import oracle.soda.OracleDatabase
import oracle.soda.OracleDocument
import oracle.soda.rdbms.OracleRDBMSClient
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import pl.malirz.vshop.product.command.Product
import java.util.function.Function
import javax.sql.DataSource


sealed interface SearchProductInCatalogueRepository : Function<SearchParams, Set<Product>>

data class SearchParams(
    val code: String
)

@Repository
@Profile("SODA")
private class SearchProductInCatalogueSODARepository(
    var oracleRDBMSClient: OracleRDBMSClient,
    var dataSource: DataSource,
    var objectMapper: ObjectMapper
) : SearchProductInCatalogueRepository {
    override fun apply(searchParams: SearchParams): Set<Product> {
        val db: OracleDatabase = oracleRDBMSClient.getDatabase(dataSource.connection)

        val collection = db.openCollection("Catalogue")
        val document = db.createDocumentFrom(searchParams)

        val resultCursor = collection.find().filter(document).cursor

        while (resultCursor.hasNext()) {
            val item: OracleDocument = resultCursor.next()
        }

        return HashSet()
    }

}