package pl.malirz.vshop.product.query

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Function

@Service
internal class SearchProductsQueryHandler(private val repository: SearchProductsRepository) :
    Function<SearchProductsQuery, List<SearchProductsView>> {

    @Transactional
    override fun apply(query: SearchProductsQuery): List<SearchProductsView> {
        return repository.apply(query)
    }
}
