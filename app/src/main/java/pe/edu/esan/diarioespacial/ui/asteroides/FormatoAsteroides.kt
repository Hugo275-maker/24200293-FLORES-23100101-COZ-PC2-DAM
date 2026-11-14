package pe.edu.esan.diarioespacial.ui.asteroides

import pe.edu.esan.diarioespacial.util.FechaUtils
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/** Formato de los datos de asteroides para mostrarlos en pantalla. */
object FormatoAsteroides {

    const val NO_DISPONIBLE = "No disponible"

    // Punto decimal y coma de miles (como en Perú): 65,260.55 km/h
    private val simbolos = DecimalFormatSymbols(Locale.US)

    /** Número redondeado a máximo dos decimales y con su unidad: "470.71 m". */
    fun numero(valor: Double?, unidad: String): String {
        if (valor == null || valor.isNaN() || valor.isInfinite()) return NO_DISPONIBLE
        val formato = DecimalFormat("#,##0.##", simbolos).apply { roundingMode = RoundingMode.HALF_UP }
        return "${formato.format(valor)} $unidad"
    }

    fun metros(valor: Double?) = numero(valor, "m")
    fun kmh(valor: Double?) = numero(valor, "km/h")
    fun km(valor: Double?) = numero(valor, "km")

    /** "2026-11-14" -> "14 de noviembre de 2026". Si no se puede leer, se muestra tal cual. */
    fun fecha(iso: String?): String = FechaUtils.legible(iso)
}
