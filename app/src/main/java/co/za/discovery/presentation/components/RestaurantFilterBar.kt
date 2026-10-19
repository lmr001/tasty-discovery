package co.za.discovery.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun RestaurantFilterBar(
    isOpenOnly: Boolean,
    isFavoritesOnly: Boolean,
    resultCount: Int,
    onOpenOnlyChange: (Boolean) -> Unit,
    onFavoritesOnlyChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(top = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text("Filters", style = MaterialTheme.typography.labelSmall)
    }
    Row(
        modifier = modifier.fillMaxWidth().padding(top = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChip(
            selected = isOpenOnly,
            onClick = { onOpenOnlyChange(!isOpenOnly) },
            label = { Text("Open now") },
            leadingIcon = { Icon(Icons.Default.Schedule, null, Modifier.size(16.dp)) }
        )
        Spacer(Modifier.size(8.dp))
        FilterChip(
            selected = isFavoritesOnly,
            onClick = { onFavoritesOnlyChange(!isFavoritesOnly) },
            label = { Text("Favourites") },
            leadingIcon = { Icon(Icons.Default.Favorite, null, Modifier.size(16.dp)) }
        )
        Spacer(Modifier.weight(1f))
        Text("$resultCount places", style = MaterialTheme.typography.labelLarge)
    }
}
