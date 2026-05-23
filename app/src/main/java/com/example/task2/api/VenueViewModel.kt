package com.example.task2.api

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.task2.database.VenueDatabase
import com.example.task2.database.VenueHelper
import com.example.task2.model.Venue
import kotlinx.coroutines.launch

sealed class UiState {
    object Loading : UiState()
    data class Success(val venues: List<Venue>) : UiState()
    data class Error(val message: String) : UiState()
}

class VenueViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VenueRepository

    private val _allVenuesState = MutableLiveData<UiState>()
    val allVenuesState: LiveData<UiState> = _allVenuesState  // This is already public

    val savedVenues: LiveData<List<Venue>>

    init {
        val database = VenueDatabase.getInstance(application)
        val dao = VenueHelper(database)
        repository = VenueRepository(RetrofitClient.apiService, dao)
        savedVenues = repository.getSavedVenues().asLiveData()
        loadVenues()
    }

    fun loadVenues() {
        viewModelScope.launch {
            _allVenuesState.value = UiState.Loading
            val result = repository.fetchVenues()
            result.fold(
                onSuccess = { venues ->
                    _allVenuesState.value = UiState.Success(venues)
                },
                onFailure = { error ->
                    _allVenuesState.value = UiState.Error(
                        error.message ?: "Unknown error occurred"
                    )
                }
            )
        }
    }

    fun toggleSave(venue: Venue) {
        viewModelScope.launch {
            if (venue.isSaved) {
                repository.deleteVenue(venue.id)
            } else {
                repository.saveVenue(venue)
            }
            refreshSavedStates()
        }
    }

    private fun refreshSavedStates() {
        viewModelScope.launch {
            val currentState = _allVenuesState.value
            if (currentState is UiState.Success) {
                val savedIds = repository.getSavedIds()
                val updated = currentState.venues.map { venue ->
                    venue.copy(isSaved = savedIds.contains(venue.id))
                }
                _allVenuesState.value = UiState.Success(updated)
            }
        }
    }

    fun deleteSavedVenue(venue: Venue) {
        viewModelScope.launch {
            repository.deleteVenue(venue.id)
            refreshSavedStates()
        }
    }
}