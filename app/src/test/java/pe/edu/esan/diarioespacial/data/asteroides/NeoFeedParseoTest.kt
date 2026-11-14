package pe.edu.esan.diarioespacial.data.asteroides

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.edu.esan.diarioespacial.data.remote.asteroides.NeoFeedResponseDto

/**
 * Lee un extracto REAL de la respuesta de NeoWs (test/resources/neo_feed_muestra.json)
 * con Gson, igual que Retrofit, y verifica la conversión a entidades.
 */
class NeoFeedParseoTest {

    private fun leerMuestra(): NeoFeedResponseDto {
        val json = javaClass.classLoader!!.getResource("neo_feed_muestra.json")!!.readText()
        return Gson().fromJson(json, NeoFeedResponseDto::class.java)
    }

    @Test
    fun parseaElFeedAgrupadoPorFecha() {
        val feed = leerMuestra()
        assertEquals(setOf("2026-11-08", "2026-11-09"), feed.nearEarthObjects!!.keys)

        val entidades = feed.aEntidades()
        assertEquals(4, entidades.size)

        val peligroso = entidades.first { it.id == "2138175" }
        assertEquals("138175 (2000 EE104)", peligroso.nombre)
        assertEquals("2026-11-08", peligroso.fechaAproximacion)
        assertTrue(peligroso.esPeligroso)
        assertEquals(26190.1123249462, peligroso.velocidadKmh!!, 1e-6)
        assertNotNull(peligroso.distanciaKm)
        assertNotNull(peligroso.diametroMinM)
        assertTrue(peligroso.diametroMaxM!! > peligroso.diametroMinM!!)

        assertFalse(entidades.first { it.id == "3092201" }.esPeligroso)
    }
}
