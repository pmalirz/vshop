package pl.malirz.vshop.catalogue.command

import jakarta.annotation.PostConstruct
import oracle.soda.OracleDatabase
import oracle.soda.rdbms.OracleRDBMSClient
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization
import org.springframework.jdbc.core.ConnectionCallback
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcCall
import org.springframework.stereotype.Component
import pl.malirz.vshop.catalogue.query.SearchProductInCatalogueRepository
import java.math.BigDecimal
import java.sql.Connection
import javax.sql.DataSource

class ProductInCatalogue(
    val id: String,
    val code: String,
    val name: String,
    val description: String?,
    val price: BigDecimal?,
    val quantity: Int?
)


@Component
private class ProductInCatalogueInitDB(
    var oracleRDBMSClient: OracleRDBMSClient,
    var jdbcTemplate: JdbcTemplate
) {
    @PostConstruct
    @DependsOnDatabaseInitialization
    fun initializeDatabase() {
        jdbcTemplate.execute(ConnectionCallback {
            val db: OracleDatabase = oracleRDBMSClient.getDatabase(it)
            db.admin().createCollection("Catalogue1")
        })
    }

}
