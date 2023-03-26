package pl.malirz.vshop

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("JPA")
class VShopApplicationWithJpaTests {
    @Test
    fun contextLoads() {
    }
}

@SpringBootTest
@ActiveProfiles("SODA")
class VShopApplicationWithSodaTests {
    @Test
    fun contextLoads() {
    }
}