package com.example.task2.mapper


import com.example.task2.database.VenueEntity
import com.example.task2.model.Venue
import com.example.task2.model.VenueDto

object VenueMapper {


    fun fromDto(dto: VenueDto, isSaved: Boolean = false): Venue {
        val address = buildAddress(dto)
        val category = dto.categories?.firstOrNull()?.name ?: "Unknown Category"
        val distance = dto.location?.distance?.let { "${it}m away" } ?: "Distance N/A"
        val checkinsCount = dto.stats?.checkinsCount ?: 0

        return Venue(
            id = dto.id,
            name = dto.name,
            address = address,
            category = category,
            distance = distance,
            checkinsCount = checkinsCount,
            isSaved = isSaved
        )
    }

    fun fromDtoList(dtoList: List<VenueDto>, savedIds: Set<String> = emptySet()): List<Venue> {
        return dtoList.map { dto -> fromDto(dto, savedIds.contains(dto.id)) }
    }

    fun fromEntity(entity: VenueEntity): Venue {
        return Venue(
            id = entity.id,
            name = entity.name,
            address = entity.address,
            category = entity.category,
            distance = entity.distance,
            checkinsCount = entity.checkinsCount,
            isSaved = true
        )
    }

    fun fromEntityList(entities: List<VenueEntity>): List<Venue> {
        return entities.map { fromEntity(it) }
    }

    fun toEntity(venue: Venue): VenueEntity {
        return VenueEntity(
            id = venue.id,
            name = venue.name,
            address = venue.address,
            category = venue.category,
            distance = venue.distance,
            checkinsCount = venue.checkinsCount
        )
    }

    private fun buildAddress(dto: VenueDto): String {
        val location = dto.location ?: return "Address unavailable"
        return when {
            !location.formattedAddress.isNullOrEmpty() ->
                location.formattedAddress.joinToString(", ")
            !location.address.isNullOrEmpty() -> {
                buildString {
                    append(location.address)
                    if (!location.crossStreet.isNullOrEmpty()) append(" (${location.crossStreet})")
                    if (!location.city.isNullOrEmpty()) append(", ${location.city}")
                    if (!location.state.isNullOrEmpty()) append(", ${location.state}")
                }
            }
            else -> "Address unavailable"
        }
    }
}