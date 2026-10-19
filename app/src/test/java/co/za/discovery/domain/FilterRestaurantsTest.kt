package co.za.discovery.domain

import org.junit.Assert.*
import org.junit.Test

class FilterRestaurantsTest {
    private val a = Restaurant("1", "Basil House", listOf("Italian"), 4.5, 0, 20, true, null)
    private val b = a.copy(id = "2", name = "Basil Garden", isOpen = false)
    @Test
    fun searchesCaseInsensitivelyAndTrims() {
        assertEquals(listOf(a, b), FilterRestaurants()(listOf(a, b), " BASIL ", false))
    }

    @Test
    fun matchesMultiTermNonContiguousQuery() {
        val sushi = Restaurant(
            "3",
            "The Sushi Room Sandton",
            listOf("Japanese", "Sushi"),
            4.8,
            2000,
            25,
            true,
            null
        )
        assertEquals(
            listOf(sushi),
            FilterRestaurants()(listOf(a, b, sushi), "Sushi Sandton", false)
        )
    }

    @Test
    fun excludesClosedAndUnknownHours() {
        assertEquals(
            listOf(a),
            FilterRestaurants()(listOf(a, b, a.copy(id = "3", isOpen = null)), "", true)
        )
    }

    @Test
    fun combinesFavouritesWithOtherFilters() {
        assertEquals(
            listOf(a),
            FilterRestaurants()(
                listOf(a, b),
                "basil",
                true,
                favoriteIds = setOf(a.id),
                isFavoritesOnly = true
            )
        )
    }

    @Test
    fun noMatchReturnsEmpty() {
        assertTrue(FilterRestaurants()(listOf(a), "pizza", false).isEmpty())
    }
}
