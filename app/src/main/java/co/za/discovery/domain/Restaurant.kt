package co.za.discovery.domain

data class Restaurant(
    val id: String,
    val name: String,
    val cuisines: List<String>,
    val rating: Double?,
    val deliveryFeeCents: Int?,
    val etaMinutes: Int?,
    val isOpen: Boolean?,
    val imageUrl: String?
)

data class Catalog(
    val restaurants: List<Restaurant> = emptyList(),
    val isRefreshing: Boolean = true,
    val message: String? = null,
    val savedAt: Long? = null
)

interface RestaurantRepository {
    val catalog: kotlinx.coroutines.flow.StateFlow<Catalog>
    val favoriteIds: kotlinx.coroutines.flow.StateFlow<Set<String>>
    suspend fun refresh()
    suspend fun toggleFavorite(id: String)
}

class FilterRestaurants {
    private val whitespaceRegex = Regex("\\s+")

    operator fun invoke(
        restaurants: List<Restaurant>,
        query: String,
        isOpenOnly: Boolean,
        favoriteIds: Set<String> = emptySet(),
        isFavoritesOnly: Boolean = false
    ): List<Restaurant> {
        val terms = query.trim().split(whitespaceRegex).filter { it.isNotEmpty() }
        return restaurants.filter { restaurant ->
            if (isOpenOnly && restaurant.isOpen != true) return@filter false
            if (isFavoritesOnly && restaurant.id !in favoriteIds) return@filter false
            if (terms.isEmpty()) return@filter true
            val searchableText = "${restaurant.name} ${restaurant.cuisines.joinToString(" ")}"
            terms.all { term -> searchableText.contains(term, ignoreCase = true) }
        }
    }
}
