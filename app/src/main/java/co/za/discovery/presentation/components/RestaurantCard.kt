package co.za.discovery.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.za.discovery.domain.Restaurant
import co.za.discovery.presentation.formatting.availabilityLabel
import co.za.discovery.presentation.formatting.formatDeliveryFee
import co.za.discovery.presentation.theme.DiscoveryMutedText
import co.za.discovery.presentation.theme.DiscoveryTeal
import java.util.Locale

@Composable
internal fun RestaurantCard(
    restaurant: Restaurant,
    isFavorite: Boolean,
    onRestaurantClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onRestaurantClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box {
            RestaurantImage(restaurant, Modifier
                .fillMaxWidth()
                .height(148.dp)
            )
            FavoriteButton(
                restaurantName = restaurant.name,
                isFavorite = isFavorite,
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            )
            AvailabilityBadge(
                restaurant = restaurant,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            )
        }
        RestaurantCardContent(restaurant)
    }
}

@Composable
private fun AvailabilityBadge(restaurant: Restaurant, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(8.dp), color = Color.White) {
        Text(
            text = availabilityLabel(restaurant),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelMedium,
            color = if (restaurant.isOpen == true) DiscoveryTeal else Color(0xFF5D615D)
        )
    }
}

@Composable
private fun RestaurantCardContent(restaurant: Restaurant) {
    Column(Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = restaurant.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(text = restaurant.rating?.let { "★ %.1f".format(Locale.ROOT, it) } ?: "Unrated",
                color = DiscoveryTeal,
                style = MaterialTheme.typography.labelLarge)
        }
        Text(
            text = restaurant.cuisines.joinToString(" • ").ifEmpty { "Cuisine unavailable" },
            style = MaterialTheme.typography.bodyMedium,
            color = DiscoveryMutedText
        )
        Spacer(Modifier.height(12.dp))
        Text(text = "${restaurant.etaMinutes?.let { "$it min" } ?: "ETA unavailable"}  •  ${
            formatDeliveryFee(
                restaurant.deliveryFeeCents
            )
        }", style = MaterialTheme.typography.labelLarge)
    }
}
