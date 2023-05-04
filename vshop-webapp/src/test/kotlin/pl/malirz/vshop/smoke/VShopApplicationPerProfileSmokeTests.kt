package pl.malirz.vshop.smoke

import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("SODA")
class VShopApplicationSODASmokeTests : AbstractSmokeIntegrationOracleTest() {

}

@ActiveProfiles("JSON")
class VShopApplicationJSONSmokeTests : AbstractSmokeIntegrationOracleTest() {

}

@ActiveProfiles("JPA")
class VShopApplicationJPASmokeTests : AbstractSmokeIntegrationOracleTest() {

}
