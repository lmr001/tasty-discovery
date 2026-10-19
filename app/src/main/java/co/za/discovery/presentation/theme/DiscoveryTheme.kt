package co.za.discovery.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal val DiscoveryTeal = Color(0xFF00685E)
internal val DiscoveryCream = Color(0xFFF7F8F2)
internal val DiscoveryMutedText = Color(0xFF5D6B65)

@Composable
fun DiscoveryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = DiscoveryTeal,
            background = DiscoveryCream,
            surface = Color.White,
            onPrimary = Color.White,
            secondaryContainer = Color(0xFFD7EEE4)
        ),
        content = content
    )
}
