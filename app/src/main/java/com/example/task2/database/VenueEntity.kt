package com.example.task2.database

data class VenueEntity(
    val id: String,
    val name: String,
    val address: String,
    val category: String,
    val distance: String,
    val checkinsCount: Int
)