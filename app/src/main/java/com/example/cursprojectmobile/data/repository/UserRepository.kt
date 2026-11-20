package com.example.cursprojectmobile.data.repository

import com.example.cursprojectmobile.data.db.UserDao
import com.example.cursprojectmobile.data.model.UserProfile

class UserRepository(private val userDao: UserDao) {

    fun getUserProfile(): UserProfile = userDao.getUserProfile()

    fun updateUserProfile(profile: UserProfile): Boolean = userDao.updateUserProfile(profile)
}
