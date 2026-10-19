package co.za.discovery.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.za.discovery.presentation.theme.DiscoveryMutedText
import co.za.discovery.presentation.theme.DiscoveryTeal

@Composable
internal fun DiscoveryHeader(
    isVisible: Boolean,
    isRefreshing: Boolean,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        modifier = modifier,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = DiscoveryTeal)
                Column(Modifier.weight(1f).padding(start = 8.dp)) {
                    Text("EXPLORING", style = MaterialTheme.typography.labelSmall, color = DiscoveryTeal)
                    Text("Johannesburg • Demo area", style = MaterialTheme.typography.titleSmall)
                }
                IconButton(onClick = onRefreshClick, enabled = !isRefreshing) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh restaurants")
                }
            }
            Text(
                text = "Good food.\nGreat discoveries.",
                modifier = Modifier.padding(top = 20.dp),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Find your next local favourite",
                modifier = Modifier.padding(top = 6.dp, bottom = 18.dp),
                color = DiscoveryMutedText
            )
        }
    }
}
