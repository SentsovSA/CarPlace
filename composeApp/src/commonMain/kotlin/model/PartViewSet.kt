package model

import kotlinx.serialization.Serializable

@Serializable
data class PartViewSet(
    val condition: String,
    val description: String,
    val partID: Int,
    val partName: String,
    val partPrice: Int,
    val sellerID: List<Int>,
    val stock: Int,
    val partImageID: List<Int>
)