package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.AdminWorkforceZoneAllocation
import com.example.data.SampleData
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun AdminFairAllocationScreen(
  modifier: Modifier = Modifier
) {
  var selectedAreaFilter by remember { mutableStateOf("All") }
  val areas = listOf("All", "Howrah", "Salt Lake / Sector V", "New Town", "Indiranagar, BLR", "Koramangala, BLR")

  var fairRotationEnabled by remember { mutableStateOf(true) }
  var antiBurnoutCapEnabled by remember { mutableStateOf(true) }
  var transparencyLogsPublic by remember { mutableStateOf(true) }

  val zoneAllocations = remember(selectedAreaFilter) {
    if (selectedAreaFilter == "All") {
      SampleData.sampleZoneAllocations
    } else {
      SampleData.sampleZoneAllocations.filter { it.area.contains(selectedAreaFilter, ignoreCase = true) }
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("admin_fair_allocation_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top Title & Prototype Label
    item {
      Column {
        Text(
          text = "Fair Workforce Allocation",
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Prototype Fair Allocation Algorithm",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyPrimary
        )
      }
    }

    // Disclaimer & Transparency Card
    item {
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
        modifier = Modifier.fillMaxWidth().testTag("admin_fairness_disclaimer_card")
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Balance,
              contentDescription = "Fairness Engine",
              tint = Color(0xFF1D4ED8),
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Prototype Fair Allocation Algorithm",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1E3A8A)
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Notice: This prototype demonstrates an explainable heuristic dispatch mechanism designed to prevent monopolization of jobs. We do not claim production-level AI accuracy.",
            fontSize = 12.sp,
            color = Color(0xFF1E40AF),
            lineHeight = 16.sp
          )
          Spacer(modifier = Modifier.height(6.dp))
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFFDBEAFE),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "Core Rule: Do not simply select the nearest worker. Instead, select a qualified worker while avoiding unfair concentration of jobs.",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF1E3A8A),
              modifier = Modifier.padding(8.dp)
            )
          }
        }
      }
    }

    // Opportunity Equity Summary Metric
    item {
      HomezyCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = HomezyCard
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text("Territory Equity Index", fontSize = 12.sp, color = HomezyTextSecondary)
            Text("0.16 (Democratic Gini Ratio)", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color(0xFF047857))
          }
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFECFDF5),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA7F3D0))
          ) {
            Text(
              text = "ANTI-MONOPOLY ACTIVE",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF065F46),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Corporate gig platforms average 0.62+ disparity (where top 10% capture 60% of all customer requests). HOMEZY's cooperative algorithm guarantees work rotation across all active guild members.",
          fontSize = 11.sp,
          color = HomezyTextSecondary,
          lineHeight = 15.sp
        )
      }
    }

    // Filter Chips for Areas
    item {
      Column {
        Text(
          text = "Filter by Cooperative Territory",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(areas) { area ->
            val isSelected = selectedAreaFilter == area
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = if (isSelected) HomezyPrimary else HomezyCard,
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isSelected) HomezyPrimary else HomezyBorder
              ),
              modifier = Modifier
                .clickable { selectedAreaFilter = area }
                .testTag("admin_area_filter_$area")
            ) {
              Text(
                text = area,
                color = if (isSelected) Color.White else HomezyText,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
              )
            }
          }
        }
      }
    }

    // Area & Service Allocation Cards
    item {
      Text(
        text = "Zone Allocations & Demand Balancing",
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
    }

    items(zoneAllocations) { zone ->
      ZoneAllocationCard(zone = zone)
    }

    // Explainable Scoring Logic Section
    item {
      HomezyCard(
        modifier = Modifier.fillMaxWidth().testTag("admin_scoring_logic_card"),
        backgroundColor = HomezyCard
      ) {
        Text(
          text = "Workforce Allocation Scoring Logic Explained",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "To eliminate arbitrary dispatch and algorithmic bias, the system factors in 7 transparent variables:",
          fontSize = 12.sp,
          color = HomezyTextSecondary,
          lineHeight = 16.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        FactorExplainerRow("1. Recent Jobs", "Workers with fewer dispatches in the trailing 7 days receive priority weighting.")
        FactorExplainerRow("2. Current Workload", "Active concurrent jobs are monitored to cap simultaneous backlog.")
        FactorExplainerRow("3. Hours Worked", "Enforces a strict 8-hour daily limit to protect safety and prevent fatigue.")
        FactorExplainerRow("4. Recent Earnings", "Equalizes take-home income distribution across cooperative guild members.")
        FactorExplainerRow("5. Live Availability", "Verifies worker is clocked-in and ready on two-wheeler transport.")
        FactorExplainerRow("6. Certified Skill", "Ensures high-level craft qualification matches specific task requirements.")
        FactorExplainerRow("7. Distance", "Favors reasonable proximity while deliberately preventing spatial monopolies.")
      }
    }

    // Federation Allocation Parameters Toggles
    item {
      Text(
        text = "Federation Governance Controls",
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = HomezyText
      )
    }

    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        RuleToggleRow(
          title = "Equitable Lead Rotation (Anti-Monopoly)",
          description = "Boosts dispatch priority for verified craftspeople who received fewer jobs this calendar week.",
          checked = fairRotationEnabled,
          onCheckedChange = { fairRotationEnabled = it }
        )

        Divider(color = HomezyBorder, modifier = Modifier.padding(vertical = 12.dp))

        RuleToggleRow(
          title = "Anti-Burnout Fatigue Cap (8h Maximum)",
          description = "Automatically halts new high-risk dispatches after 8 billable hours to protect worker safety and health.",
          checked = antiBurnoutCapEnabled,
          onCheckedChange = { antiBurnoutCapEnabled = it }
        )

        Divider(color = HomezyBorder, modifier = Modifier.padding(vertical = 12.dp))

        RuleToggleRow(
          title = "Open Algorithmic Transparency Logs",
          description = "Workers can view full reasoning for why any job was assigned in their local cooperative district.",
          checked = transparencyLogsPublic,
          onCheckedChange = { transparencyLogsPublic = it }
        )
      }
    }
  }
}

@Composable
private fun ZoneAllocationCard(
  zone: AdminWorkforceZoneAllocation,
  modifier: Modifier = Modifier
) {
  HomezyCard(
    modifier = modifier.fillMaxWidth().testTag("zone_card_${zone.area}"),
    backgroundColor = HomezyCard
  ) {
    // Area & Service Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Area",
            tint = HomezyPrimary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Area: ${zone.area}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Service: ${zone.service}",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = HomezyTextSecondary
        )
      }

      // Demand Level Badge
      val demandBg = when (zone.demand.lowercase()) {
        "surging" -> Color(0xFFFEF2F2)
        "high" -> Color(0xFFFFFBEB)
        else -> Color(0xFFEFF6FF)
      }
      val demandText = when (zone.demand.lowercase()) {
        "surging" -> Color(0xFFB91C1C)
        "high" -> Color(0xFFB45309)
        else -> Color(0xFF1D4ED8)
      }
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = demandBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, demandText.copy(alpha = 0.3f))
      ) {
        Text(
          text = "Demand: ${zone.demand}",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = demandText,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Key metrics: Available Workers & Recommended Allocation
    Surface(
      shape = RoundedCornerShape(8.dp),
      color = HomezySurfaceVariant,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text("Available Workers", fontSize = 11.sp, color = HomezyTextSecondary)
          Text("${zone.availableWorkers}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = HomezyText)
        }

        Box(
          modifier = Modifier
            .width(1.dp)
            .height(32.dp)
            .background(HomezyBorder)
        )

        Column {
          Text("Recommended Allocation", fontSize = 11.sp, color = HomezyTextSecondary)
          Text("${zone.recommendedAllocation}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = HomezyPrimary)
        }

        Box(
          modifier = Modifier
            .width(1.dp)
            .height(32.dp)
            .background(HomezyBorder)
        )

        Column(horizontalAlignment = Alignment.End) {
          Text("Equity Gini", fontSize = 11.sp, color = HomezyTextSecondary)
          Text("${zone.giniEqualityIndex}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    Text(
      text = zone.statusNotes,
      fontSize = 12.sp,
      color = HomezyTextSecondary,
      lineHeight = 16.sp
    )
  }
}

@Composable
private fun FactorExplainerRow(factor: String, description: String) {
  Column(modifier = Modifier.padding(vertical = 4.dp)) {
    Text(text = factor, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = HomezyText)
    Text(text = description, fontSize = 11.sp, color = HomezyTextSecondary, lineHeight = 15.sp)
  }
}

@Composable
private fun RuleToggleRow(
  title: String,
  description: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
      Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = HomezyText)
      Spacer(modifier = Modifier.height(2.dp))
      Text(description, fontSize = 12.sp, color = HomezyTextSecondary, lineHeight = 16.sp)
    }

    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = HomezyPrimary
      )
    )
  }
}
