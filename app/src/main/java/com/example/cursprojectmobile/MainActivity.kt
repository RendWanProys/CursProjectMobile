package com.example.cursprojectmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.cursprojectmobile.data.db.CartDao
import com.example.cursprojectmobile.data.db.DatabaseHelper
import com.example.cursprojectmobile.data.db.FoodDao
import com.example.cursprojectmobile.data.db.OrderDao
import com.example.cursprojectmobile.data.db.UserDao
import com.example.cursprojectmobile.data.repository.CartRepository
import com.example.cursprojectmobile.data.repository.FoodRepository
import com.example.cursprojectmobile.data.repository.OrderRepository
import com.example.cursprojectmobile.data.repository.UserRepository
import com.example.cursprojectmobile.ui.components.BottomNavBar
import com.example.cursprojectmobile.ui.screens.OnboardingScreen
import com.example.cursprojectmobile.ui.screens.auth.LoginScreen
import com.example.cursprojectmobile.ui.screens.auth.RegisterScreen
import com.example.cursprojectmobile.ui.screens.cart.CartScreen
import com.example.cursprojectmobile.ui.screens.chat.ChatScreen
import com.example.cursprojectmobile.ui.screens.detail.FoodDetailScreen
import com.example.cursprojectmobile.ui.screens.home.ExploreMenuScreen
import com.example.cursprojectmobile.ui.screens.location.SetLocationScreen
import com.example.cursprojectmobile.ui.screens.order.FinishOrderScreen
import com.example.cursprojectmobile.ui.screens.profile.ProfileScreen
import com.example.cursprojectmobile.ui.screens.splash.SplashScreen
import com.example.cursprojectmobile.ui.theme.CursProjectMobileTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CursProjectMobileTheme {
                AppRoot()
            }
        }
    }
}

@Composable
fun AppRoot() {
    val context = LocalContext.current

    // Инициализация базы данных SQLite и репозиториев
    val dbHelper = remember { DatabaseHelper(context.applicationContext) }
    val foodDao = remember { FoodDao(dbHelper) }
    val cartDao = remember { CartDao(dbHelper) }
    val orderDao = remember { OrderDao(dbHelper) }
    val userDao = remember { UserDao(dbHelper) }

    val foodRepository = remember { FoodRepository(foodDao) }
    val cartRepository = remember { CartRepository(cartDao) }
    val orderRepository = remember { OrderRepository(orderDao) }
    val userRepository = remember { UserRepository(userDao) }

    // Состояния приложения
    var currentScreen by remember { mutableStateOf("splash") }
    var selectedBottomTab by remember { mutableIntStateOf(0) }
    var selectedFoodId by remember { mutableLongStateOf(0L) }
    var completedOrderId by remember { mutableLongStateOf(0L) }
    var currentAddress by remember {
        mutableStateOf("109, Freedom Way, Off Lekki Phase Victoria Island, Lagos.")
    }
    var cartCount by remember { mutableIntStateOf(cartRepository.getCartCount()) }

    fun updateCartBadge() {
        cartCount = cartRepository.getCartCount()
    }

    when (currentScreen) {
        "splash" -> {
            SplashScreen(
                onNavigateNext = {
                    val user = FirebaseAuth.getInstance().currentUser
                    currentScreen = if (user != null) "main" else "onboarding"
                }
            )
        }
        "onboarding" -> {
            OnboardingScreen(
                onFinishOnboarding = { currentScreen = "register" }
            )
        }
        "register" -> {
            RegisterScreen(
                onNavigateToLogin = { currentScreen = "login" },
                onRegisterSuccess = {
                    updateCartBadge()
                    currentScreen = "main"
                }
            )
        }
        "login" -> {
            LoginScreen(
                onNavigateToRegister = { currentScreen = "register" },
                onLoginSuccess = {
                    updateCartBadge()
                    currentScreen = "main"
                }
            )
        }
        "food_detail" -> {
            FoodDetailScreen(
                foodId = selectedFoodId,
                foodRepository = foodRepository,
                cartRepository = cartRepository,
                onBack = {
                    updateCartBadge()
                    currentScreen = "main"
                },
                onCartUpdated = { updateCartBadge() }
            )
        }
        "set_location" -> {
            SetLocationScreen(
                currentAddress = currentAddress,
                onBack = { currentScreen = "main" },
                onLocationSet = { newAddress ->
                    currentAddress = newAddress
                }
            )
        }
        "finish_order" -> {
            FinishOrderScreen(
                orderId = completedOrderId,
                orderRepository = orderRepository,
                onNavigateHome = {
                    updateCartBadge()
                    selectedBottomTab = 0
                    currentScreen = "main"
                }
            )
        }
        "main" -> {
            Scaffold(
                bottomBar = {
                    BottomNavBar(
                        selectedTab = selectedBottomTab,
                        cartItemCount = cartCount,
                        onTabSelected = { tab ->
                            selectedBottomTab = tab
                            updateCartBadge()
                        }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(Color(0xFFFAFAFA))
                ) {
                    when (selectedBottomTab) {
                        0 -> {
                            ExploreMenuScreen(
                                foodRepository = foodRepository,
                                cartRepository = cartRepository,
                                currentAddress = currentAddress,
                                onNavigateToFoodDetail = { foodId ->
                                    selectedFoodId = foodId
                                    currentScreen = "food_detail"
                                },
                                onNavigateToLocation = {
                                    currentScreen = "set_location"
                                },
                                onNavigateToProfile = {
                                    selectedBottomTab = 1
                                },
                                onCartUpdated = { updateCartBadge() }
                            )
                        }
                        1 -> {
                            ProfileScreen(
                                userRepository = userRepository,
                                orderRepository = orderRepository,
                                onNavigateBack = { selectedBottomTab = 0 },
                                onSignOut = {
                                    FirebaseAuth.getInstance().signOut()
                                    currentScreen = "login"
                                }
                            )
                        }
                        2 -> {
                            CartScreen(
                                cartRepository = cartRepository,
                                orderRepository = orderRepository,
                                currentAddress = currentAddress,
                                onNavigateBack = { selectedBottomTab = 0 },
                                onNavigateToHome = { selectedBottomTab = 0 },
                                onOrderPlaced = { orderId ->
                                    completedOrderId = orderId
                                    updateCartBadge()
                                    currentScreen = "finish_order"
                                },
                                onCartUpdated = { updateCartBadge() }
                            )
                        }
                        3 -> {
                            ChatScreen(
                                onNavigateBack = { selectedBottomTab = 0 }
                            )
                        }
                    }
                }
            }
        }
    }
}
