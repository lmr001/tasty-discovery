package co.za.discovery.data

import org.junit.Assert.*
import org.junit.Test

class RestaurantDecoderTest {
    @Test
    fun toleratesNullableAndMissingFields() {
        val r =
            RestaurantDecoder().decode("""{"restaurants":[{"id":"1","name":null,"rating":9,"delivery_fee_cents":-1,"is_open":null,"extra":"ignored"}]}""")
                .single()
        assertEquals(
            "Unnamed restaurant",
            r.name
        ); assertNull(r.rating); assertNull(r.deliveryFeeCents); assertNull(r.isOpen)
    }

    @Test
    fun removesDuplicateAndMissingIds() {
        assertEquals(
            1,
            RestaurantDecoder().decode("""{"restaurants":[{"id":"1"},{"id":"1"},{}]}""").size
        )
    }

    @Test(expected = kotlinx.serialization.SerializationException::class)
    fun rejectsMalformedJson() {
        RestaurantDecoder().decode("invalid")
    }
}
