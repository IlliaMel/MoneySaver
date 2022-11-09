package com.andriyilliaandroidgeeks.moneysaver.presentation._components.navigation_drawer

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val number: Int,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val hasSwitch: Boolean = false,
    var switchIsActive: Boolean = false // always false if item has NOT switch element
)

data class MenuBlock(
    val title: String,
    val items: List<MenuItem>
)