package pe.edu.esan.diarioespacial.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Utilidades de fechas compartidas por ambas opciones (formato AAAA-MM-DD). */
object FechaUtils {

    private val FORMATO: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    /**
     * Rango de los últimos 7 días: desde hace 6 días hasta hoy.
     * Devuelve (startDate, endDate) listos para mandar a la API.
     * Sirve para APOD y para NeoWs (cuyo rango máximo es justamente 7 días).
     */
    fun ultimos7Dias(hoy: LocalDate = LocalDate.now()): Pair<String, String> =
        hoy.minusDays(6).format(FORMATO) to hoy.format(FORMATO)

    private val FORMATO_LEGIBLE: DateTimeFormatter =
        DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", Locale("es", "PE"))

    /** "2026-11-14" -> "14 de noviembre de 2026". Si no se puede leer, se devuelve tal cual. */
    fun legible(iso: String?): String {
        if (iso.isNullOrBlank()) return "Sin fecha"
        return runCatching { LocalDate.parse(iso).format(FORMATO_LEGIBLE) }.getOrDefault(iso)
    }
}
