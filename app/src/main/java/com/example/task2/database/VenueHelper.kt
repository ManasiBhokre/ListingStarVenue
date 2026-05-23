package com.example.task2.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class VenueHelper(private val database: VenueDatabase) {

    // This will emit new values whenever the database changes
    fun getAllSavedVenues(): Flow<List<VenueEntity>> = flow {
        while (true) {
            val venues = mutableListOf<VenueEntity>()
            val db = database.readableDatabase
            val cursor = db.query(
                "saved_venues",
                null,
                null,
                null,
                null,
                null,
                "name ASC"
            )

            cursor.use {
                while (it.moveToNext()) {
                    venues.add(
                        VenueEntity(
                            id = it.getString(it.getColumnIndexOrThrow("id")),
                            name = it.getString(it.getColumnIndexOrThrow("name")),
                            address = it.getString(it.getColumnIndexOrThrow("address")),
                            category = it.getString(it.getColumnIndexOrThrow("category")),
                            distance = it.getString(it.getColumnIndexOrThrow("distance")),
                            checkinsCount = it.getInt(it.getColumnIndexOrThrow("checkins_count"))
                        )
                    )
                }
            }
            emit(venues)
            kotlinx.coroutines.delay(500)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getAllSavedIds(): List<String> = withContext(Dispatchers.IO) {
        val ids = mutableListOf<String>()
        val db = database.readableDatabase
        val cursor = db.query(
            "saved_venues",
            arrayOf("id"),
            null,
            null,
            null,
            null,
            null
        )

        cursor.use {
            while (it.moveToNext()) {
                ids.add(it.getString(it.getColumnIndexOrThrow("id")))
            }
        }
        ids
    }

    suspend fun insertVenue(venue: VenueEntity) = withContext(Dispatchers.IO) {
        val db = database.writableDatabase
        val values = ContentValues().apply {
            put("id", venue.id)
            put("name", venue.name)
            put("address", venue.address)
            put("category", venue.category)
            put("distance", venue.distance)
            put("checkins_count", venue.checkinsCount)
        }
        db.insertWithOnConflict("saved_venues", null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    suspend fun deleteVenueById(venueId: String) = withContext(Dispatchers.IO) {
        val db = database.writableDatabase
        db.delete("saved_venues", "id = ?", arrayOf(venueId))
    }

    suspend fun isVenueSaved(venueId: String): Boolean = withContext(Dispatchers.IO) {
        val db = database.readableDatabase
        val cursor = db.query(
            "saved_venues",
            arrayOf("id"),
            "id = ?",
            arrayOf(venueId),
            null,
            null,
            null
        )
        val exists = cursor.count > 0
        cursor.close()
        exists
    }
}