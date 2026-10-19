package co.za.discovery.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.za.discovery.presentation.DiscoveryState
import co.za.discovery.presentation.components.CatalogMessage
import co.za.discovery.presentation.components.DiscoveryHeader
import co.za.discovery.presentation.components.EmptyRestaurants
import co.za.discovery.presentation.components.RestaurantCard
import co.za.discovery.presentation.components.RestaurantFilterBar
import co.za.discovery.presentation.components.RestaurantSearchField

@Composable
internal fun BrowseScreen(
    state: DiscoveryState,
    searchQuery: String,
    isSearchActive: Boolean,
    isHeaderVisible: Boolean,
    listState: LazyListState,
    onSearchQueryChange: (String) -> Unit,
    onSearchFocused: () -> Unit,
    onCloseSearchClick: () -> Unit,
    onOpenOnlyChange: (Boolean) -> Unit,
    onFavoritesOnlyChange: (Boolean) -> Unit,
    onRefreshClick: () -> Unit,
    onResetFiltersClick: () -> Unit,
    onRestaurantClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        BrowseControls(
            state = state,
            searchQuery = searchQuery,
            isSearchActive = isSearchActive,
            isHeaderVisible = isHeaderVisible,
            onSearchQueryChange = onSearchQueryChange,
            onSearchFocused = onSearchFocused,
            onCloseSearchClick = onCloseSearchClick,
            onOpenOnlyChange = onOpenOnlyChange,
            onFavoritesOnlyChange = onFavoritesOnlyChange,
            onRefreshClick = onRefreshClick
        )
        CatalogMessage(state.catalog.message, state.catalog.isRefreshing, onRefreshClick)
        if (state.catalog.isRefreshing) LinearProgressIndicator(Modifier.fillMaxWidth())
        if (state.restaurants.isEmpty()) {
            EmptyRestaurants(state.catalog.message != null, onResetFiltersClick)
        } else {
            RestaurantList(state, listState, onRestaurantClick, onFavoriteClick)
        }
    }
}

@Composable
private fun BrowseControls(
    state: DiscoveryState,
    searchQuery: String,
    isSearchActive: Boolean,
    isHeaderVisible: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onSearchFocused: () -> Unit,
    onCloseSearchClick: () -> Unit,
    onOpenOnlyChange: (Boolean) -> Unit,
    onFavoritesOnlyChange: (Boolean) -> Unit,
    onRefreshClick: () -> Unit
) {
    Column(Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        DiscoveryHeader(isHeaderVisible, state.catalog.isRefreshing, onRefreshClick)
        RestaurantSearchField(searchQuery, isSearchActive, onSearchQueryChange, onSearchFocused, onCloseSearchClick)
        RestaurantFilterBar(
            state.isOpenOnly,
            state.isFavoritesOnly,
            state.restaurants.size,
            onOpenOnlyChange,
            onFavoritesOnlyChange
        )
    }
}

@Composable
private fun RestaurantList(
    state: DiscoveryState,
    listState: LazyListState,
    onRestaurantClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(state.restaurants, key = { it.id }, contentType = { "restaurant" }) { restaurant ->
            RestaurantCard(
                restaurant = restaurant,
                isFavorite = restaurant.id in state.favoriteRestaurantIds,
                onRestaurantClick = { onRestaurantClick(restaurant.id) },
                onFavoriteClick = { onFavoriteClick(restaurant.id) }
            )
        }
    }
}
