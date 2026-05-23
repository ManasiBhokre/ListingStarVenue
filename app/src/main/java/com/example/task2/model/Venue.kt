package com.example.task2.model

data class Venue(
    val id: String,
    val name: String,
    val address: String,
    val category: String,
    val distance: String,
    val checkinsCount: Int,
    val isSaved: Boolean = false
)