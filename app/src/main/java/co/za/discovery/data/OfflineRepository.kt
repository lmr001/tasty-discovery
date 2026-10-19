package co.za.discovery.data

import android.content.Context
import android.util.AtomicFile
import co.za.discovery.domain.Catalog
import co.za.discovery.domain.RestaurantRepository
import java.io.File
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class OfflineRepository(
    context: Context,
    private val source: RestaurantSource,
    private val decoder: RestaurantDecoder
) : RestaurantRepository {

    private val cache = AtomicFile(File(context.filesDir, "restaurants.json"))
    private val preferences = context.getSharedPreferences("discovery", Context.MODE_PRIVATE)

    private val refreshLock = Mutex()
    private val favoriteLock = Mutex()

    private val mutableCatalog = MutableStateFlow(Catalog())
    private val mutableFavoriteIds = MutableStateFlow(
        preferences.getStringSet("favorites", emptySet())!!.toSet()
    )

    override val catalog: StateFlow<Catalog> = mutableCatalog.asStateFlow()
    override val favoriteIds: StateFlow<Set<String>> = mutableFavoriteIds.asStateFlow()

    private var cacheRead = false

    override suspend fun refresh() = refreshLock.withLock {
        withContext(Dispatchers.IO) {
            if (!cacheRead) {
                try {
                    val list = decoder.decode(cache.openRead().bufferedReader().use { it.readText() })
                    mutableCatalog.value = Catalog(list, savedAt = cache.baseFile.lastModified())
                } catch (cancelled: CancellationException) {
                    throw cancelled
                } catch (_: Exception) {
                    /* Missing or corrupt cache: load bundled baseline. */
                }
                cacheRead = true
            }

            mutableCatalog.update { it.copy(isRefreshing = true, message = null) }

            try {
                delay(800L)
                val raw = source.fetch()
                val list = decoder.decode(raw)
                val stream = cache.startWrite()
                try {
                    stream.write(raw.toByteArray(Charsets.UTF_8))
                    cache.finishWrite(stream)
                } catch (error: Exception) {
                    cache.failWrite(stream)
                    throw error
                }
                mutableCatalog.value = Catalog(list, isRefreshing = false, savedAt = cache.baseFile.lastModified())
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                val errorMessage = if (mutableCatalog.value.savedAt != null) {
                    "Couldn’t refresh. Showing your saved restaurants."
                } else {
                    "Restaurants couldn’t load. Please try again."
                }
                mutableCatalog.update { it.copy(isRefreshing = false, message = errorMessage) }
            }
        }
    }

    override suspend fun toggleFavorite(id: String) = favoriteLock.withLock {
        withContext(Dispatchers.IO) {
            val next = mutableFavoriteIds.value.toMutableSet().apply {
                if (!add(id)) remove(id)
            }.toSet()

            if (!preferences.edit().putStringSet("favorites", next).commit()) {
                throw IOException("Favorite could not be saved")
            }
            mutableFavoriteIds.value = next
        }
    }
}
