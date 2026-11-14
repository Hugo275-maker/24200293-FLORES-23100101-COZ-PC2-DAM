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
        assertEquals(url, e.hdurl) // sin hdurl, el detalle usa la url normal
        assertEquals("Autor", e.copyright)
    }

    @Test
    fun formatoDelEndpointNuevoUsaHdurlYLimpiaHtml() {
        // Así responde science.nasa.gov/wp-json/wp/v2/apod-basic: url es una página HTML.
        val pagina = "https://science.nasa.gov/image-article/apod-2026-november-14-apollo-12/"
        val foto = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/foto.jpg?w=2181"
        val e = ApodDto(
            date = "2026-11-14",
            title = "Apollo 12 and Surveyor 3",
            explanation = "<strong>Explanation:</strong> Put on your <a href=\"x\">red/blue glasses</a>.",
            url = pagina,
            hdurl = foto,
            mediaType = "image",
            copyright = null,
            credit = "<a href=\"y\">Kevin Frank</a>"
        ).toEntity()!!
        assertEquals(foto, e.url)
        assertEquals(foto, e.hdurl)
        assertEquals("Put on your red/blue glasses.", e.explanation)
        assertEquals("Kevin Frank", e.copyright)
    }

    @Test
    fun camposNulosNoRompenLaConversion() {
        val e = ApodDto("2026-11-14", null, null, null, null, null, null).toEntity()!!
        assertEquals("Sin título", e.title)
        assertEquals("image", e.mediaType)
        assertNull(ApodDto(null, "x", "y", null, null, null, null).toEntity())
    }
}
