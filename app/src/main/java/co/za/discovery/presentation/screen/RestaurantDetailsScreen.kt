package co.za.discovery.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.za.discovery.domain.Restaurant
import co.za.discovery.presentation.components.FavoriteButton
import co.za.discovery.presentation.components.RestaurantFactsCard
import co.za.discovery.presentation.components.RestaurantImage
import co.za.discovery.presentation.formatting.availabilityLabel
import co.za.discovery.presentation.theme.DiscoveryMutedText
import co.za.discovery.presentation.theme.DiscoveryTeal

@Composable
internal fun RestaurantDetailsScreen(
    restaurant: Restaurant,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(bottom = 32.dp)) {
        item {
            RestaurantDetailsHero(restaurant, isFavorite, onBackClick, onFavoriteClick)
            Column(Modifier.padding(24.dp)) {
                Text(availabilityLabel(restaurant), color = DiscoveryTeal, style = MaterialTheme.typography.labelLarge)
                Text(restaurant.name, modifier = Modifier.padding(vertical = 8.dp), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                Text(restaurant.cuisines.joinToString(" • ").ifEmpty { "Cuisine unavailable" }, color = DiscoveryMutedText)
                Spacer(Modifier.height(24.dp))
                RestaurantFactsCard(restaurant)
                Spacer(Modifier.height(24.dp))
                Text("A little closer to your next favourite", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Save this restaurant to find it again. This discovery demo uses fictional restaurants; ordering and live location are outside its scope.",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(24.dp))
                Button(onClick = onFavoriteClick, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
                    Text(if (isFavorite) "Remove from favourites" else "Save to favourites")
                }
            }
        }
    }
}

@Composable
private fun RestaurantDetailsHero(
    restaurant: Restaurant,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Box {
        RestaurantImage(restaurant, Modifier.fillMaxWidth().height(260.dp))
        Surface(Modifier.align(Alignment.TopStart).padding(16.dp)) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to restaurants")
            }
        }
        FavoriteButton(
            restaurantName = restaurant.name,
            isFavorite = isFavorite,
            onClick = onFavoriteClick,
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
        )
    }
}

@Composable
internal fun RestaurantUnavailableScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.padding(24.dp)) {
        Text("Restaurant unavailable")
        TextButton(onClick = onBackClick) { Text("Back to restaurants") }
    }
}
