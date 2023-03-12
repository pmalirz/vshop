package pl.malirz.vshop.catalogue.command

import com.fasterxml.jackson.databind.ObjectMapper
import oracle.soda.OracleDatabase
import oracle.soda.rdbms.OracleRDBMSClient
import org.springframework.stereotype.Repository
import java.util.function.Function
import javax.sql.DataSource


sealed interface FindProductInCatalogueByCodeRepository : Function<String, ProductInCatalogue?>

data class SearchParams(
    val code: String
)

@Repository
private class FindProductInCatalogueByCodeSODARepository(
    var oracleRDBMSClient: OracleRDBMSClient,
    var dataSource: DataSource,
    var objectMapper: ObjectMapper
) :
    FindProductInCatalogueByCodeRepository {
    override fun apply(code: String): ProductInCatalogue? {

        dataSource.connection.use {
            val db: OracleDatabase = oracleRDBMSClient.getDatabase(it)

            val collection = db.openCollection("Catalogue")
            val document = db.createDocumentFrom(SearchParams(code))

            val one = collection.find().filter(document).one ?: return null

            return objectMapper.readerFor(ProductInCatalogue::class.java).readValue(one.contentAsByteArray)
        }
    }

}