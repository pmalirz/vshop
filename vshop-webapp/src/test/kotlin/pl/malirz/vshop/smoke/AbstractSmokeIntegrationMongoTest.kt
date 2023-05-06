package pl.malirz.vshop.smoke

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.malirz.vshop.AbstractIntegrationMongoTest
import pl.malirz.vshop.TestContextInitializerMongo
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [TestContextInitializerMongo::class])
abstract class AbstractSmokeIntegrationMongoTest : AbstractIntegrationMongoTest() {

    @Test
    fun smokeTestOfAddAndSearchApi_HappyPath() {
        val client = HttpClient.newHttpClient()

        val uriAdd = "${baseUrl()}/products"
        val requestAdd = HttpRequest.newBuilder(URI.create(uriAdd))
            .header("Content-Type", "application/json")
            .POST(
                HttpRequest.BodyPublishers.ofString(
                    """{"code":"WHL1", "name":"Whaleboat","price":1000.0, "quantity":1, "description":"Whaleboat description", "revision":0}"""
                )
            )
            .build()
        val responseAdd = client.send(requestAdd, HttpResponse.BodyHandlers.ofString())

        Assertions.assertEquals(responseAdd.statusCode(), 200)

        val uriSearch = "${baseUrl()}/products?textContains=whaleboat"
        val requestSearch = HttpRequest.newBuilder(URI.create(uriSearch)).GET().build()
        val responseSearch = client.send(requestSearch, HttpResponse.BodyHandlers.ofString())

        Assertions.assertEquals(responseSearch.statusCode(), 200)
        Assertions.assertTrue(responseSearch.body()!!.contains("Whaleboat"))
    }


}

