package pl.malirz.vshop

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.OracleContainer
import java.time.Duration

abstract class AbstractIntegrationOracleTest {

    @LocalServerPort
    private var port: Int = 0

    fun baseUrl() = "http://localhost:$port"

    companion object {
        val oracle = OracleContainer("gvenzl/oracle-xe:21-faststart")
            .withStartupTimeout(Duration.ofMinutes(5L))
            .withUsername("vshop")
            .withPassword("vshop")

        @JvmStatic
        @BeforeAll
        fun start() {
            oracle.start()
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            oracle.stop()
        }
    }
}

class TestContextInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
        TestPropertyValues.of(
            "spring.datasource.url=" + AbstractIntegrationOracleTest.oracle.jdbcUrl
        ).applyTo(configurableApplicationContext.environment)
    }
}