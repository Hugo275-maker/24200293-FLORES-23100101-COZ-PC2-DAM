package pe.edu.esan.diarioespacial.data.remote.asteroides

import com.google.gson.annotations.SerializedName

/*
 * DTOs de la respuesta de NeoWs Feed (GET neo/rest/v1/feed).
 *
 * Todos los campos son anulables: la API puede devolver datos incompletos y
 * Gson deja en null lo que no llega, así la app no se cierra.
 *
 * Ojo con dos detalles de la respuesta:
 *  - near_earth_objects es un objeto cuyas claves son fechas ("2026-11-14")
 *    y cuyos valores son listas de asteroides -> se modela como Map.
 *  - la velocidad y la distancia llegan como TEXTO -> se guardan como String
 *    y se convierten a número en el mapper.
 */

data class NeoFeedResponseDto(
    @SerializedName("element_count") val elementCount: Int?,
    @SerializedName("near_earth_objects") val nearEarthObjects: Map<String, List<NeoDto>?>?
)

data class NeoDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("is_potentially_hazardous_asteroid") val isPotentiallyHazardous: Boolean?,
    @SerializedName("estimated_diameter") val estimatedDiameter: EstimatedDiameterDto?,
    @SerializedName("close_approach_data") val closeApproachData: List<CloseApproachDto>?
)

data class EstimatedDiameterDto(
    @SerializedName("meters") val meters: DiameterRangeDto?
)

data class DiameterRangeDto(
    @SerializedName("estimated_diameter_min") val min: Double?,
    @SerializedName("estimated_diameter_max") val max: Double?
)

data class CloseApproachDto(
    @SerializedName("close_approach_date") val closeApproachDate: String?,
    @SerializedName("epoch_date_close_approach") val epochDateCloseApproach: Long?,
    @SerializedName("relative_velocity") val relativeVelocity: RelativeVelocityDto?,
    @SerializedName("miss_distance") val missDistance: MissDistanceDto?
)

data class RelativeVelocityDto(
    @SerializedName("kilometers_per_hour") val kilometersPerHour: String?
)

data class MissDistanceDto(
    @SerializedName("kilometers") val kilometers: String?
)
