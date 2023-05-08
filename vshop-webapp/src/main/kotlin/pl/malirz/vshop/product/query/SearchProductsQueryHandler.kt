package pl.malirz.vshop.product.query

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.malirz.cqrs.QueryHandler

@Service
private class SearchProductsQueryHandler(private val repository: SearchProductsRepository) :
    QueryHandler<SearchProductsQuery, SearchProductsListView> {

    @Transactional
    override fun apply(query: SearchProductsQuery): SearchProductsListView {
        return repository.apply(query)
    }
}
