package co.za.discovery.presentation.formatting

import co.za.discovery.domain.Restaurant
import java.text.NumberFormat
import java.util.Locale

private val zarCurrencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-ZA"))

internal fun formatDeliveryFee(cents: Int?): String = when (cents) {
    null -> "Fee unavailable"
    0 -> "Free delivery"
    else -> "${zarCurrencyFormat.format(cents / 100.0)} delivery"
}

internal fun availabilityLabel(restaurant: Restaurant): String = when (restaurant.isOpen) {
    true -> "Open now"
    false -> "Closed"
    null -> "Hours unavailable"
}
