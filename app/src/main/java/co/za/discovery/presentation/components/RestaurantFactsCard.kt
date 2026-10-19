package co.za.discovery.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.za.discovery.domain.Restaurant
import co.za.discovery.presentation.formatting.availabilityLabel
import co.za.discovery.presentation.formatting.formatDeliveryFee
import co.za.discovery.presentation.theme.DiscoveryMutedText
import java.util.Locale

@Composable
internal fun RestaurantFactsCard(
    restaurant: Restaurant,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxWidth(), color = Color.White) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            RestaurantFact("Rating", restaurant.rating?.let { "★ %.1f / 5".format(Locale.ROOT, it) } ?: "Not yet rated")
            RestaurantFact("Estimated delivery", restaurant.etaMinutes?.let { "$it minutes" } ?: "Unavailable")
            RestaurantFact("Delivery fee", formatDeliveryFee(restaurant.deliveryFeeCents))
            RestaurantFact("Availability", availabilityLabel(restaurant))
        }
    }
}

@Composable
private fun RestaurantFact(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(label, Modifier.weight(1f), color = DiscoveryMutedText)
        Text(value, Modifier.weight(1f), fontWeight = FontWeight.Medium)
    }
}
