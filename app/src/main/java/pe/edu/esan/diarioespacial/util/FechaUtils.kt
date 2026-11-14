package pe.edu.esan.diarioespacial.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
}
