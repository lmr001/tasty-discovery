package co.za.discovery.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.za.discovery.presentation.DiscoveryState
import co.za.discovery.presentation.DiscoveryViewModel
import co.za.discovery.presentation.components.LoadingScreen
import co.za.discovery.presentation.theme.DiscoveryCream
import kotlinx.coroutines.delay

@Composable
fun DiscoveryScreen(viewModel: DiscoveryViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var selectedRestaurantId by rememberSaveable { mutableStateOf<String?>(null) }
    var searchQuery by rememberSaveable { mutableStateOf(state.searchQuery) }
    var isSearchActive by remember { mutableStateOf(false) }
    var isSplashVisible by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        delay(3000L)
        isSplashVisible = false
    }
    LaunchedEffect(state.actionError) {
        state.actionError?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissActionError()
        }
    }
    LaunchedEffect(state.searchQuery) {
        if (state.searchQuery != searchQuery) searchQuery = state.searchQuery
    }
    LaunchedEffect(state.searchQuery, state.isOpenOnly, state.isFavoritesOnly) {
        if (state.restaurants.isNotEmpty()) listState.scrollToItem(0)
    }

    fun closeSearch() {
        focusManager.clearFocus()
        isSearchActive = false
        searchQuery = ""
        viewModel.updateSearchQuery("")
    }

    val isHeaderVisible by remember {
        derivedStateOf {
            !isSearchActive && listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset <= 20
        }
    }

    BackHandler(isSearchActive && selectedRestaurantId == null) { closeSearch() }
    BackHandler(selectedRestaurantId != null) { selectedRestaurantId = null }

    Scaffold(
        containerColor = DiscoveryCream,
        snackbarHost = { SnackbarHost(snackbarHostState) }) { contentPadding ->
        DiscoveryContent(
            state = state,
            selectedRestaurantId = selectedRestaurantId,
            searchQuery = searchQuery,
            isSearchActive = isSearchActive,
            isHeaderVisible = isHeaderVisible,
            isSplashVisible = isSplashVisible,
            onSearchQueryChange = { searchQuery = it; viewModel.updateSearchQuery(it) },
            onSearchFocused = { isSearchActive = true },
            onCloseSearchClick = ::closeSearch,
            onOpenOnlyChange = viewModel::setOpenOnly,
            onFavoritesOnlyChange = viewModel::setFavoritesOnly,
            onRefreshClick = viewModel::refreshRestaurants,
            onResetFiltersClick = {
                closeSearch()
                viewModel.setOpenOnly(false)
                viewModel.setFavoritesOnly(false)
            },
            onRestaurantClick = { selectedRestaurantId = it },
            onBackClick = { selectedRestaurantId = null },
            onFavoriteClick = viewModel::toggleFavorite,
            modifier = Modifier.padding(contentPadding),
            listState = listState
        )
    }
}

@Composable
private fun DiscoveryContent(
    state: DiscoveryState,
    selectedRestaurantId: String?,
    searchQuery: String,
    isSearchActive: Boolean,
    isHeaderVisible: Boolean,
    isSplashVisible: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onSearchFocused: () -> Unit,
    onCloseSearchClick: () -> Unit,
    onOpenOnlyChange: (Boolean) -> Unit,
    onFavoritesOnlyChange: (Boolean) -> Unit,
    onRefreshClick: () -> Unit,
    onResetFiltersClick: () -> Unit,
    onRestaurantClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onFavoriteClick: (String) -> Unit,
    listState: androidx.compose.foundation.lazy.LazyListState,
    modifier: Modifier = Modifier
) {
    if (isSplashVisible || (state.catalog.isRefreshing && state.catalog.restaurants.isEmpty())) {
        LoadingScreen()
        return
    }

    if (selectedRestaurantId == null) {
        BrowseScreen(
            state = state,
            searchQuery = searchQuery,
            isSearchActive = isSearchActive,
            isHeaderVisible = isHeaderVisible,
            listState = listState,
            onSearchQueryChange = onSearchQueryChange,
            onSearchFocused = onSearchFocused,
            onCloseSearchClick = onCloseSearchClick,
            onOpenOnlyChange = onOpenOnlyChange,
            onFavoritesOnlyChange = onFavoritesOnlyChange,
            onRefreshClick = onRefreshClick,
            onResetFiltersClick = onResetFiltersClick,
            onRestaurantClick = onRestaurantClick,
            onFavoriteClick = onFavoriteClick,
            modifier = modifier
        )
        return
    }

    val selectedRestaurant = state.catalog.restaurants.find { it.id == selectedRestaurantId }
    if (selectedRestaurant == null) {
        RestaurantUnavailableScreen(onBackClick, modifier)
    } else {
        RestaurantDetailsScreen(
            restaurant = selectedRestaurant,
            isFavorite = selectedRestaurant.id in state.favoriteRestaurantIds,
            onBackClick = onBackClick,
            onFavoriteClick = { onFavoriteClick(selectedRestaurant.id) },
            modifier = modifier
        )
    }
}
