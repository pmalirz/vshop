package pl.malirz.vshop

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jsonMapper
import jakarta.annotation.PostConstruct
import oracle.soda.OracleDatabase
import oracle.soda.rdbms.OracleRDBMSClient
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.ConnectionCallback
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import pl.malirz.vshop.shared.utils.OracleSodaQuery
import javax.sql.DataSource


@Configuration
@Profile("SODA")
class VShopSodaConfig {

    @Bean
    fun jdbcTemplate(dataSource: DataSource) = JdbcTemplate(dataSource)

    @Bean
    fun oracleRDBMSClient() = OracleRDBMSClient()

    @Bean
    fun oracleSODACollection(
        jdbcTemplate: JdbcTemplate,
        oracleRDBMSClient: OracleRDBMSClient,
        objectMapper: ObjectMapper
    ) = OracleSodaQuery(jdbcTemplate, oracleRDBMSClient, objectMapper)

    @Bean
    fun objectMapper(): ObjectMapper =
        jsonMapper {
            addModule(
                KotlinModule.Builder()
                    .enable(KotlinFeature.StrictNullChecks)
                    .build()
            )
        }

}

@Component
@Profile("SODA")
private class SodaInitDB(
    var oracleRDBMSClient: OracleRDBMSClient,
    var jdbcTemplate: JdbcTemplate
) {
    @PostConstruct
    @DependsOnDatabaseInitialization
    fun initializeDatabase() {
        jdbcTemplate.execute(ConnectionCallback {
            val db: OracleDatabase = oracleRDBMSClient.getDatabase(it)
            db.admin().createCollection("ProductSoda")
        })
    }

}