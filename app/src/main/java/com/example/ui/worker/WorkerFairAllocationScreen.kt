package com.example.ui.worker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
  var isExpanded by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Fair Work",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
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
        colors = TopAppBarDefaults.topAppBarColors(containerColor = HomezyCard)
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
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Initial Screen: Workload, Jobs Today, Hours Worked
      item {
        HomezyCard(modifier = Modifier.fillMaxWidth().testTag("worker_fair_work_card")) {
          Text(
            text = "Fair Work Status",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text("Your current workload", fontSize = 12.sp, color = HomezyTextSecondary)
              Text("Balanced (Low Backlog)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
            }
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = Color(0xFFDCFCE7)
            ) {
              Text(
                text = "Optimal",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF15803D),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))
          Divider(color = HomezyBorder)
          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text("Jobs today", fontSize = 12.sp, color = HomezyTextSecondary)
              Text("3 completed", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = HomezyText)
            }
            Column(horizontalAlignment = Alignment.End) {
              Text("Hours worked", fontSize = 12.sp, color = HomezyTextSecondary)
              Text("5.5 hrs", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = HomezyText)
            }
          }
        }
      }

      // 2. Expandable "How HOMEZY allocates work"
      item {
        HomezyCard(modifier = Modifier.fillMaxWidth().testTag("how_homezy_allocates_card")) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { isExpanded = !isExpanded }
              .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "How HOMEZY allocates work",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyPrimary
            )
            Text(
              text = if (isExpanded) "▲ Hide" else "▼ Expand",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = HomezyPrimary
            )
          }

          AnimatedVisibility(visible = isExpanded) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(HomezySurfaceVariant)
                .padding(12.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Text(
                text = "• Skill: Matched to trade certification and customer requirements.",
                fontSize = 13.sp,
                color = HomezyText
              )
              Text(
                text = "• Availability: Prioritizes craftspeople with active status and open slots.",
                fontSize = 13.sp,
                color = HomezyText
              )
              Text(
                text = "• Distance: Dispatches within local cluster (typically < 3 km) to save travel time.",
                fontSize = 13.sp,
                color = HomezyText
              )
              Text(
                text = "• Workload: Equitable rotation prevents burnout and ensures equal opportunities.",
                fontSize = 13.sp,
                color = HomezyText
              )
              Text(
                text = "• Recent jobs: Ensures recently under-allocated members receive priority dispatch.",
                fontSize = 13.sp,
                color = HomezyText
              )
            }
          }
        }
      }
    }
  }
}
