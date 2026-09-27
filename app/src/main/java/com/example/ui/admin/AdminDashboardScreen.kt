package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.HomezyCard
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
  onNavigateToSection: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("admin_dashboard_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header
    item {
      Column {
        Text(
          text = "Bengaluru Cooperative Federation",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Governance, dispatch & cooperative welfare oversight",
          fontSize = 12.sp,
          color = HomezyTextSecondary
        )
      }
    }

    // 1. FIRST VIEW: Exactly 4 Key Metrics
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        AdminStatCard(
          title = "Total Workers",
          value = "180",
          icon = Icons.Default.People,
          modifier = Modifier.weight(1f)
        )
        AdminStatCard(
          title = "Active Workers",
          value = "142",
          icon = Icons.Default.CheckCircle,
          modifier = Modifier.weight(1f)
        )
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        AdminStatCard(
          title = "Today's Bookings",
          value = "84",
          icon = Icons.Default.Assignment,
          modifier = Modifier.weight(1f)
        )
        AdminStatCard(
          title = "Worker Earnings",
          value = "₹64,280",
          icon = Icons.Default.AccountBalanceWallet,
          modifier = Modifier.weight(1f)
        )
      }
    }

    // 2. "Attention Required" Section: 3 critical alerts
    item {
      Text(
        text = "Attention Required",
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
    }

    item {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AlertCard(
          title = "Certification Expiring",
          subtitle = "3 electricians due for skill renewal",
          icon = Icons.Default.WarningAmber,
          accentColor = Color(0xFFD97706),
          onClick = { onNavigateToSection("/admin/workers") }
        )
        AlertCard(
          title = "Insurance Renewal",
          subtitle = "2 cooperative group policies due in 7 days",
          icon = Icons.Default.HealthAndSafety,
          accentColor = Color(0xFF2563EB),
          onClick = { onNavigateToSection("/admin/welfare") }
        )
        AlertCard(
          title = "Pending Verification",
          subtitle = "4 craftspeople awaiting background approval",
          icon = Icons.Default.VerifiedUser,
          accentColor = Color(0xFF16A34A),
          onClick = { onNavigateToSection("/admin/workers") }
        )
      }
    }

    // 3. Separate Management Sections
    item {
      Text(
        text = "Federation Operations",
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
    }

    item {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        NavRowCard(
          title = "Fair Allocation",
          desc = "Workload distribution & lead equity algorithm",
          icon = Icons.Default.Balance,
          onClick = { onNavigateToSection("/admin/fair-allocation") }
        )
        NavRowCard(
          title = "AI Insights",
          desc = "Demand forecasting & price benchmark telemetry",
          icon = Icons.Default.TrendingUp,
          onClick = { onNavigateToSection("/admin/ai-insights") }
        )
        NavRowCard(
          title = "Welfare & Solidarity Fund",
          desc = "Healthcare claims & cooperative safety net",
          icon = Icons.Default.Security,
          onClick = { onNavigateToSection("/admin/welfare") }
        )
        NavRowCard(
          title = "Reports & Audits",
          desc = "Financial disclosures & compliance summaries",
          icon = Icons.Default.BarChart,
          onClick = { onNavigateToSection("/admin/reports") }
        )
      }
    }
  }
}

@Composable
private fun AdminStatCard(
  title: String,
  value: String,
  icon: ImageVector,
  modifier: Modifier = Modifier
) {
  HomezyCard(modifier = modifier) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(title, fontSize = 12.sp, color = HomezyTextSecondary, fontWeight = FontWeight.Medium)
      Icon(icon, contentDescription = null, tint = HomezyPrimary, modifier = Modifier.size(16.dp))
    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = HomezyText)
  }
}

@Composable
private fun AlertCard(
  title: String,
  subtitle: String,
  icon: ImageVector,
  accentColor: Color,
  onClick: () -> Unit
) {
  HomezyCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(accentColor.copy(alpha = 0.12f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = HomezyText)
          Text(subtitle, fontSize = 12.sp, color = HomezyTextSecondary)
        }
      }
      Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = HomezyTextSecondary, modifier = Modifier.size(16.dp))
    }
  }
}

@Composable
private fun NavRowCard(
  title: String,
  desc: String,
  icon: ImageVector,
  onClick: () -> Unit
) {
  HomezyCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(HomezyPrimaryContainer),
          contentAlignment = Alignment.Center
        ) {
          Icon(icon, contentDescription = null, tint = HomezyPrimary, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = HomezyText)
          Text(desc, fontSize = 12.sp, color = HomezyTextSecondary)
        }
      }
      Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = HomezyTextSecondary, modifier = Modifier.size(16.dp))
    }
  }
}
