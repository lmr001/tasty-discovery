package co.za.discovery.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.za.discovery.presentation.theme.DiscoveryTeal

@Composable
internal fun CatalogMessage(
    message: String?,
    isRefreshing: Boolean,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    message ?: return
    Surface(
        modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(message)
            TextButton(onClick = onRetryClick, enabled = !isRefreshing) { Text("Try again") }
        }
    }
}

@Composable
internal fun EmptyRestaurants(
    hasLoadError: Boolean,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(48.dp), tint = DiscoveryTeal)
        Text(if (hasLoadError) "Nothing loaded yet" else "No matches yet", style = MaterialTheme.typography.titleLarge)
        Text("Try again.")
        TextButton(onClick = onResetClick) { Text("Reset filters") }
    }
}
