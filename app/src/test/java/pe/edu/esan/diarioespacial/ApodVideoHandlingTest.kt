package pe.edu.esan.diarioespacial

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.edu.esan.diarioespacial.data.remote.dto.ApodDto

/**
 * Pruebas unitarias para validar el manejo de contenidos APOD de tipo video.
 */
class ApodVideoHandlingTest {

    @Test
    fun `dado un DTO de tipo video cuando se convierte a entidad entonces conserva mediaType video y campos obligatorios`() {
        val dtoVideo = ApodDto(
            date = "2026-11-14",
            title = "Supernova Remnant Video",
            explanation = "Video de simulación de explosión de supernova",
            url = "https://www.youtube.com/embed/sample_video",
            hdurl = null,
            mediaType = "video",
            copyright = "NASA / ESA"
        )

        val entity = dtoVideo.toEntity(isFavorite = true)

        assertNotNull(entity)
        assertEquals("2026-11-14", entity?.date)
        assertEquals("Supernova Remnant Video", entity?.title)
        assertEquals("video", entity?.mediaType)
        assertEquals("Video de simulación de explosión de supernova", entity?.explanation)
        assertEquals("NASA / ESA", entity?.copyright)
        assertTrue(entity?.isFavorite == true)
    }

    @Test
    fun `dado un registro de video sin hdurl cuando toEntity se ejecuta no falla y mantiene mediaType video`() {
        val dtoVideo = ApodDto(
            date = "2026-11-10",
            title = "Video del Sol en 4K",
            explanation = "<strong>Explanation:</strong> Time-lapse del Sol",
            url = "https://youtube.com/watch?v=12345",
            hdurl = null,
            mediaType = "video",
            copyright = null
        )

        val entity = dtoVideo.toEntity(isFavorite = false)

        assertNotNull(entity)
        assertEquals("video", entity?.mediaType)
        assertEquals("Time-lapse del Sol", entity?.explanation)
        assertEquals("Video del Sol en 4K", entity?.title)
    }
}
