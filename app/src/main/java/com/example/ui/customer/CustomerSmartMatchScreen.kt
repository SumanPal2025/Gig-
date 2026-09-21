package com.example.ui.customer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import com.example.data.SmartMatchCandidate
import com.example.data.WorkerProfile
import com.example.ui.components.HomezyButton
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

  // Candidates retrieved via explainable prototype algorithm
  val candidates = remember(selectedService) {
    SampleData.calculateSmartMatchCandidates(service = selectedService)
  }

  // Expanded explanations state for "Why this worker?"
  var expandedWorkerId by remember { mutableStateOf<String?>("w_rahul_das") }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Smart Match",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyText
            )
            Text(
              text = "AI Smart Matching Prototype",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = HomezyPrimary
            )
          }
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
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Prototype Transparency Notice & Subtitle
      item {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
          modifier = Modifier.fillMaxWidth().testTag("smart_match_prototype_banner")
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = "AI Prototype",
                tint = Color(0xFF1D4ED8),
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "AI Smart Matching Prototype",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A)
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "HOMEZY finds suitable workers using skill, distance, availability, rating and workload.",
              fontSize = 13.sp,
              fontWeight = FontWeight.Medium,
              color = Color(0xFF1E40AF),
              lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Notice: This prototype uses an explainable scoring algorithm (0–100%) rather than a black-box model. Scores are calculated deterministically from transparent cooperative metrics.",
              fontSize = 11.sp,
              color = Color(0xFF3B82F6),
              lineHeight = 15.sp
            )
          }
        }
      }

      // Service Filter Chips
      item {
        Column {
          Text(
            text = "Select Service Category",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyTextSecondary
          )
          Spacer(modifier = Modifier.height(8.dp))
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
                  modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
              }
            }
          }
        }
      }

      // Top 3 Header
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Top 3 Matched Workers",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF0FDF4),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0))
          ) {
            Text(
              text = "Anti-Monopoly Rotation Active",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF166534),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }

      // Top 3 Workers Cards
      items(candidates) { candidate ->
        SmartMatchWorkerCard(
          candidate = candidate,
          isExpanded = expandedWorkerId == candidate.workerId,
          onToggleExpand = {
            expandedWorkerId = if (expandedWorkerId == candidate.workerId) null else candidate.workerId
          },
          onBookWorker = {
            // Find or map to WorkerProfile
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
          }
        )
      }

      // Explainable Scoring Logic Card
      item {
        HomezyCard(
          modifier = Modifier.fillMaxWidth().testTag("scoring_logic_card"),
          backgroundColor = HomezyCard
        ) {
          Text(
            text = "How the Explainable Scoring Algorithm Works",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Unlike closed algorithms that prioritize advertising or nearest proximity alone, HOMEZY's transparent algorithm calculates a balanced score across 5 essential pillars:",
            fontSize = 12.sp,
            color = HomezyTextSecondary,
            lineHeight = 16.sp
          )
          Spacer(modifier = Modifier.height(10.dp))
          ScoringPillarItem(
            title = "1. Skill Match (Max 30 pts)",
            desc = "Trade certification, specialized tools, and historical problem tags.",
            weight = "30%"
          )
          ScoringPillarItem(
            title = "2. Distance & Proximity (Max 25 pts)",
            desc = "Fast arrival time without high transit fuel costs.",
            weight = "25%"
          )
          ScoringPillarItem(
            title = "3. Live Availability (Max 20 pts)",
            desc = "Matches customer's preferred scheduling slot.",
            weight = "20%"
          )
          ScoringPillarItem(
            title = "4. Verified Rating (Max 15 pts)",
            desc = "Customer satisfaction track record on completed jobs.",
            weight = "15%"
          )
          ScoringPillarItem(
            title = "5. Workload Fairness (Max 10 pts)",
            desc = "Prioritizes qualified workers with low current backlog to prevent burnout and ensure undivided attention.",
            weight = "10%"
          )
        }
      }
    }
  }
}

@Composable
fun SmartMatchWorkerCard(
  candidate: SmartMatchCandidate,
  isExpanded: Boolean,
  onToggleExpand: () -> Unit,
  onBookWorker: () -> Unit,
  modifier: Modifier = Modifier
) {
  val b = candidate.breakdown

  HomezyCard(
    modifier = modifier.fillMaxWidth().testTag("smart_match_worker_card_${candidate.workerId}"),
    backgroundColor = HomezyCard
  ) {
    // Header: Name, Verified Badge, Match Score
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(HomezyPrimary.copy(alpha = 0.12f)),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = candidate.workerName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
            fontWeight = FontWeight.Bold,
            color = HomezyPrimary,
            fontSize = 15.sp
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = candidate.workerName,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyText
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.Default.Verified,
              contentDescription = "Verified Badge",
              tint = Color(0xFF2563EB),
              modifier = Modifier.size(16.dp)
            )
          }
          Text(
            text = candidate.trade,
            fontSize = 12.sp,
            color = HomezyTextSecondary
          )
        }
      }

      // Match Score Badge
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFECFDF5),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF6EE7B7))
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Text(
            text = "${candidate.matchPercentage}% Match",
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF047857)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // 5 Key Evaluation Pillars Display
    Surface(
      shape = RoundedCornerShape(8.dp),
      color = HomezySurfaceVariant,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          MetricChip(
            icon = Icons.Default.Build,
            label = "Skill Match",
            value = b.skillMatchLabel,
            valueColor = Color(0xFF047857)
          )
          MetricChip(
            icon = Icons.Default.Navigation,
            label = "Distance",
            value = "${b.distanceKm} km",
            valueColor = HomezyText
          )
        }
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          MetricChip(
            icon = Icons.Default.Schedule,
            label = "Availability",
            value = b.availabilityLabel,
            valueColor = Color(0xFF1D4ED8)
          )
          MetricChip(
            icon = Icons.Default.Star,
            label = "Rating",
            value = "${b.ratingValue} ★",
            valueColor = Color(0xFFB45309)
          )
        }
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          MetricChip(
            icon = Icons.Default.Balance,
            label = "Workload",
            value = b.workloadLabel,
            valueColor = Color(0xFF047857)
          )
          MetricChip(
            icon = Icons.Default.CurrencyRupee,
            label = "Estimated",
            value = "₹${candidate.estimatedPrice}",
            valueColor = HomezyPrimary
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // "Why this worker?" Expandable Button
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(6.dp))
        .clickable { onToggleExpand() }
        .padding(vertical = 4.dp)
        .testTag("why_this_worker_button_${candidate.workerId}"),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.HelpOutline,
          contentDescription = "Why this worker?",
          tint = HomezyPrimary,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Why this worker?",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyPrimary
        )
      }
      Icon(
        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
        contentDescription = if (isExpanded) "Collapse explanation" else "Expand explanation",
        tint = HomezyPrimary,
        modifier = Modifier.size(20.dp)
      )
    }

    // Expandable Explanation Section
    AnimatedVisibility(
      visible = isExpanded,
      enter = fadeIn() + expandVertically(),
      exit = fadeOut() + shrinkVertically()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFFF8FAFC))
          .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
          .padding(12.dp)
          .testTag("explanation_content_${candidate.workerId}")
      ) {
        Text(
          text = "Algorithmic Score Breakdown",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(6.dp))

        ScoreProgressBar(label = "Skill Match", score = b.skillMatchScore, max = 30, text = "${b.skillMatchScore}/30 (${b.skillMatchLabel})")
        ScoreProgressBar(label = "Proximity", score = b.distanceScore, max = 25, text = "${b.distanceScore}/25 (${b.distanceKm} km)")
        ScoreProgressBar(label = "Availability", score = b.availabilityScore, max = 20, text = "${b.availabilityScore}/20 (${b.availabilityLabel})")
        ScoreProgressBar(label = "Rating", score = b.ratingScore, max = 15, text = "${b.ratingScore}/15 (${b.ratingValue}★)")
        ScoreProgressBar(label = "Workload Balance", score = b.workloadScore, max = 10, text = "${b.workloadScore}/10 (${b.workloadLabel})")

        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = b.explanation,
          fontSize = 11.sp,
          color = HomezyTextSecondary,
          lineHeight = 16.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Book Worker Button
    HomezyButton(
      text = "Book Worker",
      onClick = onBookWorker,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("book_worker_button_${candidate.workerId}")
    )
  }
}

@Composable
private fun MetricChip(
  icon: ImageVector,
  label: String,
  value: String,
  valueColor: Color
) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Icon(imageVector = icon, contentDescription = label, tint = HomezyTextSecondary, modifier = Modifier.size(14.dp))
    Spacer(modifier = Modifier.width(4.dp))
    Text(text = "$label: ", fontSize = 11.sp, color = HomezyTextSecondary)
    Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = valueColor)
  }
}

@Composable
private fun ScoreProgressBar(
  label: String,
  score: Int,
  max: Int,
  text: String
) {
  Column(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(text = label, fontSize = 10.sp, color = HomezyTextSecondary)
      Text(text = text, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = HomezyText)
    }
    LinearProgressIndicator(
      progress = { (score.toFloat() / max.toFloat()).coerceIn(0f, 1f) },
      modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
      color = HomezyPrimary,
      trackColor = Color(0xFFE2E8F0)
    )
  }
}

@Composable
private fun ScoringPillarItem(
  title: String,
  desc: String,
  weight: String
) {
  Column(modifier = Modifier.padding(vertical = 4.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = HomezyText)
      Text(text = weight, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = HomezyPrimary)
    }
    Text(text = desc, fontSize = 11.sp, color = HomezyTextSecondary, lineHeight = 15.sp)
  }
}
