package com.example.cursprojectmobile.data

import android.content.Context
import android.content.SharedPreferences
import com.example.cursprojectmobile.FoodCouriersApp
import com.example.cursprojectmobile.domain.model.User
import org.json.JSONObject

object UserSessionManager {
    private const val PREF_NAME = "food_couriers_session"
    private const val KEY_IS_LOGGED_IN = "key_is_logged_in"
    private const val KEY_UID = "key_uid"
    private const val KEY_EMAIL = "key_email"
    private const val KEY_FULL_NAME = "key_full_name"
    private const val KEY_USER_NAME = "key_user_name"
    private const val KEY_PHONE = "key_phone"
    private const val KEY_ADDRESS = "key_address"
    private const val KEY_AVATAR_PATH = "key_avatar_path"
    private const val KEY_REGISTERED_USERS_JSON = "key_registered_users_json"
    private const val KEY_USER_CART_PREFIX = "key_user_cart_"
    private const val KEY_USER_ORDERS_PREFIX = "key_user_orders_"

    private val prefs: SharedPreferences by lazy {
        FoodCouriersApp.instance.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun getCurrentUser(): User? {
        if (!isLoggedIn()) return null
        val uid = prefs.getString(KEY_UID, "") ?: ""
        val email = prefs.getString(KEY_EMAIL, "") ?: ""
        if (uid.isEmpty() && email.isEmpty()) return null

        return User(
            uid = uid,
            email = email,
            fullName = prefs.getString(KEY_FULL_NAME, "Guest User") ?: "Guest User",
            userName = prefs.getString(KEY_USER_NAME, "guest") ?: "guest",
            phone = prefs.getString(KEY_PHONE, "") ?: "",
            address = prefs.getString(KEY_ADDRESS, "4517 Washington Ave. Manchester, Kentucky 39495")
                ?: "4517 Washington Ave. Manchester, Kentucky 39495",
            avatarPath = prefs.getString(KEY_AVATAR_PATH, "") ?: ""
        )
    }

    fun saveSession(user: User) {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putString(KEY_UID, user.uid)
            .putString(KEY_EMAIL, user.email)
            .putString(KEY_FULL_NAME, user.fullName)
            .putString(KEY_USER_NAME, user.userName)
            .putString(KEY_PHONE, user.phone)
            .putString(KEY_ADDRESS, user.address)
            .putString(KEY_AVATAR_PATH, user.avatarPath)
            .apply()
    }

    fun updateAddress(address: String): User {
        val current = getCurrentUser() ?: User(address = address)
        val updated = current.copy(address = address)
        saveSession(updated)
        return updated
    }

    fun saveRegisteredUser(email: String, pass: String, user: User) {
        try {
            val jsonStr = prefs.getString(KEY_REGISTERED_USERS_JSON, "{}") ?: "{}"
            val jsonObj = JSONObject(jsonStr)
            val userObj = JSONObject().apply {
                put("password", pass)
                put("uid", user.uid)
                put("email", user.email)
                put("fullName", user.fullName)
                put("userName", user.userName)
                put("phone", user.phone)
                put("address", user.address)
                put("avatarPath", user.avatarPath)
            }
            jsonObj.put(email.lowercase(), userObj)
            prefs.edit().putString(KEY_REGISTERED_USERS_JSON, jsonObj.toString()).apply()
        } catch (_: Exception) {}
    }

    fun getRegisteredUser(email: String): Pair<String, User>? {
        try {
            val jsonStr = prefs.getString(KEY_REGISTERED_USERS_JSON, "{}") ?: "{}"
            val jsonObj = JSONObject(jsonStr)
            val lower = email.lowercase()
            if (!jsonObj.has(lower)) return null
            val obj = jsonObj.getJSONObject(lower)
            val pass = obj.optString("password", "")
            val user = User(
                uid = obj.optString("uid", ""),
                email = obj.optString("email", ""),
                fullName = obj.optString("fullName", ""),
                userName = obj.optString("userName", ""),
                phone = obj.optString("phone", ""),
                address = obj.optString("address", "4517 Washington Ave. Manchester, Kentucky 39495"),
                avatarPath = obj.optString("avatarPath", "")
            )
            return pass to user
        } catch (_: Exception) {
            return null
        }
    }

    fun saveUserCart(userId: String, cartJson: String) {
        if (userId.isEmpty()) return
        prefs.edit().putString(KEY_USER_CART_PREFIX + userId, cartJson).apply()
    }

    fun getUserCart(userId: String): String {
        if (userId.isEmpty()) return "[]"
        return prefs.getString(KEY_USER_CART_PREFIX + userId, "[]") ?: "[]"
    }

    fun saveUserOrders(userId: String, ordersJson: String) {
        if (userId.isEmpty()) return
        prefs.edit().putString(KEY_USER_ORDERS_PREFIX + userId, ordersJson).apply()
    }

    fun getUserOrders(userId: String): String {
        if (userId.isEmpty()) return "[]"
        return prefs.getString(KEY_USER_ORDERS_PREFIX + userId, "[]") ?: "[]"
    }

    fun clearSession() {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .apply()
    }
}
