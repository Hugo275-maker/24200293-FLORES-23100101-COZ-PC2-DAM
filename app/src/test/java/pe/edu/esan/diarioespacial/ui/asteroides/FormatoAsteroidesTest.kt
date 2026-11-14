package pe.edu.esan.diarioespacial.ui.asteroides

import org.junit.Assert.assertEquals
import org.junit.Test

class FormatoAsteroidesTest {

    @Test
    fun redondeaAMaximoDosDecimalesConUnidad() {
        assertEquals("65,260.55 km/h", FormatoAsteroides.kmh(65260.5462745637))
        assertEquals("470.71 m", FormatoAsteroides.metros(470.7066))
        assertEquals("45,290,300.21 km", FormatoAsteroides.km(45290300.20843))
    }

    @Test
    fun noAgregaCerosInnecesarios() {
        assertEquals("120 m", FormatoAsteroides.metros(120.0))
        assertEquals("12.5 m", FormatoAsteroides.metros(12.5))
    }

    @Test
    fun valorNuloMuestraNoDisponible() {
        assertEquals(FormatoAsteroides.NO_DISPONIBLE, FormatoAsteroides.km(null))
    }

    @Test
    fun formateaLaFechaOLaDejaIgualSiNoEsValida() {
        assertEquals("14 de noviembre de 2026", FormatoAsteroides.fecha("2026-11-14"))
        assertEquals("2026-Nov-14", FormatoAsteroides.fecha("2026-Nov-14"))
        assertEquals("Sin fecha", FormatoAsteroides.fecha(null))
    }
}
