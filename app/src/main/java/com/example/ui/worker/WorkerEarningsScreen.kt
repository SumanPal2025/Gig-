package com.example.ui.worker

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*

enum class EarningsPeriod(val label: String) {
  TODAY("Today"),
  THIS_WEEK("This Week"),
  THIS_MONTH("This Month")
}

data class EarningsSummary(
  val customerPaid: Int,
  val workerEarnings: Int,
  val cooperativeContribution: Int,
  val platformFee: Int,
  val jobsCount: Int,
  val hoursWorked: String,
  val savedVsCorpApps: Int
)

@Composable
fun WorkerEarningsScreen(
  modifier: Modifier = Modifier
) {
  var selectedPeriod by remember { mutableStateOf(EarningsPeriod.TODAY) }

  val summaryData = when (selectedPeriod) {
    EarningsPeriod.TODAY -> EarningsSummary(
      customerPaid = 1495,
      workerEarnings = 1420,
      cooperativeContribution = 45,
      platformFee = 30,
      jobsCount = 3,
      hoursWorked = "5.5 hrs",
      savedVsCorpApps = 375
    )
    EarningsPeriod.THIS_WEEK -> EarningsSummary(
      customerPaid = 9850,
      workerEarnings = 9357,
      cooperativeContribution = 295,
      platformFee = 198,
      jobsCount = 18,
      hoursWorked = "34.0 hrs",
      savedVsCorpApps = 2462
    )
    EarningsPeriod.THIS_MONTH -> EarningsSummary(
      customerPaid = 42600,
      workerEarnings = 40470,
      cooperativeContribution = 1278,
      platformFee = 852,
      jobsCount = 74,
      hoursWorked = "142.0 hrs",
      savedVsCorpApps = 10650
    )
  }

  val dailyEarnings = listOf(
    "Mon" to 1200f,
    "Tue" to 1550f,
    "Wed" to 980f,
    "Thu" to 1820f,
    "Fri" to 1450f,
    "Sat" to 2100f,
    "Sun" to 1420f
  )
  val maxEarning = dailyEarnings.maxOf { it.second }

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
      Column {
        Text(
          text = "Earnings & Settlements",
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Text(
          text = "Transparent co-op payout breakdown: You keep 95% of every rupee",
          fontSize = 13.sp,
          color = HomezyTextSecondary
        )
      }
    }

    // Period Switcher Tabs: Today | This Week | This Month
    item {
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = HomezySurfaceVariant,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(modifier = Modifier.padding(4.dp)) {
          EarningsPeriod.values().forEach { period ->
            val isSelected = selectedPeriod == period
            val bgColor by animateColorAsState(
              targetValue = if (isSelected) HomezyCard else Color.Transparent,
              animationSpec = tween(150),
              label = "period_bg"
            )
            val textColor by animateColorAsState(
              targetValue = if (isSelected) HomezyPrimary else HomezyTextSecondary,
              animationSpec = tween(150),
              label = "period_text"
            )

            Surface(
              shape = RoundedCornerShape(9.dp),
              color = bgColor,
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(9.dp))
                .clickable { selectedPeriod = period }
            ) {
              Box(
                modifier = Modifier.padding(vertical = 9.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = period.label,
                  fontSize = 13.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = textColor
                )
              }
            }
          }
        }
      }
    }

    // Hero Take-Home Earnings Card
    item {
      HomezyCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = HomezyPrimary,
        elevation = 3.dp
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "${selectedPeriod.label}'s Net Earnings",
              color = Color.White.copy(alpha = 0.85f),
              fontSize = 13.sp
            )
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = HomezyAccent
            ) {
              Text(
                text = "${summaryData.jobsCount} Jobs • 95% Share",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = HomezyText,
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "₹${summaryData.workerEarnings}",
            fontSize = 34.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
          )

          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Instant daily payout to linked bank A/c (SBI ...4819)",
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.75f)
          )
        }
      }
    }

    // 4-PART TRANSPARENT EARNINGS BREAKDOWN
    item {
      Text(
        text = "Fair Share Breakdown (${selectedPeriod.label})",
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
    }

    item {
      HomezyCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
      ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          // 1. Customer Paid (Gross)
          BreakdownRow(
            title = "Customer Paid (Gross Fare)",
            amount = "₹${summaryData.customerPaid}",
            subtitle = "Total collected from customers before deductions",
            icon = Icons.Default.Payments,
            iconTint = HomezyText,
            isHighlight = false
          )

          HorizontalDivider(color = HomezyBorder)

          // 2. Worker Earnings (95%)
          BreakdownRow(
            title = "Worker Earnings (95%)",
            amount = "₹${summaryData.workerEarnings}",
            subtitle = "Direct take-home credited to your wallet",
            icon = Icons.Default.AccountBalanceWallet,
            iconTint = HomezyPrimary,
            isHighlight = true
          )

          HorizontalDivider(color = HomezyBorder)

          // 3. Cooperative Contribution (3%)
          BreakdownRow(
            title = "Cooperative Contribution (3%)",
            amount = "₹${summaryData.cooperativeContribution}",
            subtitle = "Pooled for your insurance, pension & emergency fund",
            icon = Icons.Default.HealthAndSafety,
            iconTint = Color(0xFFD97706),
            isHighlight = false
          )

          HorizontalDivider(color = HomezyBorder)

          // 4. Platform/Service Fee (2%)
          BreakdownRow(
            title = "Platform / Service Fee (2%)",
            amount = "₹${summaryData.platformFee}",
            subtitle = "Non-profit server maintenance & app operations",
            icon = Icons.Default.Dns,
            iconTint = HomezyTextSecondary,
            isHighlight = false
          )
        }
      }
    }

    // Cooperative Fair Share Comparison Card
    item {
      HomezyCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color(0xFFF0FDF4),
        borderColor = Color(0xFFBBF7D0)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Savings,
            contentDescription = null,
            tint = Color(0xFF15803D),
            modifier = Modifier.size(26.dp)
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = "₹${summaryData.savedVsCorpApps} Saved vs 30% Commercial Apps",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = Color(0xFF15803D)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Corporate gig platforms charge 25%–30% commissions. HOMEZY's 5% co-op model puts ₹${summaryData.savedVsCorpApps} extra directly in your pocket for ${selectedPeriod.label.lowercase()}.",
              fontSize = 12.sp,
              color = Color(0xFF14532D),
              lineHeight = 16.sp
            )
          }
        }
      }
    }

    // Weekly Trend Chart
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "7-Day Earnings Trend",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = HomezyText
          )
          Text(
            text = "Avg ₹1,514/day",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyPrimary
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
        ) {
          Canvas(modifier = Modifier.fillMaxSize()) {
            val barWidth = size.width / (dailyEarnings.size * 2.2f)
            dailyEarnings.forEachIndexed { index, (day, amount) ->
              val barHeight = (amount / maxEarning) * (size.height - 26.dp.toPx())
              val x = index * (size.width / dailyEarnings.size) + (barWidth / 2)
              val y = size.height - 20.dp.toPx() - barHeight

              // Bar
              drawRoundRect(
                color = if (day == "Sun") HomezyPrimary else HomezySecondary.copy(alpha = 0.6f),
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx(), 6.dp.toPx())
              )
            }
          }

          // Day labels
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            dailyEarnings.forEach { (day, _) ->
              Text(
                text = day,
                fontSize = 11.sp,
                fontWeight = if (day == "Sun") FontWeight.Bold else FontWeight.Normal,
                color = if (day == "Sun") HomezyPrimary else HomezyTextSecondary
              )
            }
          }
        }
      }
    }

    // Recent Settlements List
    item {
      Text(
        text = "Recent Bank Settlements",
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = HomezyText
      )
    }

    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        SettlementRow("Today, 20 Sep (Instant)", "₹1,420.00", "Direct UPI to SBI A/c ...4819", true)
        HorizontalDivider(color = HomezyBorder, modifier = Modifier.padding(vertical = 10.dp))
        SettlementRow("Yesterday, 19 Sep", "₹1,377.50", "Direct UPI to SBI A/c ...4819", true)
        HorizontalDivider(color = HomezyBorder, modifier = Modifier.padding(vertical = 10.dp))
        SettlementRow("18 Sep 2026", "₹1,729.00", "Direct UPI to SBI A/c ...4819", true)
        HorizontalDivider(color = HomezyBorder, modifier = Modifier.padding(vertical = 10.dp))
        SettlementRow("17 Sep 2026", "₹931.00", "Direct UPI to SBI A/c ...4819", true)
      }
    }
  }
}

@Composable
private fun BreakdownRow(
  title: String,
  amount: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconTint: Color,
  isHighlight: Boolean
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(
      modifier = Modifier.weight(1f),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(34.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(iconTint.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = iconTint,
          modifier = Modifier.size(18.dp)
        )
      }
      Spacer(modifier = Modifier.width(10.dp))
      Column {
        Text(
          text = title,
          fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.SemiBold,
          fontSize = 13.sp,
          color = if (isHighlight) HomezyPrimary else HomezyText
        )
        Text(
          text = subtitle,
          fontSize = 11.sp,
          color = HomezyTextSecondary,
          lineHeight = 14.sp
        )
      }
    }

    Text(
      text = amount,
      fontSize = if (isHighlight) 16.sp else 14.sp,
      fontWeight = if (isHighlight) FontWeight.ExtraBold else FontWeight.Bold,
      color = if (isHighlight) HomezyPrimary else HomezyText
    )
  }
}

@Composable
private fun SettlementRow(
  date: String,
  amount: String,
  destination: String,
  isSuccess: Boolean
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column {
      Text(text = date, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = HomezyText)
      Text(text = destination, fontSize = 11.sp, color = HomezyTextSecondary)
    }
    Column(horizontalAlignment = Alignment.End) {
      Text(text = amount, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = HomezyPrimary)
      Text(
        text = if (isSuccess) "Settled ✓" else "Processing",
        fontSize = 11.sp,
        color = if (isSuccess) HomezySecondary else HomezyWarning,
        fontWeight = FontWeight.Medium
      )
    }
  }
}
