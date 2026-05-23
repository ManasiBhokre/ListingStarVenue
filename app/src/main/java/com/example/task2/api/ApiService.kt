package com.example.task2.api

import com.example.task2.model.VenueSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v2/venues/search")
    suspend fun searchVenues(
        @Query("ll") latLng: String = "40.7484,-73.9857",
        @Query("oauth_token") oauthToken: String = "NPKYZ3WZ1VYMNAZ2FLX1WLECAWSMUVOQZOIDBN53F3LVZBPQ",
        @Query("v") version: String = "20180616"
    ): Response<VenueSearchResponse>
}