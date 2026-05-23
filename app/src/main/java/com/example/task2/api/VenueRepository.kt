package com.example.task2.api

import com.example.task2.database.VenueHelper
import com.example.task2.mapper.VenueMapper
import com.example.task2.model.Venue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VenueRepository(
    private val apiService: ApiService,
    private val venueDao: VenueHelper
) {

    suspend fun fetchVenues(): Result<List<Venue>> {
        return try {
            val response = apiService.searchVenues()
            if (response.isSuccessful) {
                val dtos = response.body()?.response?.venues ?: emptyList()
                val savedIds = venueDao.getAllSavedIds().toSet()
                val venues = VenueMapper.fromDtoList(dtos, savedIds)
                Result.success(venues)
            } else {
                Result.failure(Exception("API error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getSavedVenues(): Flow<List<Venue>> {
        return venueDao.getAllSavedVenues().map { entities ->
            VenueMapper.fromEntityList(entities)
        }
    }

    suspend fun getSavedIds(): Set<String> {
        return venueDao.getAllSavedIds().toSet()
    }

    suspend fun saveVenue(venue: Venue) {
        venueDao.insertVenue(VenueMapper.toEntity(venue))
    }

    suspend fun deleteVenue(venueId: String) {
        venueDao.deleteVenueById(venueId)
    }

    suspend fun isVenueSaved(venueId: String): Boolean {
        return venueDao.isVenueSaved(venueId)
    }
}