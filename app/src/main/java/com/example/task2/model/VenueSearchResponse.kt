package com.example.task2.model


import com.google.gson.annotations.SerializedName

data class VenueSearchResponse(
    @SerializedName("meta") val meta: Meta,
    @SerializedName("response") val response: Response
)

data class Meta(
    @SerializedName("code") val code: Int,
    @SerializedName("requestId") val requestId: String
)

data class Response(
    @SerializedName("venues") val venues: List<VenueDto>
)

data class VenueDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("location") val location: LocationDto?,
    @SerializedName("categories") val categories: List<CategoryDto>?,
    @SerializedName("contact") val contact: ContactDto?,
    @SerializedName("stats") val stats: StatsDto?,
    @SerializedName("url") val url: String?
)

data class LocationDto(
    @SerializedName("address") val address: String?,
    @SerializedName("crossStreet") val crossStreet: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lng") val lng: Double?,
    @SerializedName("distance") val distance: Int?,
    @SerializedName("formattedAddress") val formattedAddress: List<String>?
)

data class CategoryDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("shortName") val shortName: String,
    @SerializedName("icon") val icon: IconDto?
)

data class IconDto(
    @SerializedName("prefix") val prefix: String,
    @SerializedName("suffix") val suffix: String
)

data class ContactDto(
    @SerializedName("phone") val phone: String?,
    @SerializedName("formattedPhone") val formattedPhone: String?,
    @SerializedName("twitter") val twitter: String?
)

data class StatsDto(
    @SerializedName("tipCount") val tipCount: Int,
    @SerializedName("usersCount") val usersCount: Int,
    @SerializedName("checkinsCount") val checkinsCount: Int
)

