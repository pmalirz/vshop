package pl.malirz.vshop.product.query

import pl.malirz.vshop.product.command.Product
import java.util.function.Function

interface SearchProductsRepository : Function<SearchProductsQuery, SearchProductsListView>