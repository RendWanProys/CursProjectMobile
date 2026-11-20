package com.example.cursprojectmobile.presentation.components

import Rg.CursProjectMobile.R

/**
 * Централизованный реестр графических ресурсов и заглушек.
 * Для замены картинки:
 * 1. Положите файл изображения в папку res/drawable/ (например my_burger.png)
 * 2. Замените соответствующую константу здесь (например R.drawable.my_burger)
 * Или передайте null, чтобы автоматически отображалась стильная векторная заглушка.
 */
object FoodImages {
    // Баннер акций на главном экране
    val BANNER_OFFER: Int? = R.drawable.food_banner_offer

    // Аватар пользователя
    val USER_AVATAR: Int? = R.drawable.user_avatar

    // Карта для экрана выбора адреса
    val MAP_PREVIEW: Int? = R.drawable.map_preview

    // Детальное фото бургера
    val BURGER_DETAIL: Int? = R.drawable.food_burger_detail

    // Блюда из меню
    val CHICKEN_BURGER: Int? = R.drawable.food_chicken_burger
    val CHEESE_BURGER: Int? = R.drawable.food_cheese_burger
    val PEPPER_PIZZA: Int? = R.drawable.food_pepper_pizza
    val ONION_PIZZA: Int? = R.drawable.food_onion_pizza
    val CHICKEN_SPAGHETTI: Int? = R.drawable.food_chicken_spaghetti
    val JOLLOF_RICE: Int? = R.drawable.food_jollof_rice
    val FRUITY_PANCAKES: Int? = R.drawable.food_fruity_pancakes
    val SPICY_SHAWARMA: Int? = R.drawable.food_spicy_shawarma

    fun getDrawableByName(name: String): Int? {
        return when (name) {
            "food_chicken_burger" -> CHICKEN_BURGER
            "food_cheese_burger" -> CHEESE_BURGER
            "food_burger_detail" -> BURGER_DETAIL
            "food_pepper_pizza" -> PEPPER_PIZZA
            "food_onion_pizza" -> ONION_PIZZA
            "food_chicken_spaghetti" -> CHICKEN_SPAGHETTI
            "food_jollof_rice" -> JOLLOF_RICE
            "food_fruity_pancakes" -> FRUITY_PANCAKES
            "food_spicy_shawarma" -> SPICY_SHAWARMA
            "food_banner_offer" -> BANNER_OFFER
            "user_avatar" -> USER_AVATAR
            "map_preview" -> MAP_PREVIEW
            else -> null
        }
    }
}
