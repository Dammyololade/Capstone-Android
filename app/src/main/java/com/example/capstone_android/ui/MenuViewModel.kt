package com.example.capstone_android.ui

import androidx.room.Room
import com.example.capstone_android.MenuItemNetwork
import com.example.capstone_android.MenuNetwork
import com.example.capstone_android.database.AppDatabase
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json

class MenuViewModel(private val database: AppDatabase) {

    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }

    suspend fun fetchMenu(): List<MenuItemNetwork> {
        val response = httpClient.get(
            "https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json").body<MenuNetwork>()
        return response.menu
    }

    fun saveMenuToDatabase(menuItemsNetwork: List<MenuItemNetwork>) {
        val menuItemsRoom = menuItemsNetwork.map { it.toMenuItemRoom() }
        database.menuItemDao().insertAll(*menuItemsRoom.toTypedArray())
    }

}