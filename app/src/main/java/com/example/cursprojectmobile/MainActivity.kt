package com.example.cursprojectmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cursprojectmobile.presentation.auth.AuthViewModel
import com.example.cursprojectmobile.presentation.auth.LoginScreen
import com.example.cursprojectmobile.presentation.auth.SignUpScreen
import com.example.cursprojectmobile.presentation.cart.CartScreen
import com.example.cursprojectmobile.presentation.cart.CartViewModel
import com.example.cursprojectmobile.presentation.chat.ChatScreen
import com.example.cursprojectmobile.presentation.chat.ChatViewModel
import com.example.cursprojectmobile.presentation.detail.DetailViewModel
import com.example.cursprojectmobile.presentation.detail.FoodDetailScreen
import com.example.cursprojectmobile.presentation.home.ExploreMenuScreen
import com.example.cursprojectmobile.presentation.home.MealMenuScreen
import com.example.cursprojectmobile.presentation.home.HomeViewModel
import com.example.cursprojectmobile.presentation.location.LocationViewModel
import com.example.cursprojectmobile.presentation.location.SetLocationScreen
import com.example.cursprojectmobile.presentation.notifications.NotificationsScreen
import com.example.cursprojectmobile.presentation.onboarding.OnboardingViewModel
import com.example.cursprojectmobile.presentation.onboarding.SplashScreen
import com.example.cursprojectmobile.presentation.onboarding.WelcomeScreen
import com.example.cursprojectmobile.presentation.order.FinishOrderScreen
import com.example.cursprojectmobile.presentation.order.OrderViewModel
import com.example.cursprojectmobile.presentation.profile.ProfileScreen
import com.example.cursprojectmobile.presentation.profile.ProfileViewModel
import com.example.cursprojectmobile.ui.theme.CursProjectMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CursProjectMobileTheme {
                FoodCouriersNavigation()
            }
        }
    }
}

@Composable
fun FoodCouriersNavigation() {
    val navController = rememberNavController()

    val onboardingViewModel: OnboardingViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()
    val locationViewModel: LocationViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val chatViewModel: ChatViewModel = viewModel()
    val detailViewModel: DetailViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                viewModel = onboardingViewModel,
                onNavigateToWelcome = {
                    navController.navigate("welcome") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("welcome") {
            WelcomeScreen(
                onGetStarted = {
                    navController.navigate("signup")
                },
                onSignIn = {
                    navController.navigate("login")
                }
            )
        }

        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToSignUp = {
                    navController.navigate("signup")
                },
                onLoginSuccess = {
                    homeViewModel.refreshSessionAddress()
                    navController.navigate("home") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }

        composable("signup") {
            SignUpScreen(
                viewModel = authViewModel,
                onNavigateToSignIn = {
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onSignUpSuccess = {
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onGoogleSignUpSuccess = {
                    homeViewModel.refreshSessionAddress()
                    navController.navigate("home") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            ExploreMenuScreen(
                viewModel = homeViewModel,
                onNavigateToFoodDetail = { foodId ->
                    navController.navigate("detail/$foodId")
                },
                onNavigateToMealMenu = {
                    navController.navigate("meal_menu")
                },
                onNavigateToLocation = {
                    navController.navigate("location")
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                },
                onNavigateToCart = {
                    navController.navigate("cart")
                },
                onNavigateToChat = {
                    navController.navigate("chat")
                }
            )
        }

        composable("meal_menu") {
            MealMenuScreen(
                viewModel = homeViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToFoodDetail = { foodId ->
                    navController.navigate("detail/$foodId")
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                },
                onNavigateToCart = {
                    navController.navigate("cart")
                },
                onNavigateToChat = {
                    navController.navigate("chat")
                },
                onNavigateToNotifications = {
                    navController.navigate("notifications")
                }
            )
        }

        composable("detail/{foodId}") { backStackEntry ->
            val foodId = backStackEntry.arguments?.getString("foodId")?.toLongOrNull() ?: 1L
            FoodDetailScreen(
                foodId = foodId,
                viewModel = detailViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToCart = {
                    navController.navigate("cart")
                }
            )
        }

        composable("cart") {
            CartScreen(
                viewModel = cartViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                },
                onNavigateToChat = {
                    navController.navigate("chat")
                },
                onOrderPlaced = { orderId ->
                    homeViewModel.refreshSessionAddress()
                    navController.navigate("finish_order/$orderId") {
                        popUpTo("cart") { inclusive = true }
                    }
                }
            )
        }

        composable("location") {
            SetLocationScreen(
                viewModel = locationViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onLocationSet = { address ->
                    authViewModel.updateAddress(address)
                    homeViewModel.refreshSessionAddress()
                },
                onNavigateToNotifications = {
                    navController.navigate("notifications")
                }
            )
        }

        composable("notifications") {
            NotificationsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("finish_order/{orderId}") { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")?.toLongOrNull() ?: 0L
            FinishOrderScreen(
                orderId = orderId,
                viewModel = orderViewModel,
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                    }
                }
            )
        }

        composable("profile") {
            val cartCount = homeViewModel.uiState.collectAsState().value.cartCount
            ProfileScreen(
                viewModel = profileViewModel,
                cartItemCount = cartCount,
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                onNavigateToCart = {
                    navController.navigate("cart")
                },
                onNavigateToChat = {
                    navController.navigate("chat")
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("welcome") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("chat") {
            val cartCount = homeViewModel.uiState.collectAsState().value.cartCount
            ChatScreen(
                viewModel = chatViewModel,
                cartItemCount = cartCount,
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                },
                onNavigateToCart = {
                    navController.navigate("cart")
                }
            )
        }
    }
}
