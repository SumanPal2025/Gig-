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
import com.example.data.HomezyRepository
import com.example.ui.components.*
import com.example.ui.theme.*

data class AdminWorkerItem(
  val id: String,
  val name: String,
  val cooperative: String,
  val skill: String,
  val skillLevel: String,
  val verification: String, // "Verified Member", "Pending Verification", "Suspended"
  val availability: String, // "Available", "On Job", "Offline"
  val rating: Double,
  val completedJobs: Int,
  val workload: String, // "Low (2 jobs)", "Balanced (4 jobs)", "High (6 jobs)"
  val insuranceStatus: String = "Active (₹5L Cashless)",
  val experienceYears: Int = 8,
  val phone: String = "+91 98765 43210"
)

@Composable
fun AdminWorkersScreen(
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedTab by remember { mutableStateOf("All") }
  val tabs = listOf("All", "Verified Members", "Pending Verification", "Suspended")

  val canonicalItems = remember {
    HomezyRepository.canonicalWorkers.map { w ->
      val coopName = when (w.cooperativeId) {
        "COOP-KA-0429" -> "Bengaluru Urban Electricians Co-op"
        "COOP-KA-0118" -> "Koramangala Sanitation Co-op"
        "COOP-KA-0382" -> "HSR Technical Federation"
        "COOP-KA-0715" -> "Domlur Electrical Co-op"
        "COOP-KA-0512" -> "Indiranagar Women Tech Co-op"
        "COOP-KA-0294" -> "South Bengaluru Plumbing Guild"
        "COOP-KA-0833" -> "Outer Ring Road Tech Co-op"
        "COOP-KA-0621" -> "HAL & Marathahalli Workers Co-op"
        "COOP-KA-0901" -> "Electronic City Allied Techs"
        else -> "Bengaluru Cooperative Federation"
      }
      val verificationStatus = if (w.id == "wrk_15") "Pending Verification" else if (w.isVerified) "Verified Member" else "Suspended"
      val availabilityStatus = if (w.isAvailable) "Available" else "On Job"
      val workloadDesc = if (w.completedJobs > 600) "High (6 jobs)" else if (w.completedJobs > 350) "Balanced (4 jobs)" else "Low (2 jobs)"
      AdminWorkerItem(
        id = w.id.uppercase().replace("WRK_", "WKR-10"),
        name = w.name,
        cooperative = coopName,
        skill = w.trade.split("&", "•").first().trim(),
        skillLevel = "Level ${if (w.experienceYears >= 8) 3 else if (w.experienceYears >= 5) 2 else 1} Master",
        verification = verificationStatus,
        availability = availabilityStatus,
        rating = w.rating.toDouble(),
        completedJobs = w.completedJobs,
        workload = workloadDesc,
        insuranceStatus = "Active (₹5L Cashless)",
        experienceYears = w.experienceYears,
        phone = w.phone
      )
    }
  }

  var workersList by remember { mutableStateOf(canonicalItems) }

  var selectedWorkerForModal by remember { mutableStateOf<AdminWorkerItem?>(null) }
  var snackbarMessage by remember { mutableStateOf<String?>(null) }

  val filteredWorkers = workersList.filter { worker ->
    val matchesSearch = searchQuery.isBlank() ||
      worker.name.contains(searchQuery, true) ||
      worker.skill.contains(searchQuery, true) ||
      worker.cooperative.contains(searchQuery, true) ||
      worker.id.contains(searchQuery, true)

    val matchesTab = when (selectedTab) {
      "Verified Members" -> worker.verification == "Verified Member"
      "Pending Verification" -> worker.verification == "Pending Verification"
      "Suspended" -> worker.verification == "Suspended"
      else -> true
    }

    matchesSearch && matchesTab
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("admin_workers_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header
    item {
      Column {
        Text(
          text = "Cooperative Workforce Registry",
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Text(
          text = "Manage verified members, democratic credentials, fair workloads, and verification status",
          fontSize = 13.sp,
          color = HomezyTextSecondary
        )
        Spacer(modifier = Modifier.height(12.dp))
        HomezySearchBar(
          query = searchQuery,
          onQueryChange = { searchQuery = it },
          placeholder = "Search worker name, ID, skill, or co-op..."
        )
      }
    }

    // Filter Chips
    item {
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(tabs) { tab ->
          FilterChip(
            selected = selectedTab == tab,
            onClick = { selectedTab = tab },
            label = {
              val count = when (tab) {
                "Verified Members" -> workersList.count { it.verification == "Verified Member" }
                "Pending Verification" -> workersList.count { it.verification == "Pending Verification" }
                "Suspended" -> workersList.count { it.verification == "Suspended" }
                else -> workersList.size
              }
              Text("$tab ($count)", fontSize = 12.sp)
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = HomezyPrimary,
              selectedLabelColor = Color.White
            )
          )
        }
      }
    }

    // Quick Stats Bar
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        StatCard(
          title = "Total Cooperative Members",
          value = "${workersList.size}",
          subtext = "Across 6 guilds",
          icon = Icons.Default.People,
          modifier = Modifier.weight(1f)
        )
        StatCard(
          title = "Verified Active",
          value = "${workersList.count { it.verification == "Verified Member" }}",
          badgeText = "98% Compliance",
          isPositive = true,
          icon = Icons.Default.VerifiedUser,
          modifier = Modifier.weight(1f)
        )
      }
    }

    // Workers Table Card List
    items(filteredWorkers) { worker ->
      HomezyCard(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("admin_worker_card_${worker.id}")
      ) {
        // Row 1: Avatar, Name, Cooperative, Skill
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(
                if (worker.verification == "Suspended") Color(0xFFFEE2E2)
                else HomezyPrimaryContainer
              ),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = worker.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = if (worker.verification == "Suspended") Color(0xFFDC2626) else HomezyPrimary
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = worker.name,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = HomezyText
              )
              HomezyBadge(
                text = "★ ${worker.rating}",
                containerColor = HomezyAccentContainer,
                contentColor = Color(0xFF92400E)
              )
            }
            Text(
              text = "${worker.skill} (${worker.skillLevel}) • ${worker.id}",
              fontSize = 12.sp,
              color = HomezyPrimary,
              fontWeight = FontWeight.Medium
            )
            Text(
              text = worker.cooperative,
              fontSize = 11.sp,
              color = HomezyTextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = HomezyBorder, thickness = 0.8.dp)
        Spacer(modifier = Modifier.height(8.dp))

        // Row 2: Verification, Availability, Workload metrics
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Verification badge
          when (worker.verification) {
            "Verified Member" -> HomezyBadge(
              text = "Verified Member ✓",
              containerColor = Color(0xFFDCFCE7),
              contentColor = Color(0xFF15803D),
              icon = Icons.Default.Verified
            )
            "Pending Verification" -> HomezyBadge(
              text = "Pending Verification ⏳",
              containerColor = Color(0xFFFEF3C7),
              contentColor = Color(0xFF92400E),
              icon = Icons.Default.HourglassEmpty
            )
            else -> HomezyBadge(
              text = "Suspended ⏸",
              containerColor = Color(0xFFFEE2E2),
              contentColor = Color(0xFFDC2626),
              icon = Icons.Default.Block
            )
          }

          // Availability
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = when (worker.availability) {
              "Available" -> Color(0xFFE0F2FE)
              "On Job" -> Color(0xFFFEF3C7)
              else -> Color(0xFFF3F4F6)
            }
          ) {
            Text(
              text = "Status: ${worker.availability}",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = when (worker.availability) {
                "Available" -> Color(0xFF0369A1)
                "On Job" -> Color(0xFFB45309)
                else -> Color(0xFF4B5563)
              },
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          // Workload
          Text(
            text = "Workload: ${worker.workload}",
            fontSize = 11.sp,
            color = if (worker.workload.contains("High")) Color(0xFFD97706) else Color(0xFF059669),
            fontWeight = FontWeight.SemiBold
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Row 3: Action Buttons (View, Verify, Suspend, Reactivate)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          // View Action
          OutlinedButton(
            onClick = { selectedWorkerForModal = worker },
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            modifier = Modifier.weight(1f).height(36.dp)
          ) {
            Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("View", fontSize = 11.sp)
          }

          // Verify Action (if pending)
          if (worker.verification == "Pending Verification") {
            Button(
              onClick = {
                workersList = workersList.map {
                  if (it.id == worker.id) it.copy(verification = "Verified Member", availability = "Available") else it
                }
                snackbarMessage = "${worker.name} has been verified by the Cooperative Board."
              },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
              modifier = Modifier.weight(1f).height(36.dp)
            ) {
              Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Verify", fontSize = 11.sp)
            }
          }

          // Suspend / Reactivate Actions
          if (worker.verification != "Suspended") {
            OutlinedButton(
              onClick = {
                workersList = workersList.map {
                  if (it.id == worker.id) it.copy(verification = "Suspended", availability = "Offline") else it
                }
                snackbarMessage = "${worker.name} was placed on temporary suspension."
              },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
              modifier = Modifier.weight(1f).height(36.dp)
            ) {
              Icon(Icons.Default.Block, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Suspend", fontSize = 11.sp)
            }
          } else {
            Button(
              onClick = {
                workersList = workersList.map {
                  if (it.id == worker.id) it.copy(verification = "Verified Member", availability = "Available") else it
                }
                snackbarMessage = "${worker.name} has been reactivated to active roster."
              },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = HomezyPrimary),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
              modifier = Modifier.weight(1f).height(36.dp)
            ) {
              Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Reactivate", fontSize = 11.sp)
            }
          }
        }
      }
    }
  }

  // Worker Detail Modal / Dialog
  selectedWorkerForModal?.let { worker ->
    AlertDialog(
      onDismissRequest = { selectedWorkerForModal = null },
      title = {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(worker.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
          HomezyBadge(
            text = worker.id,
            containerColor = HomezyPrimaryContainer,
            contentColor = HomezyPrimary
          )
        }
      },
      text = {
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text("Cooperative Affiliation", fontSize = 12.sp, color = HomezyTextSecondary)
          Text(worker.cooperative, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = HomezyText)

          Divider(color = HomezyBorder)

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text("Skill & Trade", fontSize = 11.sp, color = HomezyTextSecondary)
              Text("${worker.skill} (${worker.skillLevel})", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
              Text("Experience", fontSize = 11.sp, color = HomezyTextSecondary)
              Text("${worker.experienceYears} Years", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
          }

          Divider(color = HomezyBorder)

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text("Rating", fontSize = 11.sp, color = HomezyTextSecondary)
              Text("★ ${worker.rating} / 5.0", fontWeight = FontWeight.Bold, color = HomezySecondary)
            }
            Column(horizontalAlignment = Alignment.End) {
              Text("Jobs Completed", fontSize = 11.sp, color = HomezyTextSecondary)
              Text("${worker.completedJobs} Jobs", fontWeight = FontWeight.Bold, color = HomezyPrimary)
            }
          }

          Divider(color = HomezyBorder)

          Text("Welfare & Health Protection", fontSize = 11.sp, color = HomezyTextSecondary)
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFFDCFCE7),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFF15803D), modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = worker.insuranceStatus,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF15803D)
              )
            }
          }
        }
      },
      confirmButton = {
        HomezyButton(
          text = "Close",
          onClick = { selectedWorkerForModal = null },
          variant = ButtonVariant.PRIMARY
        )
      }
    )
  }
}
