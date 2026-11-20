package com.example.cursprojectmobile.data.db

import android.content.ContentValues
import android.database.Cursor
import com.example.cursprojectmobile.data.model.FoodItem

class FoodDao(private val dbHelper: DatabaseHelper) {

    fun getAllFoods(): List<FoodItem> {
        val list = mutableListOf<FoodItem>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_FOODS,
            null, null, null, null, null,
            "${DatabaseHelper.COL_FOOD_ID} ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(cursorToFoodItem(it))
            }
        }
        return list
    }

    fun getPopularFoods(): List<FoodItem> {
        val list = mutableListOf<FoodItem>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_FOODS,
            null,
            "${DatabaseHelper.COL_FOOD_IS_POPULAR} = 1",
            null, null, null,
            "${DatabaseHelper.COL_FOOD_RATING} DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(cursorToFoodItem(it))
            }
        }
        return list
    }

    fun getFoodsByCategory(category: String): List<FoodItem> {
        val list = mutableListOf<FoodItem>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_FOODS,
            null,
            "${DatabaseHelper.COL_FOOD_CATEGORY} = ?",
            arrayOf(category),
            null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(cursorToFoodItem(it))
            }
        }
        return list
    }

    fun searchFoods(query: String): List<FoodItem> {
        val list = mutableListOf<FoodItem>()
        val db = dbHelper.readableDatabase
        val q = "%$query%"
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_FOODS,
            null,
            "${DatabaseHelper.COL_FOOD_TITLE} LIKE ? OR ${DatabaseHelper.COL_FOOD_DESCRIPTION} LIKE ? OR ${DatabaseHelper.COL_FOOD_CATEGORY} LIKE ?",
            arrayOf(q, q, q),
            null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(cursorToFoodItem(it))
            }
        }
        return list
    }

    fun getFoodById(id: Long): FoodItem? {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_FOODS,
            null,
            "${DatabaseHelper.COL_FOOD_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        cursor.use {
            if (it.moveToFirst()) {
                return cursorToFoodItem(it)
            }
        }
        return null
    }

    fun insertFood(item: FoodItem): Long {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put(DatabaseHelper.COL_FOOD_TITLE, item.title)
            put(DatabaseHelper.COL_FOOD_SUBTITLE, item.subtitle)
            put(DatabaseHelper.COL_FOOD_DESCRIPTION, item.description)
            put(DatabaseHelper.COL_FOOD_PRICE, item.price)
            put(DatabaseHelper.COL_FOOD_RATING, item.rating)
            put(DatabaseHelper.COL_FOOD_ORDER_COUNT, item.orderCount)
            put(DatabaseHelper.COL_FOOD_CATEGORY, item.category)
            put(DatabaseHelper.COL_FOOD_IMAGE_NAME, item.imageResName)
            put(DatabaseHelper.COL_FOOD_IS_POPULAR, if (item.isPopular) 1 else 0)
        }
        return db.insert(DatabaseHelper.TABLE_FOODS, null, cv)
    }

    private fun cursorToFoodItem(c: Cursor): FoodItem {
        return FoodItem(
            id = c.getLong(c.getColumnIndexOrThrow(DatabaseHelper.COL_FOOD_ID)),
            title = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_FOOD_TITLE)),
            subtitle = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_FOOD_SUBTITLE)) ?: "",
            description = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_FOOD_DESCRIPTION)) ?: "",
            price = c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COL_FOOD_PRICE)),
            rating = c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COL_FOOD_RATING)),
            orderCount = c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COL_FOOD_ORDER_COUNT)),
            category = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_FOOD_CATEGORY)),
            imageResName = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_FOOD_IMAGE_NAME)) ?: "",
            isPopular = c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COL_FOOD_IS_POPULAR)) == 1
        )
    }
}
