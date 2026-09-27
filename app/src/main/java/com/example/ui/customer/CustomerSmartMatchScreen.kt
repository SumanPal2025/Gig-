package com.example.ui.customer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.data.SmartMatchCandidate
import com.example.data.WorkerProfile
import com.example.ui.components.HomezyCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerSmartMatchScreen(
  onBack: () -> Unit,
  onSelectWorkerForBooking: (WorkerProfile, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedService by remember { mutableStateOf("AC Service") }
  val services = listOf("AC Service", "Electrician", "Plumber")

  // Candidates retrieved via explainable algorithm
  val candidates = remember(selectedService) {
    SampleData.calculateSmartMatchCandidates(service = selectedService)
  }

  // Expanded state for "Why this worker?"
  var expandedWorkerId by remember { mutableStateOf<String?>(null) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Smart Match",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("smart_match_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = HomezyText
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = HomezyCard
        )
      )
    },
    modifier = modifier.testTag("customer_smart_match_screen")
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(HomezyBackground)
        .padding(paddingValues),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Service Filter Chips
      item {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(services) { service ->
            val isSelected = selectedService == service
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = if (isSelected) HomezyPrimary else HomezyCard,
              border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = if (isSelected) HomezyPrimary else HomezyBorder
              ),
              modifier = Modifier
                .clickable { selectedService = service }
                .testTag("service_filter_$service")
            ) {
              Text(
                text = service,
                color = if (isSelected) Color.White else HomezyText,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
              )
            }
          }
        }
      }

      // Section Header: "Recommended for You"
      item {
        Text(
          text = "Recommended for You",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText,
          modifier = Modifier.testTag("smart_match_recommended_header")
        )
      }

      // Candidates
      items(candidates) { candidate ->
        val isExpanded = expandedWorkerId == candidate.workerId

        HomezyCard(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("smart_match_worker_card_${candidate.workerId}")
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = candidate.workerName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = HomezyText
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "${candidate.trade} · ${candidate.distanceKm} km · ★ ${candidate.rating}",
                fontSize = 13.sp,
                color = HomezyTextSecondary
              )
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFDCFCE7)
            ) {
              Text(
                text = "${candidate.matchPercentage}% Match",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF15803D),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // "Why this worker?" small expandable toggle
          Row(
            modifier = Modifier
              .clickable {
                expandedWorkerId = if (isExpanded) null else candidate.workerId
              }
              .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (isExpanded) "Hide details ▲" else "Why this worker? ▼",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = HomezyPrimary
            )
          }

          // Expandable section: Skill, Distance, Availability, Rating, Workload
          AnimatedVisibility(visible = isExpanded) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(HomezySurfaceVariant)
                .padding(10.dp),
              verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Text(
                text = "• Skill: ${candidate.breakdown.skillMatchLabel}",
                fontSize = 12.sp,
                color = HomezyText
              )
              Text(
                text = "• Distance: ${candidate.distanceKm} km",
                fontSize = 12.sp,
                color = HomezyText
              )
              Text(
                text = "• Availability: ${candidate.availability}",
                fontSize = 12.sp,
                color = HomezyText
              )
              Text(
                text = "• Rating: ★ ${candidate.rating} (${candidate.jobsCompleted} jobs)",
                fontSize = 12.sp,
                color = HomezyText
              )
              Text(
                text = "• Workload: ${candidate.breakdown.workloadScore}/10 (Balanced allocation)",
                fontSize = 12.sp,
                color = HomezyText
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // [ Select Worker ] Button
          Button(
            onClick = {
              val workerProfile = SampleData.workers.find { it.name.contains(candidate.workerName.split(" ")[0]) }
                ?: WorkerProfile(
                  id = candidate.workerId,
                  name = candidate.workerName,
                  trade = candidate.trade,
                  cooperativeId = "COOP-KA-042",
                  experienceYears = 6,
                  rating = candidate.rating,
                  completedJobs = candidate.jobsCompleted,
                  hourlyRate = candidate.estimatedPrice,
                  distanceKm = candidate.distanceKm,
                  isVerified = candidate.verified,
                  isAvailable = candidate.availability == "Available",
                  skills = listOf(candidate.trade, "Diagnostics", "Certified Co-Op Member"),
                  phone = "+91 98765 43210",
                  locationArea = "Indiranagar, Bengaluru",
                  fairAllocationScore = candidate.matchPercentage
                )
              onSelectWorkerForBooking(workerProfile, selectedService)
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = HomezyPrimary),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("select_worker_${candidate.workerId}")
          ) {
            Text(
              text = "Select Worker",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }
    }
  }
}
