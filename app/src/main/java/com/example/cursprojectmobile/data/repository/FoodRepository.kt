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
        if (category.equals("All", ignoreCase = true)) {
            val all = foodDao.getAllFoods()
            val burgers = all.filter { it.category.equals("Burger", ignoreCase = true) }
            val pizzas = all.filter { it.category.equals("Pizza", ignoreCase = true) }
            val sandwiches = all.filter { it.category.equals("Sandwich", ignoreCase = true) }
            val mixed = mutableListOf<FoodItem>()
            val maxLen = maxOf(burgers.size, pizzas.size, sandwiches.size)
            for (i in 0 until maxLen) {
                if (i < burgers.size) mixed.add(burgers[i])
                if (i < pizzas.size) mixed.add(pizzas[i])
                if (i < sandwiches.size) mixed.add(sandwiches[i])
            }
            return if (mixed.isNotEmpty()) mixed else all
        }
        return foodDao.getFoodsByCategory(category)
    }

    fun getItemsByCategory(category: String): List<FoodItem> = getFoodsByCategory(category)

    fun searchFoods(query: String): List<FoodItem> = foodDao.searchFoods(query)

    fun searchItems(query: String): List<FoodItem> = searchFoods(query)

    fun getFoodById(id: Long): FoodItem? = foodDao.getFoodById(id)

    fun getItemById(id: Long): FoodItem? = getFoodById(id)
}
