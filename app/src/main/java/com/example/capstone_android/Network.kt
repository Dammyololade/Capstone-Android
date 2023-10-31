package com.example.capstone_android

import com.example.capstone_android.database.MenuItemRoom
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuNetwork(
    val menu: List<MenuItemNetwork>,
)

@Serializable
data class MenuItemNetwork(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("price")
    val price: String,
    @SerialName("image")
    val image: String,
    @SerialName("description")
    val description: String,
    @SerialName("category")
    val category: String,
) {
    fun toMenuItemRoom() = MenuItemRoom(
      id, title,  price.toDouble(), image, description, category
    )
}
