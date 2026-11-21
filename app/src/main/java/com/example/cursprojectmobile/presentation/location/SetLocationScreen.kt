package com.example.cursprojectmobile.presentation.location

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.cursprojectmobile.ui.theme.BrandLightPink
import com.example.cursprojectmobile.ui.theme.BrandPrimary
import com.example.cursprojectmobile.ui.theme.TextDark
import com.example.cursprojectmobile.ui.theme.TextGray

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SetLocationScreen(
    viewModel: LocationViewModel,
    onBack: () -> Unit,
    onLocationSet: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    // When coordinates change, dynamically animate the real map!
    LaunchedEffect(uiState.selectedLatitude, uiState.selectedLongitude) {
        webViewInstance?.evaluateJavascript(
            "if (typeof updateLocation === 'function') { updateLocation(${uiState.selectedLatitude}, ${uiState.selectedLongitude}); }",
            null
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFDE8EC))
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "‹",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandPrimary
                )
            }

            Text(
                text = "Set Your Location",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFDE8EC)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🔔", fontSize = 18.sp)
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Real Interactive OpenStreetMap
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, Color(0xFFE8E8E8), RoundedCornerShape(20.dp))
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        WebView(ctx).apply {
                            webViewClient = object : WebViewClient() {}
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.useWideViewPort = true
                            settings.loadWithOverviewMode = true

                            val mapHtml = """
                                <!DOCTYPE html>
                                <html>
                                <head>
                                  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
                                  <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
                                  <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                                  <style>
                                    html, body, #map { margin: 0; padding: 0; width: 100%; height: 100%; background: #e5e3df; }
                                    .custom-pin {
                                      background: #D61355;
                                      border: 3px solid #FFFFFF;
                                      border-radius: 50%;
                                      width: 24px;
                                      height: 24px;
                                      box-shadow: 0 3px 8px rgba(0,0,0,0.35);
                                    }
                                  </style>
                                </head>
                                <body>
                                  <div id="map"></div>
                                  <script>
                                    var map = L.map('map', { zoomControl: false, attributionControl: false })
                                                .setView([${uiState.selectedLatitude}, ${uiState.selectedLongitude}], 15);
                                    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
                                      maxZoom: 19
                                    }).addTo(map);

                                    var icon = L.divIcon({ className: 'custom-pin', iconSize: [24, 24], iconAnchor: [12, 12] });
                                    var marker = L.marker([${uiState.selectedLatitude}, ${uiState.selectedLongitude}], { icon: icon }).addTo(map);

                                    function updateLocation(lat, lon) {
                                      map.flyTo([lat, lon], 15, { animate: true, duration: 1.2 });
                                      marker.setLatLng([lat, lon]);
                                    }
                                  </script>
                                </body>
                                </html>
                            """.trimIndent()
                            loadDataWithBaseURL("https://openstreetmap.org", mapHtml, "text/html", "UTF-8", null)
                            webViewInstance = this
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Your Current Delivery Location",
                fontSize = 13.sp,
                color = TextGray,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Fixed height container with minLines = 2 so text NEVER jumps!
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = uiState.selectedAddress,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    lineHeight = 24.sp,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Select From Saved Addresses",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Preset Addresses with real coordinates linked
            uiState.presetLocations.forEach { loc ->
                val isSelected = loc.address.equals(uiState.selectedAddress, ignoreCase = true)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { viewModel.selectAddress(loc.address) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) BrandLightPink else Color(0xFFF9F9F9)
                    ),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, BrandPrimary) else null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (loc.label == "Home") "📍" else "🏠",
                            fontSize = 20.sp
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = loc.label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) BrandPrimary else TextGray
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = loc.address,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = TextDark,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        if (isSelected) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(BrandPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("✓", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Bottom Confirm Button
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp,
            color = Color.White
        ) {
            Box(modifier = Modifier.padding(20.dp)) {
                Button(
                    onClick = {
                        viewModel.confirmLocation {
                            onLocationSet(uiState.selectedAddress)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                ) {
                    Text(
                        text = "Confirm Location",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
