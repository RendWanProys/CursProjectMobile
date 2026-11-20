package com.example.cursprojectmobile.data.db

import android.content.ContentValues
import android.database.Cursor
import com.example.cursprojectmobile.data.model.CartItem
import com.example.cursprojectmobile.data.model.FoodItem

class CartDao(private val dbHelper: DatabaseHelper) {

    fun getCartItems(): List<CartItem> {
        val list = mutableListOf<CartItem>()
        val db = dbHelper.readableDatabase

        val query = """
            SELECT 
                c.${DatabaseHelper.COL_CART_ID} AS cart_id,
                c.${DatabaseHelper.COL_CART_QUANTITY} AS cart_qty,
                c.${DatabaseHelper.COL_CART_ADDED_AT} AS cart_added_at,
                f.${DatabaseHelper.COL_FOOD_ID} AS f_id,
                f.${DatabaseHelper.COL_FOOD_TITLE} AS f_title,
                f.${DatabaseHelper.COL_FOOD_SUBTITLE} AS f_subtitle,
                f.${DatabaseHelper.COL_FOOD_DESCRIPTION} AS f_description,
                f.${DatabaseHelper.COL_FOOD_PRICE} AS f_price,
                f.${DatabaseHelper.COL_FOOD_RATING} AS f_rating,
                f.${DatabaseHelper.COL_FOOD_ORDER_COUNT} AS f_orders,
                f.${DatabaseHelper.COL_FOOD_CATEGORY} AS f_category,
                f.${DatabaseHelper.COL_FOOD_IMAGE_NAME} AS f_image,
                f.${DatabaseHelper.COL_FOOD_IS_POPULAR} AS f_popular
            FROM ${DatabaseHelper.TABLE_CART} c
            JOIN ${DatabaseHelper.TABLE_FOODS} f ON c.${DatabaseHelper.COL_CART_FOOD_ID} = f.${DatabaseHelper.COL_FOOD_ID}
            ORDER BY c.${DatabaseHelper.COL_CART_ID} ASC
        """.trimIndent()

        val cursor = db.rawQuery(query, null)
        cursor.use {
            while (it.moveToNext()) {
                val foodItem = FoodItem(
                    id = it.getLong(it.getColumnIndexOrThrow("f_id")),
                    title = it.getString(it.getColumnIndexOrThrow("f_title")),
                    subtitle = it.getString(it.getColumnIndexOrThrow("f_subtitle")) ?: "",
                    description = it.getString(it.getColumnIndexOrThrow("f_description")) ?: "",
                    price = it.getDouble(it.getColumnIndexOrThrow("f_price")),
                    rating = it.getDouble(it.getColumnIndexOrThrow("f_rating")),
                    orderCount = it.getInt(it.getColumnIndexOrThrow("f_orders")),
                    category = it.getString(it.getColumnIndexOrThrow("f_category")),
                    imageResName = it.getString(it.getColumnIndexOrThrow("f_image")) ?: "",
                    isPopular = it.getInt(it.getColumnIndexOrThrow("f_popular")) == 1
                )
                val cartItem = CartItem(
                    id = it.getLong(it.getColumnIndexOrThrow("cart_id")),
                    foodItem = foodItem,
                    quantity = it.getInt(it.getColumnIndexOrThrow("cart_qty")),
                    addedAt = it.getLong(it.getColumnIndexOrThrow("cart_added_at"))
                )
                list.add(cartItem)
            }
        }
        return list
    }

    fun addToCart(foodId: Long, quantity: Int = 1): Boolean {
        val db = dbHelper.writableDatabase
        // Check if item already exists in cart
        val cursor = db.query(
            DatabaseHelper.TABLE_CART,
            arrayOf(DatabaseHelper.COL_CART_ID, DatabaseHelper.COL_CART_QUANTITY),
            "${DatabaseHelper.COL_CART_FOOD_ID} = ?",
            arrayOf(foodId.toString()),
            null, null, null
        )

        var existingId: Long? = null
        var currentQty = 0

        cursor.use {
            if (it.moveToFirst()) {
                existingId = it.getLong(it.getColumnIndexOrThrow(DatabaseHelper.COL_CART_ID))
                currentQty = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.COL_CART_QUANTITY))
            }
        }

        return if (existingId != null) {
            val cv = ContentValues().apply {
                put(DatabaseHelper.COL_CART_QUANTITY, currentQty + quantity)
            }
            db.update(
                DatabaseHelper.TABLE_CART,
                cv,
                "${DatabaseHelper.COL_CART_ID} = ?",
                arrayOf(existingId.toString())
            ) > 0
        } else {
            val cv = ContentValues().apply {
                put(DatabaseHelper.COL_CART_FOOD_ID, foodId)
                put(DatabaseHelper.COL_CART_QUANTITY, quantity)
                put(DatabaseHelper.COL_CART_ADDED_AT, System.currentTimeMillis())
            }
            db.insert(DatabaseHelper.TABLE_CART, null, cv) > 0
        }
    }

    fun updateQuantity(cartId: Long, newQuantity: Int): Boolean {
        val db = dbHelper.writableDatabase
        return if (newQuantity <= 0) {
            db.delete(
                DatabaseHelper.TABLE_CART,
                "${DatabaseHelper.COL_CART_ID} = ?",
                arrayOf(cartId.toString())
            ) > 0
        } else {
            val cv = ContentValues().apply {
                put(DatabaseHelper.COL_CART_QUANTITY, newQuantity)
            }
            db.update(
                DatabaseHelper.TABLE_CART,
                cv,
                "${DatabaseHelper.COL_CART_ID} = ?",
                arrayOf(cartId.toString())
            ) > 0
        }
    }

    fun removeFromCart(cartId: Long): Boolean {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseHelper.TABLE_CART,
            "${DatabaseHelper.COL_CART_ID} = ?",
            arrayOf(cartId.toString())
        ) > 0
    }

    fun clearCart(): Int {
        val db = dbHelper.writableDatabase
        return db.delete(DatabaseHelper.TABLE_CART, null, null)
    }

    fun getCartTotalCount(): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM(${DatabaseHelper.COL_CART_QUANTITY}) FROM ${DatabaseHelper.TABLE_CART}",
            null
        )
        cursor.use {
            if (it.moveToFirst() && !it.isNull(0)) {
                return it.getInt(0)
            }
        }
        return 0
    }
}
