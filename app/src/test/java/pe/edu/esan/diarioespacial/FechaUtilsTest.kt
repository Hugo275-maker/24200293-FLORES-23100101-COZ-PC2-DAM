package pe.edu.esan.diarioespacial

import org.junit.Assert.assertEquals
import org.junit.Test
import pe.edu.esan.diarioespacial.util.FechaUtils
import java.time.LocalDate

class FechaUtilsTest {

    @Test
    fun `ultimos7Dias calcula exactamente el rango desde hace 6 dias hasta hoy`() {
        val hoyFijo = LocalDate.of(2026, 11, 14)
        val (startDate, endDate) = FechaUtils.ultimos7Dias(hoyFijo)

        assertEquals("2026-11-08", startDate)
        assertEquals("2026-11-14", endDate)
    }

    @Test
    fun `ultimos7Dias genera fechas con formato ISO AAAA-MM-DD`() {
        val hoyFijo = LocalDate.of(2026, 1, 5)
        val (startDate, endDate) = FechaUtils.ultimos7Dias(hoyFijo)

        assertEquals("2025-12-30", startDate)
        assertEquals("2026-01-05", endDate)
    }
}
