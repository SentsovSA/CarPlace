package model

import kotlinx.serialization.Serializable

@Serializable
data class PartImage(
    val file: String,
    val fileName: String,
    val imageID: Int,
    val imageURL: String,
    val partID: List<Int>
)