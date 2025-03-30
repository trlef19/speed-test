package com.letr.speedtest

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
data class NavItem(val label: String, val icon: ImageVector)
sealed class BottomNavigationItems(val route: String, val icon: Int, val label: String) {
    object Home : BottomNavigationItems("home", R.drawable.wifi, "Home")
    object Search : BottomNavigationItems("search", R.drawable.speed, "Search")
    object Profile : BottomNavigationItems("profile", R.drawable.person, "Profile")
    object Settings : BottomNavigationItems("settings", R.drawable.settings, "Settings")
}