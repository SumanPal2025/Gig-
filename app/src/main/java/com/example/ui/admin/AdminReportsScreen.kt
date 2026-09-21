package com.example.ui.admin

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun AdminReportsScreen(
  modifier: Modifier = Modifier
) {
  val bookingsOverTime = listOf(
    "Mon" to 42f,
    "Tue" to 58f,
    "Wed" to 64f,
    "Thu" to 71f,
    "Fri" to 88f,
    "Sat" to 112f,
    "Sun" to 95f
  )
  val maxBookings = bookingsOverTime.maxOf { it.second }

  val popularServices = listOf(
    Triple("AC Servicing & Repair", 34, HomezyPrimary),
    Triple("Electrical Diagnostics", 28, HomezySecondary),
    Triple("Plumbing & Pipe Fixing", 22, HomezyAccent),
    Triple("Deep Home Cleaning", 16, Color(0xFF0284C7))
  )

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("admin_reports_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top Title
    item {
      Column {
        Text(
          text = "Cooperative Analytics & Reports",
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Text(
          text = "Transparent performance metrics: bookings, worker utilization, earnings equity, and customer satisfaction",
          fontSize = 13.sp,
          color = HomezyTextSecondary
        )
      }
    }

    // Chart 1: Bookings Over Time
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text("Bookings Over Time", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = HomezyText)
            Text("Weekly volume (Mon - Sun)", fontSize = 11.sp, color = HomezyTextSecondary)
          }
          HomezyBadge(
            text = "Total: 530 Jobs",
            containerColor = HomezyPrimaryContainer,
            contentColor = HomezyPrimary
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Canvas Line Chart
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
        ) {
          Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height - 24.dp.toPx()
            val stepX = width / (bookingsOverTime.size - 1)

            val path = Path()
            bookingsOverTime.forEachIndexed { i, (_, count) ->
              val x = i * stepX
              val y = height - (count / maxBookings) * (height - 10.dp.toPx())
              if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }

            drawPath(
              path = path,
              color = HomezyPrimary,
              style = Stroke(width = 3.dp.toPx())
            )

            // Draw Dots
            bookingsOverTime.forEachIndexed { i, (_, count) ->
              val x = i * stepX
              val y = height - (count / maxBookings) * (height - 10.dp.toPx())
              drawCircle(
                color = HomezySecondary,
                radius = 4.dp.toPx(),
                center = Offset(x, y)
              )
            }
          }

          // X-Axis day labels
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            bookingsOverTime.forEach { (day, _) ->
              Text(
                text = day,
                fontSize = 10.sp,
                color = HomezyTextSecondary
              )
            }
          }
        }
      }
    }

    // Chart 2: Worker Utilization & Fair Rotation
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Worker Utilization & Capacity", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = HomezyText)
          HomezyBadge(text = "Optimal 78.8%", containerColor = Color(0xFFDCFCE7), contentColor = Color(0xFF15803D))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Multi-segment progress bar for utilization
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .background(Color(0xFFE5E7EB), RoundedCornerShape(6.dp))
        ) {
          Box(
            modifier = Modifier
              .weight(0.55f)
              .fillMaxHeight()
              .background(HomezyPrimary, RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp))
          )
          Box(
            modifier = Modifier
              .weight(0.24f)
              .fillMaxHeight()
              .background(HomezySecondary)
          )
          Box(
            modifier = Modifier
              .weight(0.21f)
              .fillMaxHeight()
              .background(Color(0xFFD1D5DB), RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp))
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          UtilizationLegendItem(color = HomezyPrimary, label = "Active on Job (55%)")
          UtilizationLegendItem(color = HomezySecondary, label = "Standby / Transit (24%)")
          UtilizationLegendItem(color = Color(0xFF9CA3AF), label = "Rest / Off-Duty (21%)")
        }
      }
    }

    // Chart 3: Transparent Cooperative Earnings Split
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Text("Earnings Transparency Breakdown", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = HomezyText)
        Text("Gross GMV: ₹64,280 today across 68 jobs", fontSize = 11.sp, color = HomezyTextSecondary)

        Spacer(modifier = Modifier.height(12.dp))

        EarningsSplitBar(label = "Worker Direct Payout (95%)", amount = "₹61,066", percent = 95, color = Color(0xFF16A34A))
        Spacer(modifier = Modifier.height(8.dp))
        EarningsSplitBar(label = "Welfare Reserve Pool (3%)", amount = "₹1,928", percent = 3, color = Color(0xFF2563EB))
        Spacer(modifier = Modifier.height(8.dp))
        EarningsSplitBar(label = "Federation Platform Cap (2%)", amount = "₹1,286", percent = 2, color = Color(0xFF4B5563))
      }
    }

    // Chart 4: Popular Services
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Text("Popular Services Distribution", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = HomezyText)
        Spacer(modifier = Modifier.height(12.dp))

        popularServices.forEach { (name, share, color) ->
          Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(name, fontSize = 12.sp, color = HomezyText, fontWeight = FontWeight.Medium)
              Text("$share%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
              progress = { share / 100f },
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
              color = color,
              trackColor = HomezySurfaceVariant,
            )
          }
        }
      }
    }

    // Chart 5: Customer Satisfaction
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text("Customer Satisfaction", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = HomezyText)
            Text("Based on 512 verified customer reviews", fontSize = 11.sp, color = HomezyTextSecondary)
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = null, tint = HomezySecondary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("4.91 / 5.0", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = HomezyText)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        SatisfactionBar("5 Stars ★★★★★", 88)
        SatisfactionBar("4 Stars ★★★★☆", 10)
        SatisfactionBar("3 Stars ★★★☆☆", 2)
      }
    }
  }
}

@Composable
private fun UtilizationLegendItem(color: Color, label: String) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Box(modifier = Modifier.size(8.dp).background(color, RoundedCornerShape(2.dp)))
    Spacer(modifier = Modifier.width(4.dp))
    Text(label, fontSize = 10.sp, color = HomezyTextSecondary)
  }
}

@Composable
private fun EarningsSplitBar(label: String, amount: String, percent: Int, color: Color) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(modifier = Modifier.size(10.dp).background(color, RoundedCornerShape(2.dp)))
      Spacer(modifier = Modifier.width(6.dp))
      Text(label, fontSize = 12.sp, color = HomezyText)
    }
    Text(amount, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = color)
  }
}

@Composable
private fun SatisfactionBar(label: String, percent: Int) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(label, fontSize = 11.sp, color = HomezyTextSecondary, modifier = Modifier.width(100.dp))
    LinearProgressIndicator(
      progress = { percent / 100f },
      modifier = Modifier
        .weight(1f)
        .height(6.dp),
      color = HomezySecondary,
      trackColor = HomezySurfaceVariant
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text("$percent%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = HomezyText)
  }
}
