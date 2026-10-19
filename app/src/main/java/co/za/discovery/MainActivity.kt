package co.za.discovery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import co.za.discovery.presentation.DiscoveryViewModel
import co.za.discovery.presentation.screen.DiscoveryScreen
import co.za.discovery.presentation.theme.DiscoveryTheme

class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  enableEdgeToEdge()
  val repository = (application as DiscoveryApplication).repository
  setContent {
   val viewModel: DiscoveryViewModel = viewModel(factory = object : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST") override fun <T : ViewModel> create(modelClass: Class<T>): T = DiscoveryViewModel(repository) as T
   })
   DiscoveryTheme { DiscoveryScreen(viewModel) }
  }
 }
}
