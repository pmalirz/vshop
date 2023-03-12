package pl.malirz.vshop

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import oracle.soda.rdbms.OracleRDBMSClient
import oracle.ucp.jdbc.UCPDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.TransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import pl.malirz.vshop.shared.utils.OracleSODACollection
import javax.sql.DataSource

@SpringBootApplication
@EnableWebMvc
@EnableTransactionManagement
class VShopApplication

fun main(args: Array<String>) {
    runApplication<VShopApplication>(*args)
}

@Configuration
@Import(UCPDataSource::class)
class Configuration {

    @Bean
    fun transactionManager(dataSource: DataSource): TransactionManager {
        return DataSourceTransactionManager(dataSource)
    }

    @Bean
    fun jdbcTemplate(dataSource: DataSource) = JdbcTemplate(dataSource)

    @Bean
    fun dataSource(ucpDataSource: UCPDataSource): DataSource {
        return ucpDataSource.datasource()
    }

    @Bean
    fun oracleRDBMSClient() = OracleRDBMSClient()

    @Bean
    fun oracleSODACollection(jdbcTemplate: JdbcTemplate, oracleRDBMSClient: OracleRDBMSClient, objectMapper: ObjectMapper) =
        OracleSODACollection(jdbcTemplate, oracleRDBMSClient, objectMapper)

    @Bean
    fun objectMapper(): ObjectMapper {
        val kotlinModule = KotlinModule.Builder()
            .enable(KotlinFeature.StrictNullChecks)
            .build()

        return JsonMapper.builder()
            .addModule(kotlinModule)
            .build()
    }

}
