package com.example.cursprojectmobile.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.cursprojectmobile.data.model.FoodItem
import com.example.cursprojectmobile.data.model.UserProfile

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "food_couriers.db"
        const val DATABASE_VERSION = 1

        // Table Foods
        const val TABLE_FOODS = "foods"
        const val COL_FOOD_ID = "id"
        const val COL_FOOD_TITLE = "title"
        const val COL_FOOD_SUBTITLE = "subtitle"
        const val COL_FOOD_DESCRIPTION = "description"
        const val COL_FOOD_PRICE = "price"
        const val COL_FOOD_RATING = "rating"
        const val COL_FOOD_ORDER_COUNT = "order_count"
        const val COL_FOOD_CATEGORY = "category"
        const val COL_FOOD_IMAGE_NAME = "image_res_name"
        const val COL_FOOD_IS_POPULAR = "is_popular"

        // Table Cart
        const val TABLE_CART = "cart"
        const val COL_CART_ID = "id"
        const val COL_CART_FOOD_ID = "food_id"
        const val COL_CART_QUANTITY = "quantity"
        const val COL_CART_ADDED_AT = "added_at"

        // Table Orders
        const val TABLE_ORDERS = "orders"
        const val COL_ORDER_ID = "id"
        const val COL_ORDER_DATE = "date"
        const val COL_ORDER_SUBTOTAL = "subtotal"
        const val COL_ORDER_DELIVERY_FEE = "delivery_fee"
        const val COL_ORDER_DISCOUNT = "discount"
        const val COL_ORDER_TOTAL = "total_amount"
        const val COL_ORDER_ADDRESS = "address"
        const val COL_ORDER_STATUS = "status"
        const val COL_ORDER_RATING = "rating"
        const val COL_ORDER_FEEDBACK = "feedback"

        // Table User Profile
        const val TABLE_USER = "user_profile"
        const val COL_USER_ID = "id"
        const val COL_USER_NAME = "name"
        const val COL_USER_EMAIL = "email"
        const val COL_USER_PHONE = "phone"
        const val COL_USER_OCCUPATION = "occupation"
        const val COL_USER_EMPLOYER = "employer"
        const val COL_USER_COUNTRY = "country"
        const val COL_USER_NOTIFICATIONS = "is_notification_enabled"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createFoodsTable = """
            CREATE TABLE $TABLE_FOODS (
                $COL_FOOD_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_FOOD_TITLE TEXT NOT NULL,
                $COL_FOOD_SUBTITLE TEXT,
                $COL_FOOD_DESCRIPTION TEXT,
                $COL_FOOD_PRICE REAL NOT NULL,
                $COL_FOOD_RATING REAL DEFAULT 4.5,
                $COL_FOOD_ORDER_COUNT INTEGER DEFAULT 1000,
                $COL_FOOD_CATEGORY TEXT NOT NULL,
                $COL_FOOD_IMAGE_NAME TEXT,
                $COL_FOOD_IS_POPULAR INTEGER DEFAULT 0
            )
        """.trimIndent()

        val createCartTable = """
            CREATE TABLE $TABLE_CART (
                $COL_CART_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CART_FOOD_ID INTEGER NOT NULL,
                $COL_CART_QUANTITY INTEGER NOT NULL DEFAULT 1,
                $COL_CART_ADDED_AT INTEGER NOT NULL,
                FOREIGN KEY($COL_CART_FOOD_ID) REFERENCES $TABLE_FOODS($COL_FOOD_ID) ON DELETE CASCADE
            )
        """.trimIndent()

        val createOrdersTable = """
            CREATE TABLE $TABLE_ORDERS (
                $COL_ORDER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ORDER_DATE INTEGER NOT NULL,
                $COL_ORDER_SUBTOTAL REAL NOT NULL,
                $COL_ORDER_DELIVERY_FEE REAL NOT NULL,
                $COL_ORDER_DISCOUNT REAL NOT NULL,
                $COL_ORDER_TOTAL REAL NOT NULL,
                $COL_ORDER_ADDRESS TEXT NOT NULL,
                $COL_ORDER_STATUS TEXT NOT NULL,
                $COL_ORDER_RATING INTEGER DEFAULT 0,
                $COL_ORDER_FEEDBACK TEXT
            )
        """.trimIndent()

        val createUserTable = """
            CREATE TABLE $TABLE_USER (
                $COL_USER_ID INTEGER PRIMARY KEY,
                $COL_USER_NAME TEXT NOT NULL,
                $COL_USER_EMAIL TEXT NOT NULL,
                $COL_USER_PHONE TEXT,
                $COL_USER_OCCUPATION TEXT,
                $COL_USER_EMPLOYER TEXT,
                $COL_USER_COUNTRY TEXT,
                $COL_USER_NOTIFICATIONS INTEGER DEFAULT 1
            )
        """.trimIndent()

        db.execSQL(createFoodsTable)
        db.execSQL(createCartTable)
        db.execSQL(createOrdersTable)
        db.execSQL(createUserTable)

        seedInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FOODS")
        onCreate(db)
    }

    private fun seedInitialData(db: SQLiteDatabase) {
        val initialFoods = listOf(
            FoodItem(
                title = "Chicken burger",
                subtitle = "100 gr chicken + tomato + cheese Lettuce",
                description = "Tender grilled chicken fillet seasoned with fragrant spices, sliced tomatoes, melted gouda and fresh crispy iceberg lettuce.",
                price = 20.0,
                rating = 3.8,
                orderCount = 5200,
                category = "Burger",
                imageResName = "food_chicken_burger",
                isPopular = true
            ),
            FoodItem(
                title = "Chese burger",
                subtitle = "100 gr meat + onion + tomato + Lettuce cheese",
                description = "Classic beef burger made with prime ground beef, caramelized onions, pickles, cheddar cheese slice and our secret homemade sauce.",
                price = 15.0,
                rating = 4.5,
                orderCount = 7400,
                category = "Burger",
                imageResName = "food_cheese_burger",
                isPopular = true
            ),
            FoodItem(
                title = "Chicken Burger Promo Pack",
                subtitle = "Special Combo Menu Pack",
                description = "In a medium bowl, add ground chicken, breadcrumbs, mayonnaise, onions, parsley, garlic, paprika, salt and pepper. Use your hands to combine all the ingredients together until blended, but don't over mix.",
                price = 20.0,
                rating = 4.8,
                orderCount = 7000,
                category = "Burger",
                imageResName = "food_burger_detail",
                isPopular = true
            ),
            FoodItem(
                title = "Pepper Pizza",
                subtitle = "5kg box of Pizza",
                description = "Hot and spicy oven-baked pizza covered with premium slices of beef pepperoni, melted mozzarella cheese, oregano and savory tomato sauce.",
                price = 15.0,
                rating = 4.7,
                orderCount = 4500,
                category = "Pizza",
                imageResName = "food_pepper_pizza",
                isPopular = true
            ),
            FoodItem(
                title = "Onion Pizza",
                subtitle = "Pizza Palace",
                description = "Crispy stone-baked crust paired with sweet caramelized onions, gorgonzola cheese, fresh rosemary and virgin olive oil.",
                price = 15.0,
                rating = 4.4,
                orderCount = 2800,
                category = "Pizza",
                imageResName = "food_onion_pizza",
                isPopular = false
            ),
            FoodItem(
                title = "Chicken Spagetti",
                subtitle = "1 Big Pack",
                description = "Al dente Italian spaghetti served with braised chicken breast slices, creamy parmesan garlic sauce, and fresh basil leaves.",
                price = 7.0,
                rating = 4.6,
                orderCount = 3800,
                category = "Sandwich",
                imageResName = "food_chicken_spaghetti",
                isPopular = true
            ),
            FoodItem(
                title = "Jollof Rice",
                subtitle = "1 Combo pack",
                description = "West African authentic party Jollof rice cooked in rich tomato-bell pepper reduction, served with sweet plantains and roasted chicken.",
                price = 10.0,
                rating = 4.8,
                orderCount = 4200,
                category = "Sandwich",
                imageResName = "food_jollof_rice",
                isPopular = true
            ),
            FoodItem(
                title = "Fruity Pancakes",
                subtitle = "Noodle Home",
                description = "Stack of fluffy golden pancakes crowned with wild berries, powdered sugar, whipped vanilla cream and pure maple syrup.",
                price = 12.0,
                rating = 4.7,
                orderCount = 2900,
                category = "Pizza",
                imageResName = "food_fruity_pancakes",
                isPopular = false
            ),
            FoodItem(
                title = "Spicy Shawarma",
                subtitle = "Hot Cool Spot",
                description = "Finely shredded spit-roasted meat wrapped in toasted lavash flatbread with garlic toum, pickled turnips and fiery chili sauce.",
                price = 15.0,
                rating = 4.9,
                orderCount = 8100,
                category = "Sandwich",
                imageResName = "food_spicy_shawarma",
                isPopular = true
            )
        )

        for (item in initialFoods) {
            val cv = ContentValues().apply {
                put(COL_FOOD_TITLE, item.title)
                put(COL_FOOD_SUBTITLE, item.subtitle)
                put(COL_FOOD_DESCRIPTION, item.description)
                put(COL_FOOD_PRICE, item.price)
                put(COL_FOOD_RATING, item.rating)
                put(COL_FOOD_ORDER_COUNT, item.orderCount)
                put(COL_FOOD_CATEGORY, item.category)
                put(COL_FOOD_IMAGE_NAME, item.imageResName)
                put(COL_FOOD_IS_POPULAR, if (item.isPopular) 1 else 0)
            }
            db.insert(TABLE_FOODS, null, cv)
        }

        // Seed default user profile
        val defaultUser = UserProfile()
        val userCv = ContentValues().apply {
            put(COL_USER_ID, defaultUser.id)
            put(COL_USER_NAME, defaultUser.name)
            put(COL_USER_EMAIL, defaultUser.email)
            put(COL_USER_PHONE, defaultUser.phone)
            put(COL_USER_OCCUPATION, defaultUser.occupation)
            put(COL_USER_EMPLOYER, defaultUser.employer)
            put(COL_USER_COUNTRY, defaultUser.country)
            put(COL_USER_NOTIFICATIONS, if (defaultUser.isNotificationEnabled) 1 else 0)
        }
        db.insert(TABLE_USER, null, userCv)
    }
}
