package co.za.discovery.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import co.za.discovery.presentation.theme.DiscoveryTeal

@Composable
internal fun RestaurantSearchField(
    query: String,
    isActive: Boolean,
    onQueryChange: (String) -> Unit,
    onFocus: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth().onFocusChanged { if (it.isFocused) onFocus() },
        singleLine = true,
        label = { Text("Search restaurants") },
        leadingIcon = {
            if (isActive) {
                IconButton(onClick = onCloseClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Close search", tint = DiscoveryTeal)
                }
            } else {
                Icon(Icons.Default.Search, contentDescription = null)
            }
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear search")
                }
            }
        }
    )
}
