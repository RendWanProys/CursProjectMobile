package com.example.cursprojectmobile.data.db

import android.content.ContentValues
import android.database.Cursor
import com.example.cursprojectmobile.data.model.UserProfile

class UserDao(private val dbHelper: DatabaseHelper) {

    fun getUserProfile(): UserProfile {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_USER,
            null,
            "${DatabaseHelper.COL_USER_ID} = 1",
            null, null, null, null
        )
        cursor.use {
            if (it.moveToFirst()) {
                return UserProfile(
                    id = it.getLong(it.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_USER_NAME)),
                    email = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_USER_EMAIL)),
                    phone = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_USER_PHONE)) ?: "",
                    occupation = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_USER_OCCUPATION)) ?: "",
                    employer = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_USER_EMPLOYER)) ?: "",
                    country = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_USER_COUNTRY)) ?: "",
                    isNotificationEnabled = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.COL_USER_NOTIFICATIONS)) == 1
                )
            }
        }
        return UserProfile()
    }

    fun updateUserProfile(profile: UserProfile): Boolean {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put(DatabaseHelper.COL_USER_NAME, profile.name)
            put(DatabaseHelper.COL_USER_EMAIL, profile.email)
            put(DatabaseHelper.COL_USER_PHONE, profile.phone)
            put(DatabaseHelper.COL_USER_OCCUPATION, profile.occupation)
            put(DatabaseHelper.COL_USER_EMPLOYER, profile.employer)
            put(DatabaseHelper.COL_USER_COUNTRY, profile.country)
            put(DatabaseHelper.COL_USER_NOTIFICATIONS, if (profile.isNotificationEnabled) 1 else 0)
        }
        return db.update(
            DatabaseHelper.TABLE_USER,
            cv,
            "${DatabaseHelper.COL_USER_ID} = 1",
            null
        ) > 0
    }
}
