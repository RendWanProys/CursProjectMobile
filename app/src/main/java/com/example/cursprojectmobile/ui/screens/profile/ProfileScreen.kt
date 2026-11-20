package com.example.cursprojectmobile.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cursprojectmobile.data.model.UserProfile
import com.example.cursprojectmobile.data.repository.OrderRepository
import com.example.cursprojectmobile.data.repository.UserRepository
import com.example.cursprojectmobile.ui.components.FoodImagePlaceholder
import com.example.cursprojectmobile.ui.components.FoodImages

val PrimaryRed = Color(0xFFD61355)
val SoftPink = Color(0xFFFFF0F3)
val TextDark = Color(0xFF1F1F1F)
val TextGray = Color(0xFF757575)

@Composable
fun ProfileScreen(
    userRepository: UserRepository,
    orderRepository: OrderRepository,
    onNavigateBack: () -> Unit,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    var profile by remember { mutableStateOf(userRepository.getUserProfile()) }
    var isEditing by remember { mutableStateOf(false) }

    var name by remember(profile) { mutableStateOf(profile.name) }
    var phone by remember(profile) { mutableStateOf(profile.phone) }
    var occupation by remember(profile) { mutableStateOf(profile.occupation) }
    var employer by remember(profile) { mutableStateOf(profile.employer) }
    var notificationsEnabled by remember(profile) { mutableStateOf(profile.isNotificationEnabled) }

    val orders = remember { orderRepository.getAllOrders() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .verticalScroll(rememberScrollState())
    ) {
        // --- HEADER ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftPink)
                    .clickable(onClick = onNavigateBack),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "❮", fontSize = 18.sp, color = PrimaryRed, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "Profile",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftPink)
                    .clickable {
                        Toast.makeText(context, "Notifications", Toast.LENGTH_SHORT).show()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🔔", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- AVATAR WITH EDIT BADGE ---
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(110.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(SoftPink),
                contentAlignment = Alignment.Center
            ) {
                FoodImagePlaceholder(
                    drawableRes = FoodImages.USER_AVATAR,
                    emoji = "👩",
                    cornerRadius = 55.dp,
                    fontSize = 50.sp,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Pencil edit icon
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(PrimaryRed)
                    .clickable { isEditing = !isEditing },
                contentAlignment = Alignment.Center
            ) {
                Text("✏️", fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- PERSONAL INFO SECTION ---
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "Personal Info",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (isEditing) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Your name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = occupation,
                            onValueChange = { occupation = it },
                            label = { Text("Occupation") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = employer,
                            onValueChange = { employer = it },
                            label = { Text("Employer") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    } else {
                        ProfileRow(label = "Your name", value = profile.name)
                        HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(vertical = 10.dp))
                        ProfileRow(label = "Occupation", value = profile.occupation)
                        HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(vertical = 10.dp))
                        ProfileRow(label = "Employer", value = profile.employer)
                    }

                    HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(vertical = 10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Country (${profile.country})", fontSize = 14.sp, color = TextDark)
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = PrimaryRed
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- CONTACT INFO SECTION ---
            Text(
                text = "Contact Info",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (isEditing) {
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone number") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    } else {
                        ProfileRow(label = "Phone number", value = profile.phone)
                    }
                    HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(vertical = 10.dp))
                    ProfileRow(label = "Email", value = profile.email)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- ORDERS HISTORY SUMMARY ---
            Text(
                text = "Order History (${orders.size})",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (orders.isEmpty()) {
                        Text("No orders yet", fontSize = 14.sp, color = TextGray)
                    } else {
                        orders.take(3).forEach { o ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Order #${o.id}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Text("$ ${"%.2f".format(o.totalAmount)}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryRed)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- ACTION BUTTONS ---
            if (isEditing) {
                Button(
                    onClick = {
                        val updated = profile.copy(
                            name = name,
                            phone = phone,
                            occupation = occupation,
                            employer = employer,
                            isNotificationEnabled = notificationsEnabled
                        )
                        userRepository.updateUserProfile(updated)
                        profile = updated
                        isEditing = false
                        Toast.makeText(context, "Profile updated!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                ) {
                    Text("Save Changes", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            } else {
                Button(
                    onClick = { isEditing = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                ) {
                    Text("Edit Profile", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onSignOut,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryRed),
                border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryRed)
            ) {
                Text("Log Out", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ProfileRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 14.sp, color = TextGray)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
    }
}
