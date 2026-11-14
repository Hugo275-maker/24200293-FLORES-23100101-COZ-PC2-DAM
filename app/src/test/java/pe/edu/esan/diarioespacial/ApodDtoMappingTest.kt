package pe.edu.esan.diarioespacial

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import pe.edu.esan.diarioespacial.data.remote.dto.ApodDto

class ApodDtoMappingTest {

    @Test
    fun `dado DTO con hdurl valida cuando toEntity entonces prioriza hdurl sobre url de pagina HTML`() {
        val dto = ApodDto(
            date = "2026-11-14",
            title = "Apollo 12 and Surveyor 3",
            explanation = "<strong>Explanation:</strong> Stereo image",
            url = "https://science.nasa.gov/image-article/apod-2026-november-14-apollo-12-and-surveyor-3/",
            hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/KF-ApAn48-7133-4.jpg",
            mediaType = "image",
            copyright = "Kevin Frank"
        )

        val entity = dto.toEntity(isFavorite = false)

        assertNotNull(entity)
        assertEquals("https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/KF-ApAn48-7133-4.jpg", entity?.url)
        assertEquals("https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/KF-ApAn48-7133-4.jpg", entity?.hdurl)
        assertEquals("Stereo image", entity?.explanation)
        assertEquals("Kevin Frank", entity?.copyright)
    }

    @Test
    fun `dado DTO con campos nulos cuando toEntity entonces asigna valores por defecto seguros`() {
        val dto = ApodDto(
            date = "2026-11-13",
            title = null,
            explanation = null,
            url = null,
            hdurl = null,
            mediaType = null,
            copyright = null
        )

        val entity = dto.toEntity(isFavorite = false)

        assertNotNull(entity)
        assertEquals("Sin título", entity?.title)
        assertEquals("Sin descripción disponible.", entity?.explanation)
        assertEquals("image", entity?.mediaType)
        assertNull(entity?.copyright)
        assertNull(entity?.url)
    }

    @Test
    fun `dado DTO sin fecha cuando toEntity entonces retorna null`() {
        val dto = ApodDto(
            date = "",
            title = "Test",
            explanation = "Test",
            url = null,
            hdurl = null,
            mediaType = "image",
            copyright = null
        )

        val entity = dto.toEntity()
        assertNull(entity)
    }

    @Test
    fun `dado DTO con etiquetas HTML complejas cuando toEntity entonces limpia las etiquetas`() {
        val dto = ApodDto(
            date = "2026-11-12",
            title = "Galaxia <i>NGC 4449</i>",
            explanation = "<b>Explicación:</b> Imagen de <a href=\"https://nasa.gov\">NASA</a> &amp; ESA",
            url = "https://assets.science.nasa.gov/sample.jpg",
            hdurl = null,
            mediaType = "image",
            copyright = "<a href=\"https://nasa.gov\">Alan Bean</a>"
        )

        val entity = dto.toEntity()

        assertEquals("Galaxia NGC 4449", entity?.title)
        assertEquals("Imagen de NASA & ESA", entity?.explanation)
        assertEquals("Alan Bean", entity?.copyright)
    }
}
