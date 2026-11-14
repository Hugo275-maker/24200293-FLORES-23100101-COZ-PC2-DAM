package pe.edu.esan.diarioespacial.data.asteroides

import pe.edu.esan.diarioespacial.data.local.asteroides.AsteroidEntity
import pe.edu.esan.diarioespacial.data.remote.asteroides.NeoDto
import pe.edu.esan.diarioespacial.data.remote.asteroides.NeoFeedResponseDto

/*
 * Conversión explícita DTO (API) -> Entity (Room) de la opción Asteroides.
 * Aquí se resuelven los datos incompletos y los números que llegan como texto.
 */

/**
 * Aplana el feed (agrupado por fecha) en una sola lista de entidades.
 * Se descartan los asteroides sin id (no se podrían guardar) y, si uno
 * aparece en varias fechas, se queda una sola vez.
 */
fun NeoFeedResponseDto.aEntidades(): List<AsteroidEntity> =
    nearEarthObjects.orEmpty()
        .flatMap { (fechaDelFeed, asteroides) ->
            asteroides.orEmpty().mapNotNull { it.aEntidad(fechaDelFeed) }
        }
        .distinctBy { it.id }

/**
 * @param fechaDelFeed clave de fecha bajo la que vino el asteroide; se usa si
 * close_approach_data no trae la fecha.
 */
fun NeoDto.aEntidad(fechaDelFeed: String?): AsteroidEntity? {
    val idValido = id?.takeIf { it.isNotBlank() } ?: return null
    // En el feed, close_approach_data trae la aproximación de esa fecha.
    val aproximacion = closeApproachData?.firstOrNull()
    val metros = estimatedDiameter?.meters

    return AsteroidEntity(
        id = idValido,
        nombre = name?.takeIf { it.isNotBlank() } ?: "Asteroide $idValido",
        fechaAproximacion = aproximacion?.closeApproachDate?.takeIf { it.isNotBlank() }
            ?: fechaDelFeed,
        epochAproximacion = aproximacion?.epochDateCloseApproach,
        diametroMinM = metros?.min,
        diametroMaxM = metros?.max,
        velocidadKmh = aproximacion?.relativeVelocity?.kilometersPerHour.aNumero(),
        distanciaKm = aproximacion?.missDistance?.kilometers.aNumero(),
        esPeligroso = isPotentiallyHazardous == true
    )
}

/** "48375.123" -> 48375.123 ; null, vacío o texto inválido -> null. */
internal fun String?.aNumero(): Double? = this?.trim()?.toDoubleOrNull()
