package com.example.cursprojectmobile.data.repository

import com.example.cursprojectmobile.data.db.FoodDao
import com.example.cursprojectmobile.data.model.Category
import com.example.cursprojectmobile.data.model.FoodItem

class FoodRepository(private val foodDao: FoodDao) {

    fun getCategories(): List<Category> {
        return listOf(
            Category("all", "All", "🍽️"),
            Category("Burger", "Burger", "🍔"),
            Category("Pizza", "Pizza", "🍕"),
            Category("Sandwich", "Sandwich", "🥪")
        )
    }

    fun getAllFoods(): List<FoodItem> = foodDao.getAllFoods()

    fun getPopularFoods(): List<FoodItem> = foodDao.getPopularFoods()

    fun getFoodsByCategory(category: String): List<FoodItem> {
        return if (category == "All" || category == "all") {
            foodDao.getAllFoods()
        } else {
            foodDao.getFoodsByCategory(category)
        }
    }

    fun searchFoods(query: String): List<FoodItem> = foodDao.searchFoods(query)

    fun getFoodById(id: Long): FoodItem? = foodDao.getFoodById(id)
}
