package pl.malirz.vshop

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.testcontainers.containers.OracleContainer
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("SODA")
@ContextConfiguration(initializers = [VShopApplicationSODASmokeTests::class])
@Sql(
    scripts = ["classpath:db/oracle/create-tables.sql"],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
    config = SqlConfig(separator = "/")
)
class VShopApplicationSODASmokeTests : ApplicationContextInitializer<ConfigurableApplicationContext> {

    @LocalServerPort
    private var port: Int = 0

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

    @Test
    fun smokeTestOfAddAndSearchApi() {
        val client = HttpClient.newHttpClient()

        val uriAdd = "http://localhost:$port/products"
        val requestAdd = HttpRequest.newBuilder(URI.create(uriAdd))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(
                """{"code":"WHL1", "name":"Whaleboat","price":1000.0, "quantity":1, "description":"Whaleboat description", "revision":0}"""))
            .build()
        val responseAdd = client.send(requestAdd, HttpResponse.BodyHandlers.ofString())

        Assertions.assertEquals(responseAdd.statusCode(), 200)

        val uriSearch = "http://localhost:$port/products?textContains=whaleboat"
        val requestSearch = HttpRequest.newBuilder(URI.create(uriSearch)).GET().build()
        val responseSearch = client.send(requestSearch, HttpResponse.BodyHandlers.ofString())

        Assertions.assertEquals(responseSearch.statusCode(), 200)
        Assertions.assertTrue(responseSearch.body()!!.contains("Whaleboat"))
    }

    override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
        TestPropertyValues.of(
            "spring.datasource.url=" + oracle.jdbcUrl
        ).applyTo(configurableApplicationContext.environment)
    }
}

