package pe.edu.esan.diarioespacial.data.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ApodDtoTest {

    private val logo = "https://science.nasa.gov/wp-content/themes/nasa-child/assets/images/nasa-logo@2x.png"

    @Test
    fun logoGenericoDeLaNasaSeGuardaComoSinImagen() {
        val e = ApodDto("2026-11-14", "NASA Science", "Texto", logo, logo, "image", null).toEntity()!!
        assertNull(e.url)
        assertNull(e.hdurl)
        assertEquals("Texto", e.explanation)
    }

    @Test
    fun urlNormalSeConserva() {
        val url = "https://apod.nasa.gov/apod/image/2611/foto.jpg"
        val e = ApodDto("2026-11-14", "Foto", "Texto", url, null, "image", " Autor ").toEntity()!!
        assertEquals(url, e.url)
        assertNull(e.hdurl)
        assertEquals("Autor", e.copyright)
    }

    @Test
    fun camposNulosNoRompenLaConversion() {
        val e = ApodDto("2026-11-14", null, null, null, null, null, null).toEntity()!!
        assertEquals("Sin título", e.title)
        assertEquals("image", e.mediaType)
        assertNull(ApodDto(null, "x", "y", null, null, null, null).toEntity())
    }
}
