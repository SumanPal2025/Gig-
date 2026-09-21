package com.example.ui.admin

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
  onNavigateToSection: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val hourlyBookings = listOf(
    "8AM" to 8f,
    "10AM" to 19f,
    "12PM" to 28f,
    "2PM" to 16f,
    "4PM" to 24f,
    "6PM" to 31f,
    "8PM" to 12f
  )
  val maxBooking = hourlyBookings.maxOf { it.second }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("admin_dashboard_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top Federation Overview Banner
    item {
      HomezyCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = HomezyCard,
        elevation = 1.dp
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Bengaluru Cooperative Federation",
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyText
            )
            Text(
              text = "Central dispatch, fair lead allocation & worker welfare monitoring",
              fontSize = 12.sp,
              color = HomezyTextSecondary
            )
          }

          HomezyBadge(
            text = "FEDERATION LIVE",
            containerColor = Color(0xFFDCFCE7),
            contentColor = Color(0xFF15803D),
            icon = Icons.Default.Circle
          )
        }
      }
    }

    // 6 Dashboard Statistics Grid
    item {
      Text(
        text = "Key Operational Statistics",
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = HomezyText
      )
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        StatCard(
          title = "Total Workers",
          value = "180",
          subtext = "+12 onboarded this week",
          icon = Icons.Default.People,
          modifier = Modifier.weight(1f)
        )
        StatCard(
          title = "Active Workers",
          value = "142",
          badgeText = "78.8% Active",
          isPositive = true,
          icon = Icons.Default.CheckCircleOutline,
          modifier = Modifier.weight(1f)
        )
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        StatCard(
          title = "Today's Bookings",
          value = "84",
          subtext = "Peak hours active",
          icon = Icons.Default.Assignment,
          modifier = Modifier.weight(1f)
        )
        StatCard(
          title = "Completed Jobs",
          value = "68",
          badgeText = "81% Done",
          isPositive = true,
          icon = Icons.Default.TaskAlt,
          modifier = Modifier.weight(1f)
        )
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        StatCard(
          title = "Worker Earnings",
          value = "₹64,280",
          subtext = "95% Direct Member Payout",
          icon = Icons.Default.AccountBalanceWallet,
          modifier = Modifier.weight(1f)
        )
        StatCard(
          title = "Customer Satisfaction",
          value = "4.91 / 5.0",
          badgeText = "High Trust",
          isPositive = true,
          icon = Icons.Default.SentimentSatisfiedAlt,
          modifier = Modifier.weight(1f)
        )
      }
    }

    // Interactive Demo Charts
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Today's Hourly Demand Curve",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = HomezyText
          )
          HomezyBadge(
            text = "Real-Time Telemetry",
            containerColor = HomezyPrimaryContainer,
            contentColor = HomezyPrimary
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
        ) {
          Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height - 24.dp.toPx()
            val stepX = width / (hourlyBookings.size - 1)

            val path = Path()
            hourlyBookings.forEachIndexed { i, (_, count) ->
              val x = i * stepX
              val y = height - (count / maxBooking) * (height - 10.dp.toPx())
              if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }

            drawPath(
              path = path,
              color = HomezyPrimary,
              style = Stroke(width = 3.dp.toPx())
            )

            // Draw data points
            hourlyBookings.forEachIndexed { i, (_, count) ->
              val x = i * stepX
              val y = height - (count / maxBooking) * (height - 10.dp.toPx())
              drawCircle(
                color = HomezyAccent,
                radius = 4.dp.toPx(),
                center = Offset(x, y)
              )
            }
          }

          // X-Axis time labels
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            hourlyBookings.forEach { (time, _) ->
              Text(
                text = time,
                fontSize = 10.sp,
                color = HomezyTextSecondary
              )
            }
          }
        }
      }
    }

    // Cooperative Innovation Banner: Fair Allocation & Welfare
    item {
      HomezyCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = HomezySurfaceVariant
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Balance, contentDescription = null, tint = HomezyPrimary, modifier = Modifier.size(24.dp))
          Spacer(modifier = Modifier.width(10.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Algorithmic Fairness Gini Index: 0.16",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = HomezyPrimary
            )
            Text(
              text = "Optimal equity reached. Work opportunities are distributed evenly across all verified workers without gig starvation.",
              fontSize = 12.sp,
              color = HomezyText,
              lineHeight = 16.sp
            )
          }
        }
        Spacer(modifier = Modifier.height(10.dp))
        HomezyButton(
          text = "Open Fair Allocation Monitor",
          onClick = { onNavigateToSection("/admin/fair-allocation") },
          variant = ButtonVariant.OUTLINE,
          icon = Icons.Default.Analytics,
          fullWidth = true
        )
      }
    }
  }
}
