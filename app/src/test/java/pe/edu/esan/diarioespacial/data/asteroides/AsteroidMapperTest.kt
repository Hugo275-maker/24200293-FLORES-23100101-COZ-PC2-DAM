package pe.edu.esan.diarioespacial.data.asteroides

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.edu.esan.diarioespacial.data.remote.asteroides.CloseApproachDto
import pe.edu.esan.diarioespacial.data.remote.asteroides.DiameterRangeDto
import pe.edu.esan.diarioespacial.data.remote.asteroides.EstimatedDiameterDto
import pe.edu.esan.diarioespacial.data.remote.asteroides.MissDistanceDto
import pe.edu.esan.diarioespacial.data.remote.asteroides.NeoDto
import pe.edu.esan.diarioespacial.data.remote.asteroides.NeoFeedResponseDto
import pe.edu.esan.diarioespacial.data.remote.asteroides.RelativeVelocityDto

class AsteroidMapperTest {

    private fun neoCompleto(id: String = "2465633") = NeoDto(
        id = id,
        name = "465633 (2009 JR5)",
        isPotentiallyHazardous = true,
        estimatedDiameter = EstimatedDiameterDto(DiameterRangeDto(min = 210.5, max = 470.7)),
        closeApproachData = listOf(
            CloseApproachDto(
                closeApproachDate = "2026-11-14",
                epochDateCloseApproach = 1794700000000,
                relativeVelocity = RelativeVelocityDto("65260.5462745637"),
                missDistance = MissDistanceDto("45290300.20843")
            )
        )
    )

    @Test
    fun convierteTextosANumerosYCopiaLosCampos() {
        val e = neoCompleto().aEntidad("2026-11-14")!!
        assertEquals("2465633", e.id)
        assertEquals("465633 (2009 JR5)", e.nombre)
        assertEquals("2026-11-14", e.fechaAproximacion)
        assertEquals(470.7, e.diametroMaxM!!, 0.0)
        assertEquals(65260.5462745637, e.velocidadKmh!!, 1e-9)
        assertEquals(45290300.20843, e.distanciaKm!!, 1e-6)
        assertTrue(e.esPeligroso)
        assertFalse(e.esFavorito)
    }

    @Test
    fun noFallaConCamposNulosOAusentes() {
        val e = NeoDto("123", null, null, null, null).aEntidad("2026-11-10")!!
        assertEquals("Asteroide 123", e.nombre)
        assertEquals("2026-11-10", e.fechaAproximacion) // usa la fecha del feed
        assertNull(e.diametroMaxM)
        assertNull(e.velocidadKmh)
        assertNull(e.distanciaKm)
        assertFalse(e.esPeligroso)
    }

    @Test
    fun textoNumericoInvalidoQuedaEnNull() {
        val dto = neoCompleto().copy(
            closeApproachData = listOf(
                CloseApproachDto("2026-11-14", null, RelativeVelocityDto("abc"), MissDistanceDto(""))
            )
        )
        val e = dto.aEntidad(null)!!
        assertNull(e.velocidadKmh)
        assertNull(e.distanciaKm)
    }

    @Test
    fun descartaSinIdYAplanaElFeedSinDuplicados() {
        val feed = NeoFeedResponseDto(
            elementCount = 4,
            nearEarthObjects = mapOf(
                "2026-11-14" to listOf(neoCompleto("1"), NeoDto(null, "sin id", null, null, null)),
                "2026-11-13" to listOf(neoCompleto("2"), neoCompleto("1")),
                "2026-11-12" to null
            )
        )
        val entidades = feed.aEntidades()
        assertEquals(listOf("1", "2"), entidades.map { it.id })
    }

    @Test
    fun feedVacioDevuelveListaVacia() {
        assertTrue(NeoFeedResponseDto(null, null).aEntidades().isEmpty())
    }
}
