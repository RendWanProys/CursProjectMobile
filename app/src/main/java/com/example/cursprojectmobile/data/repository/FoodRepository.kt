package com.example.cursprojectmobile.data.repository

import com.example.cursprojectmobile.FoodCouriersApp
import com.example.cursprojectmobile.data.db.DatabaseHelper
import com.example.cursprojectmobile.data.db.FoodDao
import com.example.cursprojectmobile.data.model.Category
import com.example.cursprojectmobile.data.model.FoodItem

class FoodRepository(
    private val foodDao: FoodDao = FoodDao(DatabaseHelper(FoodCouriersApp.instance))
) {

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

    fun getPopularItems(): List<FoodItem> = getPopularFoods()

    fun getFoodsByCategory(category: String): List<FoodItem> {
        return if (category.equals("All", ignoreCase = true)) {
            foodDao.getAllFoods()
        } else {
            foodDao.getFoodsByCategory(category)
        }
    }

    fun getItemsByCategory(category: String): List<FoodItem> = getFoodsByCategory(category)

    fun searchFoods(query: String): List<FoodItem> = foodDao.searchFoods(query)

    fun searchItems(query: String): List<FoodItem> = searchFoods(query)

    fun getFoodById(id: Long): FoodItem? = foodDao.getFoodById(id)

    fun getItemById(id: Long): FoodItem? = getFoodById(id)
}
