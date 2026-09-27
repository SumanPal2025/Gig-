package com.example.ui.worker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.HomezyCard
import com.example.ui.theme.*

enum class EarningsPeriod(val label: String) {
  TODAY("Today's Earnings"),
  THIS_WEEK("This Week"),
  THIS_MONTH("This Month")
}

data class EarningsSummary(
  val period: EarningsPeriod,
  val amount: Int,
  val customerPayment: Int,
  val workerEarnings: Int,
  val cooperativeContribution: Int,
  val platformFee: Int
)

@Composable
fun WorkerEarningsScreen(
  modifier: Modifier = Modifier
) {
  var selectedPeriod by remember { mutableStateOf(EarningsPeriod.TODAY) }
  var showBreakdown by remember { mutableStateOf(false) }

  val summaries = listOf(
    EarningsSummary(EarningsPeriod.TODAY, 1420, 1495, 1420, 45, 30),
    EarningsSummary(EarningsPeriod.THIS_WEEK, 9357, 9850, 9357, 295, 198),
    EarningsSummary(EarningsPeriod.THIS_MONTH, 40470, 42600, 40470, 1278, 852)
  )

  val currentSummary = summaries.first { it.period == selectedPeriod }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("worker_earnings_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header
    item {
      Text(
        text = "Earnings",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = "Cooperative payout summary",
        fontSize = 13.sp,
        color = HomezyTextSecondary
      )
    }

    // Initial Screen: Today's Earnings, This Week, This Month Cards
    item {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        summaries.forEach { item ->
          val isSelected = selectedPeriod == item.period
          HomezyCard(
            onClick = { selectedPeriod = item.period },
            modifier = Modifier.fillMaxWidth(),
            borderColor = if (isSelected) HomezyPrimary else HomezyBorder
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = item.period.label,
                  fontSize = 13.sp,
                  color = if (isSelected) HomezyPrimary else HomezyTextSecondary,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = "₹${item.amount}",
                  fontSize = 20.sp,
                  fontWeight = FontWeight.Bold,
                  color = HomezyText
                )
              }

              Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (isSelected) HomezyPrimaryContainer else HomezySurfaceVariant
              ) {
                Text(
                  text = if (isSelected) "Selected" else "Select",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = if (isSelected) HomezyPrimary else HomezyTextSecondary,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }
        }
      }
    }

    // Simple "View Breakdown" Section
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth().testTag("earnings_breakdown_card")) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { showBreakdown = !showBreakdown }
            .padding(vertical = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "View Breakdown (${selectedPeriod.label})",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyPrimary
          )
          Text(
            text = if (showBreakdown) "▲ Hide" else "▼ Expand",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = HomezyPrimary
          )
        }

        AnimatedVisibility(visible = showBreakdown) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 12.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(HomezySurfaceVariant)
              .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text("Customer Payment", fontSize = 13.sp, color = HomezyTextSecondary)
              Text("₹${currentSummary.customerPayment}", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = HomezyText)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text("Worker Earnings", fontSize = 13.sp, color = HomezyTextSecondary)
              Text("₹${currentSummary.workerEarnings}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = HomezySecondary)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text("Cooperative Contribution", fontSize = 13.sp, color = HomezyTextSecondary)
              Text("₹${currentSummary.cooperativeContribution}", fontSize = 13.sp, color = HomezyText)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text("Platform Fee", fontSize = 13.sp, color = HomezyTextSecondary)
              Text("₹${currentSummary.platformFee}", fontSize = 13.sp, color = HomezyText)
            }
          }
        }
      }
    }
  }
}
