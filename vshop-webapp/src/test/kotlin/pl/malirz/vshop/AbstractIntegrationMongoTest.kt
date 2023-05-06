package pl.malirz.vshop

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.builder.ImageFromDockerfile
import java.nio.file.Path

abstract class AbstractIntegrationMongoTest {

    @LocalServerPort
    private var port: Int = 0

    fun baseUrl() = "http://localhost:$port"

    companion object {
        /*        val mongo = GenericContainer(
                    ImageFromDockerfile()
                        .withFileFromClasspath("Dockerfile", "mongo/Dockerfile")
                        .withFileFromClasspath("/data/replica.key", "mongo/data/replica.key")
                        .withFileFromClasspath(
                            "/data/scripts/1-create-users.js", "mongo/data/scripts/1-create-users.js"
                        )
                        .withFileFromClasspath(
                            "/data/scripts/2-init-replica-set.sh", "mongo/data/scripts/2-init-replica-set.sh"
                        )
                ).waitingFor(Wait.forLogMessage(".*Successfully initiated replica set.*", 1))
                    .withExposedPorts(27017)
                    .withEnv("MONGO_INITDB_DATABASE", "vshop")
                    .withEnv("MONGO_INITDB_ROOT_USERNAME", "root")
                    .withEnv("MONGO_INITDB_ROOT_PASSWORD", "root")*/

        private val mongoDir = "../vshop-docker/mongo"

        val mongo = GenericContainer(
            ImageFromDockerfile()
                .withFileFromPath(".", Path.of(mongoDir))
                .withFileFromPath("Dockerfile", Path.of("$mongoDir/Dockerfile"))
                .withFileFromPath("/data/replica.key", Path.of("$mongoDir/data/replica.key"))
                .withFileFromPath(
                    "/data/scripts/1-create-users.js", Path.of("$mongoDir/data/scripts/1-create-users.js")
                )
                .withFileFromPath(
                    "/data/scripts/2-init-replica-set.sh", Path.of("$mongoDir/data/scripts/2-init-replica-set.sh")
                )
        ).waitingFor(Wait.forLogMessage(".*Successfully initiated replica set.*", 1))
            .withExposedPorts(27017)
            .withEnv("MONGO_INITDB_DATABASE", "vshop")
            .withEnv("MONGO_INITDB_ROOT_USERNAME", "root")
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", "root")

        @JvmStatic
        @BeforeAll
        fun start() {
            mongo.start()
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            mongo.stop()
        }
    }
}

class TestContextInitializerMongo : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
        // The port is selected by the container at random from the ephemeral port range
        val mappedPort = AbstractIntegrationMongoTest.mongo.getMappedPort(27017)
        TestPropertyValues.of(
            "spring.data.mongodb.uri=mongodb://vshop:vshop@localhost:$mappedPort/vshop"
        ).applyTo(configurableApplicationContext.environment)
    }
}
