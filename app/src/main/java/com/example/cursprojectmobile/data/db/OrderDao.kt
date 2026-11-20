package com.example.cursprojectmobile.data.db

import android.content.ContentValues
import android.database.Cursor
import com.example.cursprojectmobile.data.model.Order

class OrderDao(private val dbHelper: DatabaseHelper) {

    fun createOrder(order: Order): Long {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put(DatabaseHelper.COL_ORDER_DATE, order.date)
            put(DatabaseHelper.COL_ORDER_SUBTOTAL, order.subtotal)
            put(DatabaseHelper.COL_ORDER_DELIVERY_FEE, order.deliveryFee)
            put(DatabaseHelper.COL_ORDER_DISCOUNT, order.discount)
            put(DatabaseHelper.COL_ORDER_TOTAL, order.totalAmount)
            put(DatabaseHelper.COL_ORDER_ADDRESS, order.address)
            put(DatabaseHelper.COL_ORDER_STATUS, order.status)
            put(DatabaseHelper.COL_ORDER_RATING, order.rating)
            put(DatabaseHelper.COL_ORDER_FEEDBACK, order.feedback)
        }
        return db.insert(DatabaseHelper.TABLE_ORDERS, null, cv)
    }

    fun getAllOrders(): List<Order> {
        val list = mutableListOf<Order>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_ORDERS,
            null, null, null, null, null,
            "${DatabaseHelper.COL_ORDER_ID} DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(cursorToOrder(it))
            }
        }
        return list
    }

    fun getOrderById(id: Long): Order? {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_ORDERS,
            null,
            "${DatabaseHelper.COL_ORDER_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        cursor.use {
            if (it.moveToFirst()) {
                return cursorToOrder(it)
            }
        }
        return null
    }

    fun updateOrderRating(orderId: Long, rating: Int, feedback: String): Boolean {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put(DatabaseHelper.COL_ORDER_RATING, rating)
            put(DatabaseHelper.COL_ORDER_FEEDBACK, feedback)
        }
        return db.update(
            DatabaseHelper.TABLE_ORDERS,
            cv,
            "${DatabaseHelper.COL_ORDER_ID} = ?",
            arrayOf(orderId.toString())
        ) > 0
    }

    private fun cursorToOrder(c: Cursor): Order {
        return Order(
            id = c.getLong(c.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ID)),
            date = c.getLong(c.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_DATE)),
            subtotal = c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_SUBTOTAL)),
            deliveryFee = c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_DELIVERY_FEE)),
            discount = c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_DISCOUNT)),
            totalAmount = c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_TOTAL)),
            address = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ADDRESS)),
            status = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_STATUS)),
            rating = c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_RATING)),
            feedback = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_FEEDBACK)) ?: ""
        )
    }
}
