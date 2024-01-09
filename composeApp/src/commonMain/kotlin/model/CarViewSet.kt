package model

import kotlinx.serialization.Serializable

@Serializable
data class CarViewSet(
    val brand: String,
    val carId: Int,
    val condition: String,
    val description: String,
    val model: String,
    val price: Int,
    val sellerID: Int,
    val stock: Int,
    val vin: String,
    val year: String
)