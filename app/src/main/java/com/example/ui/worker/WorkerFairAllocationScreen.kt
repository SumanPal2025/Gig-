package com.example.ui.worker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.ui.components.HomezyCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerFairAllocationScreen(
  onBack: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  // Current worker's fair allocation state (e.g. Rahul Das / Rahul Sharma)
  val myStat = remember {
    SampleData.sampleWorkerAllocationStats.firstOrNull() ?: SampleData.sampleWorkerAllocationStats[0]
  }

  val peerStats = remember {
    SampleData.sampleWorkerAllocationStats
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Fair Work Allocation",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyText
            )
            Text(
              text = "Prototype Fair Allocation Algorithm",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = HomezyPrimary
            )
          }
        },
        navigationIcon = {
          if (onBack != null) {
            IconButton(
              onClick = onBack,
              modifier = Modifier.testTag("worker_fair_allocation_back_button")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = HomezyText
              )
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = HomezyCard
        )
      )
    },
    modifier = modifier.testTag("worker_fair_allocation_screen")
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(HomezyBackground)
        .padding(paddingValues),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Prototype Label & Algorithm Notice
      item {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
          modifier = Modifier.fillMaxWidth().testTag("worker_algorithm_notice_banner")
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Balance,
                contentDescription = "Fair Allocation",
                tint = Color(0xFF15803D),
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Prototype Fair Allocation Algorithm",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF166534)
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            // Exact required explanation
            Text(
              text = "HOMEZY considers workload and availability so qualified workers receive fair opportunities.",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF14532D),
              lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Unlike predatory gig platforms where algorithms create winner-take-all monopolies or force 14-hour burnout shifts, HOMEZY's cooperative algorithm distributes incoming customer jobs democratically among all certified workers in your zone.",
              fontSize = 11.sp,
              color = Color(0xFF166534),
              lineHeight = 15.sp
            )
          }
        }
      }

      // 5 Core Worker Metrics (Exact required metrics)
      item {
        Text(
          text = "Your Allocation Metrics (Current Cycle)",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
      }

      item {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          // Row 1: Jobs Received & Hours Worked
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            MetricCard(
              title = "Jobs Received",
              value = "${myStat.jobsReceived}",
              subtext = "Equitably allocated this week",
              icon = Icons.Default.WorkOutline,
              accentColor = HomezyPrimary,
              modifier = Modifier.weight(1f).testTag("metric_jobs_received")
            )
            MetricCard(
              title = "Hours Worked",
              value = "${myStat.hoursWorked} hrs",
              subtext = "Billable on-site time",
              icon = Icons.Default.Timer,
              accentColor = Color(0xFF0284C7),
              modifier = Modifier.weight(1f).testTag("metric_hours_worked")
            )
          }

          // Row 2: Current Workload & Idle Time
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            MetricCard(
              title = "Current Workload",
              value = myStat.currentWorkload,
              subtext = "Optimal balance (no burnout)",
              icon = Icons.Default.Speed,
              accentColor = Color(0xFF16A34A),
              modifier = Modifier.weight(1f).testTag("metric_current_workload")
            )
            MetricCard(
              title = "Idle Time",
              value = "${myStat.idleTimeHours} hrs",
              subtext = "Standby availability today",
              icon = Icons.Default.HourglassEmpty,
              accentColor = Color(0xFFD97706),
              modifier = Modifier.weight(1f).testTag("metric_idle_time")
            )
          }

          // Row 3: Earnings (Full width)
          MetricCard(
            title = "Earnings",
            value = "₹${myStat.recentEarnings}",
            subtext = "95% direct worker payout (₹0 predatory commissions)",
            icon = Icons.Default.CurrencyRupee,
            accentColor = Color(0xFF059669),
            modifier = Modifier.fillMaxWidth().testTag("metric_earnings")
          )
        }
      }

      // Dispatch Queue Status Card
      item {
        HomezyCard(
          modifier = Modifier.fillMaxWidth().testTag("dispatch_priority_card"),
          backgroundColor = HomezyCard
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Dispatch Queue Position",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = HomezyText
              )
              Text(
                text = myStat.status,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = HomezyPrimary
              )
            }
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color(0xFFEFF6FF),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
            ) {
              Text(
                text = "${myStat.fairOpportunityScore}% Opportunity Score",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D4ED8),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
              )
            }
          }
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Because your current workload is Low and you completed 6 jobs this cycle, you are queued for high priority in the next incoming booking match in your service radius.",
            fontSize = 12.sp,
            color = HomezyTextSecondary,
            lineHeight = 16.sp
          )
        }
      }

      // Transparent Peer Zone Distribution (Democracy in Action)
      item {
        Column {
          Text(
            text = "Zone Peer Opportunity Distribution",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "All verified members in your trade share access to nearby jobs without favoritism.",
            fontSize = 11.sp,
            color = HomezyTextSecondary
          )
        }
      }

      items(peerStats) { peer ->
        Card(
          shape = RoundedCornerShape(10.dp),
          colors = CardDefaults.cardColors(containerColor = HomezyCard),
          border = androidx.compose.foundation.BorderStroke(1.dp, HomezyBorder),
          modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(HomezySurfaceVariant),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = peer.workerName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.joinToString(""),
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = HomezyText
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = peer.workerName,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = HomezyText
                )
                Text(
                  text = "${peer.jobsReceived} jobs • ${peer.hoursWorked}h • ${peer.currentWorkload} load",
                  fontSize = 11.sp,
                  color = HomezyTextSecondary
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (peer.currentWorkload == "Low") Color(0xFFECFDF5) else if (peer.currentWorkload == "Moderate") Color(0xFFEFF6FF) else Color(0xFFFFFBEB),
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (peer.currentWorkload == "Low") Color(0xFFA7F3D0) else if (peer.currentWorkload == "Moderate") Color(0xFFBFDBFE) else Color(0xFFFDE68A)
              )
            ) {
              Text(
                text = if (peer.currentWorkload == "Low") "Next in Queue" else if (peer.currentWorkload == "Moderate") "Active" else "Rest Cycle",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (peer.currentWorkload == "Low") Color(0xFF047857) else if (peer.currentWorkload == "Moderate") Color(0xFF1D4ED8) else Color(0xFFB45309),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }
      }

      // Algorithm Explainability Note
      item {
        Card(
          shape = RoundedCornerShape(10.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier.fillMaxWidth().testTag("worker_scoring_rules_card")
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text(
              text = "Cooperative Anti-Burnout Policy",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "HOMEZY automatically pauses aggressive dispatching once a craftsperson reaches 8 daily on-site hours or 35 monthly completed jobs. This guarantees long-term earning stability and family time without penalty to your algorithm standing.",
              fontSize = 11.sp,
              color = HomezyTextSecondary,
              lineHeight = 15.sp
            )
          }
        }
      }
    }
  }
}

@Composable
private fun MetricCard(
  title: String,
  value: String,
  subtext: String,
  icon: ImageVector,
  accentColor: Color,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = HomezyCard),
    border = androidx.compose.foundation.BorderStroke(1.dp, HomezyBorder),
    modifier = modifier
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = HomezyTextSecondary)
        Icon(imageVector = icon, contentDescription = title, tint = accentColor, modifier = Modifier.size(16.dp))
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = HomezyText)
      Spacer(modifier = Modifier.height(2.dp))
      Text(text = subtext, fontSize = 10.sp, color = HomezyTextSecondary, maxLines = 1)
    }
  }
}
