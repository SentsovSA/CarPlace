package model

import kotlinx.serialization.Serializable

@Serializable
data class CarImage(
    val carID: Int,
    val file: String,
    val fileName: String,
    val imageID: Int,
    val imageURL: String
)