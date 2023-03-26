package pl.malirz.vshop

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@Configuration
@EnableJpaRepositories
@Profile("JPA")
class VShopJpaConfig {

}