package com.example.capstone_android.ui

interface Destinations {
    val route: String
}

object Home : Destinations {
    override val route = "Home"
}

object Login : Destinations {
    override val route = "Register"
}

object Profile : Destinations {
    override val route = "Profile"
}
