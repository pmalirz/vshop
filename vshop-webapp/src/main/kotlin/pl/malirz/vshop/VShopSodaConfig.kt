package pl.malirz.vshop

import com.fasterxml.jackson.databind.ObjectMapper
import oracle.soda.rdbms.OracleRDBMSClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import pl.malirz.vshop.shared.infrastructure.repository.utils.OracleSodaQuery
import javax.sql.DataSource


@Configuration
@EnableTransactionManagement
@Profile("SODA")
class VShopSodaConfig {

    @Bean
    fun jdbcTemplate(dataSource: DataSource) = JdbcTemplate(dataSource)

    @Bean
    fun transactionManager(dataSource: DataSource): PlatformTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }

    @Bean
    fun oracleRDBMSClient() = OracleRDBMSClient()

    @Bean
    fun oracleSODACollection(
        jdbcTemplate: JdbcTemplate,
        oracleRDBMSClient: OracleRDBMSClient,
        objectMapper: ObjectMapper
    ) = OracleSodaQuery(jdbcTemplate, oracleRDBMSClient, objectMapper)

}
