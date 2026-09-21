package com.example.ui.customer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.*
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun CustomerInstaHelpScreen(
  onDispatchConfirmed: (InstaHelpRequest) -> Unit,
  onNavigateBack: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  // Service Options
  val serviceOptions = listOf(
    "Electrical Emergency" to Icons.Default.ElectricBolt,
    "Water Leakage" to Icons.Default.WaterDrop,
    "AC Breakdown" to Icons.Default.AcUnit,
    "Other Urgent Help" to Icons.Default.Handyman
  )

  var selectedService by remember { mutableStateOf("Electrical Emergency") }
  var currentLocation by remember { mutableStateOf("Salt Lake, Sector V, Kolkata") }
  val mockCoordinates = "22.5850° N, 88.4312° E"
  var selectedRadiusKm by remember { mutableDoubleStateOf(5.0) }

  // Worker Selection & Confirmation State
  var selectedWorkerForRequest by remember { mutableStateOf<InstaHelpWorkerResult?>(null) }
  var activeDispatchedRequest by remember { mutableStateOf<InstaHelpRequest?>(null) }
  var showLocationEditDialog by remember { mutableStateOf(false) }

  // Radar Animation
  val infiniteTransition = rememberInfiniteTransition(label = "radar_anim")
  val pulseRadius by infiniteTransition.animateFloat(
    initialValue = 0.2f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(2000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "pulse_radius"
  )

  // Worker Results dynamically computed based on selected emergency service & radius
  val availableWorkers = remember(selectedService, selectedRadiusKm) {
    SampleData.getNearbyInstaHelpWorkers(selectedService, selectedRadiusKm)
  }

  // Location Selector Dialog
  if (showLocationEditDialog) {
    var tempLoc by remember { mutableStateOf(currentLocation) }
    Dialog(onDismissRequest = { showLocationEditDialog = false }) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = HomezyCard,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text("Change Emergency Location", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = HomezyText)
          Spacer(modifier = Modifier.height(12.dp))
          OutlinedTextField(
            value = tempLoc,
            onValueChange = { tempLoc = it },
            label = { Text("Address / Neighborhood") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text("Mock Coordinates: $mockCoordinates", fontSize = 11.sp, color = HomezyTextSecondary)
          Spacer(modifier = Modifier.height(16.dp))
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = { showLocationEditDialog = false }) {
              Text("Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
              onClick = {
                currentLocation = tempLoc
                showLocationEditDialog = false
              },
              colors = ButtonDefaults.buttonColors(containerColor = HomezyPrimary)
            ) {
              Text("Update Location")
            }
          }
        }
      }
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
  ) {
    if (activeDispatchedRequest != null) {
      val req = activeDispatchedRequest!!
      ActiveInstaHelpTrackerView(
        request = req,
        onSimulateWorkerAccept = {
          activeDispatchedRequest = req.copy(status = InstaHelpStatus.ACCEPTED)
        },
        onCancelRequest = {
          activeDispatchedRequest = null
        }
      )
    } else {
      // Main Customer Insta Help Flow
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .testTag("customer_insta_help_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
    // Top Emergency Hero
    item {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = Color(0xFFFEE2E2),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Bolt,
              contentDescription = null,
              tint = Color(0xFFDC2626),
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "INSTA HELP • RAPID RESPONSE",
              fontSize = 12.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color(0xFFDC2626)
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "Need help right now?",
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "Find the nearest available verified worker.",
          fontSize = 14.sp,
          color = HomezyTextSecondary,
          textAlign = TextAlign.Center
        )
      }
    }

    // Prototype Disclaimer Notice
    item {
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFFFFBEB),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
        modifier = Modifier.fillMaxWidth().testTag("insta_help_prototype_notice")
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.Top
        ) {
          Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = Color(0xFFB45309),
            modifier = Modifier.size(18.dp).padding(top = 1.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Prototype for urgent household and community services. Not for municipal emergency or public-safety dispatch.",
            fontSize = 11.sp,
            color = Color(0xFF92400E),
            lineHeight = 15.sp
          )
        }
      }
    }

    // Service Options Selection
    item {
      Text(
        text = "Select Urgent Service:",
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = HomezyText
      )
    }

    item {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        serviceOptions.forEach { (optionName, icon) ->
          val isSelected = selectedService == optionName
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isSelected) HomezyPrimaryContainer else HomezyCard,
            border = androidx.compose.foundation.BorderStroke(
              width = if (isSelected) 2.dp else 1.dp,
              color = if (isSelected) HomezyPrimary else HomezyBorder
            ),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { selectedService = optionName }
              .testTag("insta_service_option_${optionName.lowercase().replace(" ", "_")}")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isSelected) HomezyPrimary else HomezySurfaceVariant),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = icon,
                  contentDescription = null,
                  tint = if (isSelected) Color.White else HomezyPrimary,
                  modifier = Modifier.size(20.dp)
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = optionName,
                  fontSize = 15.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                  color = HomezyText
                )
                Text(
                  text = when (optionName) {
                    "Electrical Emergency" -> "Short circuits, sparking switchboards, main MCB trips"
                    "Water Leakage" -> "Pipe bursts, tap leaks, drain overflow emergencies"
                    "AC Breakdown" -> "Sudden cooling stoppage, refrigerant leak, compressor trip"
                    else -> "Door lock jammed, urgent domestic carpentry & handyman fixes"
                  },
                  fontSize = 11.sp,
                  color = HomezyTextSecondary
                )
              }

              RadioButton(
                selected = isSelected,
                onClick = { selectedService = optionName },
                colors = RadioButtonDefaults.colors(selectedColor = HomezyPrimary)
              )
            }
          }
        }
      }
    }

    // Location & Service Radius Section
    item {
      HomezyCard(
        modifier = Modifier.fillMaxWidth().testTag("insta_location_radius_card")
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color(0xFFDC2626),
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Emergency Location",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = HomezyText
              )
            }

            TextButton(
              onClick = { showLocationEditDialog = true },
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Text("Change", fontSize = 12.sp, color = HomezyPrimary, fontWeight = FontWeight.Bold)
            }
          }

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = currentLocation,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = HomezyText
          )
          Text(
            text = "Mock Coordinates: $mockCoordinates • GPS Accuracy: ±5m",
            fontSize = 11.sp,
            color = HomezyTextSecondary
          )

          Spacer(modifier = Modifier.height(14.dp))
          Divider(color = HomezyBorder)
          Spacer(modifier = Modifier.height(12.dp))

          // Service Radius Filter
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Service Radius:",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyText
            )
            Text(
              text = "${selectedRadiusKm.toInt()} km",
              fontSize = 13.sp,
              fontWeight = FontWeight.ExtraBold,
              color = HomezyPrimary
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            val radiusOptions = listOf(1.0, 3.0, 5.0, 10.0)
            radiusOptions.forEach { radius ->
              val isSelected = selectedRadiusKm == radius
              Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) HomezyPrimary else HomezySurfaceVariant,
                modifier = Modifier
                  .weight(1f)
                  .clickable { selectedRadiusKm = radius }
                  .testTag("radius_chip_${radius.toInt()}km")
              ) {
                Text(
                  text = "${radius.toInt()} km",
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) Color.White else HomezyText,
                  textAlign = TextAlign.Center,
                  modifier = Modifier.padding(vertical = 6.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Sonar Radar Visualizer Box
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(140.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFFF1F5F9)),
            contentAlignment = Alignment.Center
          ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
              val center = this.center
              val maxRadius = size.minDimension / 2 - 10.dp.toPx()

              // Grid circles
              drawCircle(Color(0xFFCBD5E1), radius = maxRadius * 0.33f, style = Stroke(1.5f))
              drawCircle(Color(0xFFCBD5E1), radius = maxRadius * 0.66f, style = Stroke(1.5f))
              drawCircle(Color(0xFFCBD5E1), radius = maxRadius, style = Stroke(1.5f))

              // Sonar pulse
              drawCircle(
                color = Color(0xFFDC2626).copy(alpha = (1f - pulseRadius).coerceIn(0f, 1f) * 0.4f),
                radius = maxRadius * pulseRadius,
                style = Stroke(3f)
              )
            }

            // User Center Pin
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFFDC2626)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Home, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }

            // Nearby Worker Marker (Rahul Das Pin)
            Box(
              modifier = Modifier
                .offset(x = 42.dp, y = (-28).dp)
                .size(30.dp)
                .clip(CircleShape)
                .background(HomezyPrimary),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Handyman, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            }

            // Worker 2 Pin
            Box(
              modifier = Modifier
                .offset(x = (-46).dp, y = 24.dp)
                .size(26.dp)
                .clip(CircleShape)
                .background(HomezySecondary),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.ElectricBolt, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
            }

            // Bottom radar status text
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color.Black.copy(alpha = 0.7f),
              modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 6.dp)
            ) {
              Text(
                text = "${availableWorkers.size} verified workers ready within ${selectedRadiusKm.toInt()} km",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }
        }
      }
    }

    // Worker Results Section
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Nearby Available Workers (${availableWorkers.size})",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp,
          color = HomezyText
        )
        Text(
          text = "Sorted by proximity",
          fontSize = 11.sp,
          color = HomezyTextSecondary
        )
      }
    }

    if (availableWorkers.isEmpty()) {
      item {
        HomezyCard(modifier = Modifier.fillMaxWidth()) {
          Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(Icons.Default.SearchOff, contentDescription = null, tint = HomezyTextSecondary, modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text("No workers available within ${selectedRadiusKm.toInt()} km", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("Try expanding the search radius to 10 km.", fontSize = 12.sp, color = HomezyTextSecondary)
          }
        }
      }
    } else {
      items(availableWorkers) { worker ->
        InstaHelpWorkerResultCard(
          worker = worker,
          onRequestInstaHelp = { selectedWorkerForRequest = worker }
        )
      }
    }
  }

  // Confirmation Modal Overlay
  if (selectedWorkerForRequest != null) {
    val worker = selectedWorkerForRequest!!
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0.6f)),
      contentAlignment = Alignment.Center
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .clickable { selectedWorkerForRequest = null }
      )
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = HomezyCard,
        shadowElevation = 8.dp,
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
          .testTag("insta_help_confirmation_dialog")
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
        ) {
          // Modal Header
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFFEE2E2)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Bolt,
                  contentDescription = null,
                  tint = Color(0xFFDC2626),
                  modifier = Modifier.size(22.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Confirm Insta Help",
                  fontWeight = FontWeight.Bold,
                  fontSize = 17.sp,
                  color = HomezyText
                )
                Text(
                  text = "Priority Dispatch Review",
                  fontSize = 12.sp,
                  color = HomezyTextSecondary
                )
              }
            }
            IconButton(
              onClick = { selectedWorkerForRequest = null },
              modifier = Modifier.size(32.dp)
            ) {
              Icon(Icons.Default.Close, contentDescription = "Close", tint = HomezyTextSecondary)
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
          HorizontalDivider(color = HomezyBorder)
          Spacer(modifier = Modifier.height(14.dp))

          // Key Review Items
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = HomezySurfaceVariant,
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(14.dp),
              verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              // Service
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Service:", fontSize = 12.sp, color = HomezyTextSecondary)
                Text(selectedService, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = HomezyText)
              }

              // Location
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
              ) {
                Text("Location:", fontSize = 12.sp, color = HomezyTextSecondary)
                Column(horizontalAlignment = Alignment.End) {
                  Text(currentLocation, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = HomezyText)
                  Text("($mockCoordinates)", fontSize = 11.sp, color = HomezyTextSecondary)
                }
              }

              // Worker
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Worker:", fontSize = 12.sp, color = HomezyTextSecondary)
                Text("${worker.workerName} (${worker.skill})", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = HomezyPrimary)
              }

              // Estimated Price
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Estimated Price:", fontSize = 12.sp, color = HomezyTextSecondary)
                Text("₹${worker.estimatedPrice}", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF15803D))
              }

              // Urgency
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Urgency:", fontSize = 12.sp, color = HomezyTextSecondary)
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = Color(0xFFFEE2E2)
                ) {
                  Text(
                    text = "Immediate Priority",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFDC2626),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(verticalAlignment = Alignment.Top) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = null,
              tint = Color(0xFFB45309),
              modifier = Modifier.size(16.dp).padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Cooperative members charge standardized rates with 0% surge surge gouging. ETA: ${worker.etaMinutes} mins.",
              fontSize = 11.sp,
              color = HomezyTextSecondary,
              lineHeight = 16.sp
            )
          }

          Spacer(modifier = Modifier.height(20.dp))

          // Actions
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedButton(
              onClick = { selectedWorkerForRequest = null },
              modifier = Modifier.weight(1f).height(44.dp),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("Cancel", fontSize = 13.sp, color = HomezyTextSecondary)
            }

            Button(
              onClick = {
                val newRequest = InstaHelpRequest(
                  id = "INSTA-${(1000..9999).random()}",
                  service = selectedService,
                  location = currentLocation,
                  searchRadiusKm = selectedRadiusKm,
                  workerId = worker.workerId,
                  workerName = worker.workerName,
                  workerSkill = worker.skill,
                  workerDistanceKm = worker.distanceKm,
                  workerEtaMinutes = worker.etaMinutes,
                  estimatedPrice = worker.estimatedPrice,
                  estimatedEarnings = worker.estimatedEarnings,
                  urgency = "Immediate / Priority 1",
                  status = InstaHelpStatus.PENDING,
                  timestamp = "Just now"
                )
                selectedWorkerForRequest = null
                activeDispatchedRequest = newRequest
                onDispatchConfirmed(newRequest)
              },
              modifier = Modifier
                .weight(1.6f)
                .height(44.dp)
                .testTag("confirm_emergency_request_button"),
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
            ) {
              Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Confirm Emergency Request", fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            }
          }
        }
      }
    }
  }
}
}
}

@Composable
private fun InstaHelpWorkerResultCard(
  worker: InstaHelpWorkerResult,
  onRequestInstaHelp: () -> Unit
) {
  HomezyCard(
    onClick = onRequestInstaHelp,
    modifier = Modifier
      .fillMaxWidth()
      .testTag("worker_result_card_${worker.workerName.lowercase().replace(" ", "_")}")
  ) {
    Column {
      // Header: Name & Verification
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(HomezyPrimaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = worker.workerName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
              fontWeight = FontWeight.Bold,
              color = HomezyPrimary,
              fontSize = 15.sp
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = worker.workerName,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = HomezyText
              )
              if (worker.isVerified) {
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = Color(0xFFDCFCE7)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = Icons.Default.Verified,
                      contentDescription = null,
                      tint = Color(0xFF15803D),
                      modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                      text = "Verified",
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF15803D)
                    )
                  }
                }
              }
            }
            Text(
              text = "Skill: ${worker.skill}",
              fontSize = 12.sp,
              color = HomezyTextSecondary,
              fontWeight = FontWeight.Medium
            )
          }
        }

        // ETA Badge
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFFEFF6FF),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.DirectionsRun, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "ETA: ${worker.etaMinutes} min",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1D4ED8)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Metrics Row: Distance, Availability, Estimated Price
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(HomezySurfaceVariant, RoundedCornerShape(8.dp))
          .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Distance
        Column {
          Text("Distance", fontSize = 10.sp, color = HomezyTextSecondary)
          Text(
            text = "${worker.distanceKm} km",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
        }

        Divider(modifier = Modifier.height(26.dp).width(1.dp), color = HomezyBorder)

        // Availability
        Column {
          Text("Availability", fontSize = 10.sp, color = HomezyTextSecondary)
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(Color(0xFF15803D))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = worker.availability,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF15803D)
            )
          }
        }

        Divider(modifier = Modifier.height(26.dp).width(1.dp), color = HomezyBorder)

        // Estimated Price
        Column {
          Text("Est. Price", fontSize = 10.sp, color = HomezyTextSecondary)
          Text(
            text = "₹${worker.estimatedPrice}",
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = HomezyPrimary
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Button: Request Insta Help
      Button(
        onClick = onRequestInstaHelp,
        modifier = Modifier
          .fillMaxWidth()
          .height(42.dp)
          .testTag("request_insta_help_btn_${worker.workerName.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = HomezyPrimary)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(Icons.Default.FlashOn, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Request Insta Help",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

@Composable
private fun ActiveInstaHelpTrackerView(
  request: InstaHelpRequest,
  onSimulateWorkerAccept: () -> Unit,
  onCancelRequest: () -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .padding(16.dp)
      .testTag("active_insta_help_tracker"),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      HomezyCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = if (request.status == InstaHelpStatus.PENDING) Color(0xFFFFFBEB) else Color(0xFFF0FDF4),
        borderColor = if (request.status == InstaHelpStatus.PENDING) Color(0xFFFDE68A) else Color(0xFFBBF7D0)
      ) {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(if (request.status == InstaHelpStatus.PENDING) Color(0xFFFEF3C7) else Color(0xFFDCFCE7)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (request.status == InstaHelpStatus.PENDING) Icons.Default.HourglassTop else Icons.Default.DirectionsRun,
              contentDescription = null,
              tint = if (request.status == InstaHelpStatus.PENDING) Color(0xFFB45309) else Color(0xFF15803D),
              modifier = Modifier.size(28.dp)
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = if (request.status == InstaHelpStatus.PENDING) "Insta Help Dispatched!" else "Worker Accepted & En Route!",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = HomezyText
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = if (request.status == InstaHelpStatus.PENDING)
              "Waiting for ${request.workerName} to confirm emergency arrival..."
            else
              "${request.workerName} is en route to ${request.location}. ETA: ${request.workerEtaMinutes} mins.",
            fontSize = 13.sp,
            color = HomezyTextSecondary,
            textAlign = TextAlign.Center
          )
        }
      }
    }

    // Emergency Summary Card
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Text("Request Summary", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = HomezyText)
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Service Issue:", fontSize = 12.sp, color = HomezyTextSecondary)
          Text(request.service, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Assigned Worker:", fontSize = 12.sp, color = HomezyTextSecondary)
          Text("${request.workerName} (${request.workerSkill})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = HomezyPrimary)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Estimated Price:", fontSize = 12.sp, color = HomezyTextSecondary)
          Text("₹${request.estimatedPrice}", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF15803D))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Urgency:", fontSize = 12.sp, color = HomezyTextSecondary)
          Text(request.urgency, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
        }
      }
    }

    // Prototype interactive simulation button
    if (request.status == InstaHelpStatus.PENDING) {
      item {
        HomezyButton(
          text = "Simulate Worker Acceptance (Demo)",
          onClick = onSimulateWorkerAccept,
          variant = ButtonVariant.OUTLINE,
          icon = Icons.Default.CheckCircle,
          fullWidth = true
        )
      }
    }

    item {
      OutlinedButton(
        onClick = onCancelRequest,
        modifier = Modifier.fillMaxWidth().height(44.dp),
        shape = RoundedCornerShape(8.dp)
      ) {
        Text("Return to Insta Help Search", fontSize = 13.sp, color = HomezyTextSecondary)
      }
    }
  }
}
