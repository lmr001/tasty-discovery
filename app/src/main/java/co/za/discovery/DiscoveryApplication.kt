package co.za.discovery

import android.app.Application
import co.za.discovery.data.*
import co.za.discovery.domain.RestaurantRepository

class DiscoveryApplication : Application() {
 val repository: RestaurantRepository by lazy { OfflineRepository(this, AssetRestaurantSource(this), RestaurantDecoder()) }
}
