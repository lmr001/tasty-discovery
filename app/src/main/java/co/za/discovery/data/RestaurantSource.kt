package co.za.discovery.data

import android.content.Context
import co.za.discovery.domain.Restaurant
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface RestaurantSource {
    suspend fun fetch(): String
}

// The assignment explicitly permits a local JSON source. Swap this adapter for HTTP later.
class AssetRestaurantSource(private val context: Context) : RestaurantSource {
    override suspend fun fetch(): String = withContext(Dispatchers.IO) {
        if (co.za.discovery.BuildConfig.SIMULATE_FAILURE) throw java.io.IOException("Simulated source failure")
        context.assets.open("restaurants.json").bufferedReader().use { it.readText() }
    }
}

@Serializable
private data class Payload(val restaurants: List<Dto> = emptyList())
@Serializable
private data class Dto(
    val id: String? = null,
    val name: String? = null,
    val cuisines: List<String>? = null,
    val rating: Double? = null,
    @SerialName("delivery_fee_cents") val fee: Int? = null,
    @SerialName("eta_minutes") val eta: Int? = null,
    @SerialName("is_open") val open: Boolean? = null,
    @SerialName("image_url") val image: String? = null
)

class RestaurantDecoder {
    private val json = Json { ignoreUnknownKeys = true }
    fun decode(raw: String): List<Restaurant> =
        json.decodeFromString<Payload>(raw).restaurants.mapNotNull { dto ->
            val id = dto.id?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
            Restaurant(
                id,
                dto.name?.takeIf { it.isNotBlank() } ?: "Unnamed restaurant",
                dto.cuisines.orEmpty(),
                dto.rating?.takeIf { it.isFinite() && it in 0.0..5.0 },
                dto.fee?.takeIf { it >= 0 },
                dto.eta?.takeIf { it > 0 },
                dto.open,
                dto.image?.takeIf { it.startsWith("https://") })
        }.distinctBy { it.id }
}
