package com.udacity.asteroidradar

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Asteroid(val id: Long,
                    @Json(name = "name") val codename: String,
                    @Json(name = "close_approach_data") val closeApproachDate: String,
                    @Json(name = "absolute_magnitude_h") val absoluteMagnitude: Double,
                    @Json(name = "estimated_diameter/kilometers/estimated_diameter_max") val estimatedDiameter: Double,
                    @Json(name = "close_approach_data/relative_velocity/kilometers_per_second") val relativeVelocity: Double,
                    @Json(name = "close_approach_data/miss_distance/astronomical") val distanceFromEarth: Double,
                    @Json(name = "is_potentially_hazardous_asteroid") val isPotentiallyHazardous: Boolean
                    ) : Parcelable