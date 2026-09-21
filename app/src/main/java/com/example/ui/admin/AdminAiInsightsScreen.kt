package com.example.ui.admin

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.DemandDataPoint
import com.example.data.ExplainableWeightedMovingAverageForecaster
import com.example.data.ServiceDemandForecast
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun AdminAiInsightsScreen(
  modifier: Modifier = Modifier
) {
  val forecaster = remember { ExplainableWeightedMovingAverageForecaster() }
  val forecastResponse = remember { forecaster.generateForecast() }
  val services = forecastResponse.services

  var selectedServiceTab by remember { mutableStateOf("All Services") }
  var snackbarMsg by remember { mutableStateOf<String?>(null) }
  var reallocatingService by remember { mutableStateOf<ServiceDemandForecast?>(null) }

  // Reallocation Confirmation Modal Dialog
  reallocatingService?.let { s ->
    Dialog(onDismissRequest = { reallocatingService = null }) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = HomezyCard,
        shadowElevation = 8.dp,
        modifier = Modifier
          .fillMaxWidth()
          .padding(8.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          // Modal Header
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(HomezyPrimaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Campaign,
                  contentDescription = null,
                  tint = HomezyPrimary,
                  modifier = Modifier.size(22.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Workforce Reallocation",
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = HomezyText
                )
                Text(
                  text = "${s.serviceName} Guild",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium,
                  color = HomezyPrimary
                )
              }
            }
            IconButton(
              onClick = { reallocatingService = null },
              modifier = Modifier.size(32.dp)
            ) {
              Icon(
                Icons.Default.Close,
                contentDescription = "Close dialog",
                tint = HomezyTextSecondary
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
          Divider(color = HomezyBorder)
          Spacer(modifier = Modifier.height(14.dp))

          // Reallocation Stats Breakdown Box
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = HomezySurfaceVariant,
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Current Active Workers:", fontSize = 12.sp, color = HomezyTextSecondary)
                Text("${s.availableWorkers} on duty", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = HomezyText)
              }
              Spacer(modifier = Modifier.height(8.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Recommended Target:", fontSize = 12.sp, color = HomezyTextSecondary)
                Text("${s.recommendedWorkers} Workers", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = HomezyPrimary)
              }
              Spacer(modifier = Modifier.height(8.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Reallocation Delta:", fontSize = 12.sp, color = HomezyTextSecondary)
                Text("+${s.reallocationDelta} Standby Workers", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF15803D))
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(verticalAlignment = Alignment.Top) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = null,
              tint = HomezyPrimary,
              modifier = Modifier.size(16.dp).padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "An automated voluntary shift surge alert will be broadcast to qualified ${s.serviceName} cooperative members currently on standby.",
              fontSize = 11.sp,
              color = HomezyTextSecondary,
              lineHeight = 16.sp
            )
          }

          Spacer(modifier = Modifier.height(20.dp))

          // Action Buttons: Cancel and Broadcast Alert
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedButton(
              onClick = { reallocatingService = null },
              modifier = Modifier
                .weight(1f)
                .height(44.dp),
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
              Text(
                text = "Cancel",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = HomezyTextSecondary
              )
            }

            Button(
              onClick = {
                snackbarMsg = "Shift surge notification broadcasted to +${s.reallocationDelta} ${s.serviceName} cooperative members."
                reallocatingService = null
              },
              modifier = Modifier
                .weight(1.4f)
                .height(44.dp)
                .testTag("confirm_reallocation_btn"),
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = HomezyPrimary)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Send,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Broadcast Alert",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  maxLines = 1
                )
              }
            }
          }
        }
      }
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("admin_ai_insights_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top Title & Prototype Badge
    item {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "AI Demand Forecast",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          HomezyBadge(
            text = "Prototype AI Forecast",
            containerColor = Color(0xFFFEF3C7),
            contentColor = Color(0xFFB45309)
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Historical booking velocity & predictive workforce demand modeling across cooperative guilds",
          fontSize = 13.sp,
          color = HomezyTextSecondary
        )
      }
    }

    // Disclaimer Card
    item {
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFFFFBEB),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
        modifier = Modifier.fillMaxWidth().testTag("ai_forecast_disclaimer_card")
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.Top
        ) {
          Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = Color(0xFFB45309),
            modifier = Modifier.size(20.dp).padding(top = 1.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Prototype Notice",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = Color(0xFF92400E)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = forecastResponse.disclaimer,
              fontSize = 11.sp,
              color = Color(0xFF78350F),
              lineHeight = 16.sp
            )
          }
        }
      }
    }

    // Demand Forecast Summary Cards (AC Service, Electrician, Plumber)
    item {
      Text(
        text = "Service Demand Overview",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
    }

    item {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        services.forEach { forecast ->
          ServiceForecastSummaryCard(
            forecast = forecast,
            onSelectService = { selectedServiceTab = forecast.serviceName }
          )
        }
      }
    }

    // Interactive Chart: Past Demand -> Predicted Demand
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth().testTag("demand_forecast_chart_card")) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Past Demand → Predicted Demand",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = HomezyText
            )
            Text(
              text = "Historical vs. Projected weekly job requests",
              fontSize = 11.sp,
              color = HomezyTextSecondary
            )
          }
          HomezyBadge(
            text = "4 Wk History • 3 Wk Forecast",
            containerColor = HomezyPrimaryContainer,
            contentColor = HomezyPrimary
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Service Selector Chips
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          val tabs = listOf("All Services", "AC Service", "Electrician", "Plumber")
          tabs.forEach { tab ->
            val isSelected = selectedServiceTab == tab
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = if (isSelected) HomezyPrimary else HomezySurfaceVariant,
              modifier = Modifier
                .clickable { selectedServiceTab = tab }
                .testTag("chart_tab_${tab.lowercase().replace(" ", "_")}")
            ) {
              Text(
                text = tab,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else HomezyText,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Canvas Line Chart
        DemandForecastLineChart(
          services = services,
          selectedFilter = selectedServiceTab
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Legend
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(12.dp, 3.dp)
                .background(HomezyPrimary)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Past Demand (Solid)", fontSize = 10.sp, color = HomezyTextSecondary, fontWeight = FontWeight.Medium)
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(12.dp, 3.dp)
                .background(Color(0xFFE11D48))
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Predicted Demand (Dashed)", fontSize = 10.sp, color = HomezyTextSecondary, fontWeight = FontWeight.Medium)
          }
        }
      }
    }

    // Workforce Recommendation Section
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Workforce Recommendations",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Text(
            text = "Available vs. Recommended staffing to maintain service SLA",
            fontSize = 11.sp,
            color = HomezyTextSecondary
          )
        }
      }
    }

    items(services) { serviceItem ->
      WorkforceRecommendationCard(
        forecast = serviceItem,
        onReallocateClick = { reallocatingService = serviceItem }
      )
    }

    // Replaceable Architecture Note
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Engineering, contentDescription = null, tint = HomezyPrimary, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Replaceable Forecasting Engine Architecture",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = HomezyText
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "The system is decoupled via the 'DemandForecastingEngine' interface. The current lightweight weighted moving average (WMA) prototype is designed for transparent explanation and can be seamlessly upgraded to TensorFlow Lite or ARIMA models without UI changes.",
          fontSize = 11.sp,
          color = HomezyTextSecondary,
          lineHeight = 16.sp
        )
      }
    }
  }

  // Snackbar Notification
  snackbarMsg?.let { msg ->
    LaunchedEffect(msg) {
      kotlinx.coroutines.delay(3500)
      snackbarMsg = null
    }
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      contentAlignment = Alignment.BottomCenter
    ) {
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF1F2937),
        shadowElevation = 6.dp
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF34D399), modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(msg, color = Color.White, fontSize = 12.sp)
        }
      }
    }
  }
}

@Composable
private fun ServiceForecastSummaryCard(
  forecast: ServiceDemandForecast,
  onSelectService: () -> Unit
) {
  val (badgeBg, badgeText, icon) = when (forecast.demandLevel) {
    "High" -> Triple(Color(0xFFFEE2E2), Color(0xFFDC2626), Icons.Default.TrendingUp)
    "Medium" -> Triple(Color(0xFFFEF3C7), Color(0xFFD97706), Icons.Default.TrendingFlat)
    else -> Triple(Color(0xFFDCFCE7), Color(0xFF15803D), Icons.Default.TrendingDown)
  }

  HomezyCard(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onSelectService() }
      .testTag("forecast_card_${forecast.serviceName.lowercase().replace(" ", "_")}")
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)
      ) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(HomezyPrimaryContainer),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = when {
              forecast.serviceName.contains("AC") -> Icons.Default.AcUnit
              forecast.serviceName.contains("Electr") -> Icons.Default.ElectricBolt
              else -> Icons.Default.Plumbing
            },
            contentDescription = null,
            tint = HomezyPrimary,
            modifier = Modifier.size(20.dp)
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
          Text(
            text = forecast.serviceName,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = HomezyText
          )
          Text(
            text = "Historical: ${forecast.historicalDemandPoints.last().count} jobs → Predicted: ${forecast.predictedDemandPoints.last().count} jobs",
            fontSize = 11.sp,
            color = HomezyTextSecondary
          )
        }
      }

      Surface(
        shape = RoundedCornerShape(6.dp),
        color = badgeBg
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = badgeText,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = forecast.demandTrendLabel,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = badgeText
          )
        }
      }
    }
  }
}

@Composable
private fun DemandForecastLineChart(
  services: List<ServiceDemandForecast>,
  selectedFilter: String
) {
  val targetServices = if (selectedFilter == "All Services") {
    services
  } else {
    services.filter { it.serviceName == selectedFilter }
  }

  val allPointsCount = 7 // 4 historical + 3 predicted
  val labels = listOf("W-3", "W-2", "W-1", "Current", "W+1 (Est)", "W+2 (Est)", "W+3 (Est)")
  val maxVal = 90f

  val colors = mapOf(
    "AC Service" to HomezyPrimary,
    "Electrician" to HomezySecondary,
    "Plumber" to Color(0xFF0284C7)
  )

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(150.dp)
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val width = size.width
      val height = size.height - 24.dp.toPx()
      val stepX = width / (allPointsCount - 1)
      val splitX = 3 * stepX // Index 3 is "Current Week"

      // Vertical threshold line separating historical from forecast
      drawLine(
        color = Color(0xFFCBD5E1),
        start = Offset(splitX, 0f),
        end = Offset(splitX, height),
        strokeWidth = 1.5.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
      )

      targetServices.forEach { s ->
        val lineColor = colors[s.serviceName] ?: HomezyPrimary
        val combinedPoints = s.historicalDemandPoints.map { it.count } + s.predictedDemandPoints.map { it.count }

        // 1. Draw Historical Solid Path (points 0..3)
        val histPath = Path()
        for (i in 0..3) {
          val x = i * stepX
          val y = height - (combinedPoints[i] / maxVal) * (height - 12.dp.toPx())
          if (i == 0) histPath.moveTo(x, y) else histPath.lineTo(x, y)
        }
        drawPath(
          path = histPath,
          color = lineColor,
          style = Stroke(width = 3.dp.toPx())
        )

        // Draw Historical Dots
        for (i in 0..3) {
          val x = i * stepX
          val y = height - (combinedPoints[i] / maxVal) * (height - 12.dp.toPx())
          drawCircle(
            color = lineColor,
            radius = 3.5.dp.toPx(),
            center = Offset(x, y)
          )
        }

        // 2. Draw Predicted Dashed Path (points 3..6)
        val predPath = Path()
        predPath.moveTo(3 * stepX, height - (combinedPoints[3] / maxVal) * (height - 12.dp.toPx()))
        for (i in 4 until combinedPoints.size) {
          val x = i * stepX
          val y = height - (combinedPoints[i] / maxVal) * (height - 12.dp.toPx())
          predPath.lineTo(x, y)
        }
        drawPath(
          path = predPath,
          color = Color(0xFFE11D48),
          style = Stroke(
            width = 3.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
          )
        )

        // Draw Predicted Dots
        for (i in 4 until combinedPoints.size) {
          val x = i * stepX
          val y = height - (combinedPoints[i] / maxVal) * (height - 12.dp.toPx())
          drawCircle(
            color = Color(0xFFE11D48),
            radius = 4.dp.toPx(),
            center = Offset(x, y)
          )
        }
      }
    }

    // X-Axis Labels
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.BottomCenter),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      labels.forEachIndexed { idx, label ->
        Text(
          text = label,
          fontSize = 9.sp,
          color = if (idx >= 4) Color(0xFFE11D48) else HomezyTextSecondary,
          fontWeight = if (idx >= 4) FontWeight.Bold else FontWeight.Normal
        )
      }
    }
  }
}

@Composable
private fun WorkforceRecommendationCard(
  forecast: ServiceDemandForecast,
  onReallocateClick: () -> Unit
) {
  val isSurge = forecast.reallocationDelta > 0

  HomezyCard(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("workforce_rec_${forecast.serviceName.lowercase().replace(" ", "_")}")
  ) {
    Column {
      // Header Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = forecast.serviceName,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = HomezyText
          )
          Spacer(modifier = Modifier.width(8.dp))
          HomezyBadge(
            text = "Predicted: ${forecast.demandLevel}",
            containerColor = if (forecast.demandLevel == "High") Color(0xFFFEE2E2) else Color(0xFFFEF3C7),
            contentColor = if (forecast.demandLevel == "High") Color(0xFFDC2626) else Color(0xFFD97706)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // 3-Column Metrics Box
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(HomezySurfaceVariant, RoundedCornerShape(8.dp))
          .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text("Available Workers", fontSize = 11.sp, color = HomezyTextSecondary)
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "${forecast.availableWorkers}",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = HomezyText
          )
        }

        Divider(
          color = HomezyBorder,
          modifier = Modifier
            .height(32.dp)
            .width(1.dp)
        )

        Column {
          Text("Recommended Workers", fontSize = 11.sp, color = HomezyTextSecondary)
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "${forecast.recommendedWorkers}",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (isSurge) HomezyPrimary else Color(0xFF15803D)
          )
        }

        Divider(
          color = HomezyBorder,
          modifier = Modifier
            .height(32.dp)
            .width(1.dp)
        )

        Column {
          Text("Status", fontSize = 11.sp, color = HomezyTextSecondary)
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = if (isSurge) "+${forecast.reallocationDelta} Deficit" else "Balanced",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSurge) Color(0xFFDC2626) else Color(0xFF15803D)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Recommendation text & Action Button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier.weight(1f).padding(end = 8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = if (isSurge) Icons.Default.Campaign else Icons.Default.CheckCircle,
            contentDescription = null,
            tint = if (isSurge) HomezyPrimary else Color(0xFF15803D),
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = forecast.recommendationText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = HomezyText
          )
        }

        if (isSurge) {
          Button(
            onClick = onReallocateClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = HomezyPrimary),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
            modifier = Modifier
              .height(34.dp)
              .testTag("reallocate_btn_${forecast.serviceName.lowercase().replace(" ", "_")}")
          ) {
            Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Reallocate", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        } else {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFFDCFCE7)
          ) {
            Text(
              text = "Balanced ✓",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF15803D),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }
    }
  }
}
