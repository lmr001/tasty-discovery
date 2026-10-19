package co.za.discovery.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.za.discovery.domain.Catalog
import co.za.discovery.domain.FilterRestaurants
import co.za.discovery.domain.Restaurant
import co.za.discovery.domain.RestaurantRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DiscoveryState(
    val catalog: Catalog = Catalog(),
    val restaurants: List<Restaurant> = emptyList(),
    val favoriteRestaurantIds: Set<String> = emptySet(),
    val searchQuery: String = "",
    val isOpenOnly: Boolean = false,
    val isFavoritesOnly: Boolean = false,
    val actionError: String? = null
)

class DiscoveryViewModel(
    private val repository: RestaurantRepository,
    private val filterRestaurants: FilterRestaurants = FilterRestaurants()
) : ViewModel() {
    private val searchQuery = MutableStateFlow("")
    private val isOpenOnly = MutableStateFlow(false)
    private val isFavoritesOnly = MutableStateFlow(false)
    private val actionError = MutableStateFlow<String?>(null)

    private val filteredState = combine(
        repository.catalog,
        repository.favoriteIds,
        searchQuery,
        isOpenOnly,
        isFavoritesOnly
    ) { catalog, favoriteIds, query, openOnly, favoritesOnly ->
        DiscoveryState(
            catalog = catalog,
            restaurants = filterRestaurants(
                restaurants = catalog.restaurants,
                query = query,
                isOpenOnly = openOnly,
                favoriteIds = favoriteIds,
                isFavoritesOnly = favoritesOnly
            ),
            favoriteRestaurantIds = favoriteIds,
            searchQuery = query,
            isOpenOnly = openOnly,
            isFavoritesOnly = favoritesOnly
        )
    }.flowOn(Dispatchers.Default)

    val state = combine(filteredState, actionError) { discoveryState, error ->
        discoveryState.copy(actionError = error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DiscoveryState()
    )

    init {
        refreshRestaurants()
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun setOpenOnly(enabled: Boolean) {
        isOpenOnly.value = enabled
    }

    fun setFavoritesOnly(enabled: Boolean) {
        isFavoritesOnly.value = enabled
    }

    fun refreshRestaurants() {
        viewModelScope.launch { repository.refresh() }
    }

    fun toggleFavorite(restaurantId: String) {
        viewModelScope.launch {
            try {
                repository.toggleFavorite(restaurantId)
                actionError.value = null
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                actionError.value = "Couldn’t save your favourite. Please try again."
            }
        }
    }

    fun dismissActionError() {
        actionError.value = null
    }
}
