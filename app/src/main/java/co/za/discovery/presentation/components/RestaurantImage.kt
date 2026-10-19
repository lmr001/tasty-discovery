package co.za.discovery.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import co.za.discovery.domain.Restaurant
import co.za.discovery.presentation.theme.DiscoveryTeal
import coil.compose.AsyncImage

@Composable
private fun RestaurantPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(
            Brush.linearGradient(listOf(Color(0xFFDCEEE2), Color(0xFFF5DEC0)))
        ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Restaurant,
            contentDescription = null,
            tint = DiscoveryTeal.copy(alpha = 0.65f),
            modifier = Modifier.size(56.dp)
        )
    }
}

@Composable
internal fun RestaurantImage(
    restaurant: Restaurant,
    modifier: Modifier = Modifier
) {
    if (restaurant.imageUrl == null) {
        RestaurantPlaceholder(modifier)
    } else {
        Box(modifier = modifier) {
            RestaurantPlaceholder(Modifier.matchParentSize())
            AsyncImage(
                model = restaurant.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}
