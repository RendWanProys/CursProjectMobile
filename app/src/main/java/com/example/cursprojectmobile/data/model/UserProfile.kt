package com.example.cursprojectmobile.data.model

data class UserProfile(
    val id: Long = 1,
    val name: String = "Joyce Johnson",
    val email: String = "ekamcy@mail.com",
    val phone: String = "+234 813 0400 445",
    val occupation: String = "Manager",
    val employer: String = "Food Couriers",
    val country: String = "Nigeria",
    val isNotificationEnabled: Boolean = true
)
