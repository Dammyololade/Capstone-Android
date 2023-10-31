package com.example.capstone_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.room.Room
import com.example.capstone_android.database.AppDatabase
import com.example.capstone_android.database.PreferencesManager
import com.example.capstone_android.ui.Home
import com.example.capstone_android.ui.HomeScreen
import com.example.capstone_android.ui.Login
import com.example.capstone_android.ui.MenuViewModel
import com.example.capstone_android.ui.Profile
import com.example.capstone_android.ui.ProfileScreen
import com.example.capstone_android.ui.onboarding.LoginScreen
import com.example.capstone_android.ui.theme.CapstoneAndroidTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val database by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database").build()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        val menuViewModel = MenuViewModel(database)
        super.onCreate(savedInstanceState)
        setContent {
            CapstoneAndroidTheme {
                val isLoggedIn = PreferencesManager(this).isLoggedIn()
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = if (isLoggedIn) Home.route else Login.route
                ) {
                    composable(Login.route) {
                        LoginScreen(navController)
                    }
                    composable(Home.route) {
                        HomeScreen(database, navController = navController)
                    }
                    composable(Profile.route) {
                        ProfileScreen(navController = navController)
                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            if (database.menuItemDao().isEmpty()) {
                val menuItemsNetwork = menuViewModel.fetchMenu()
                menuViewModel.saveMenuToDatabase(menuItemsNetwork)
            }
        }
    }
}