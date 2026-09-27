package com.example.ui.worker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.InstaHelpRequest
import com.example.data.JobOffer
import com.example.data.JobOfferStatus
import com.example.ui.components.HomezyCard
import com.example.ui.theme.*

@Composable
fun WorkerDashboardScreen(
  incomingJobs: List<JobOffer>,
  instaHelpRequests: List<InstaHelpRequest> = emptyList(),
  onAcceptJob: (JobOffer) -> Unit,
  onDeclineJob: (JobOffer) -> Unit,
  onAcceptInstaHelp: (InstaHelpRequest) -> Unit = {},
  onDeclineInstaHelp: (InstaHelpRequest) -> Unit = {},
  onNavigateToFairAllocation: () -> Unit = {},
  onNavigateToFairPrice: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var isAvailable by remember { mutableStateOf(true) }

  val nextJob = incomingJobs.firstOrNull { it.status == JobOfferStatus.NEW }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("worker_dashboard_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Top Greeting & Availability
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Good morning, Rahul 👋",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Text(
            text = "Electrician • Member #0429",
            fontSize = 12.sp,
            color = HomezyTextSecondary
          )
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = if (isAvailable) Color(0xFFDCFCE7) else Color(0xFFFEF3C7)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (isAvailable) Color(0xFF16A34A) else Color(0xFFD97706))
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (isAvailable) "Available" else "Busy",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = if (isAvailable) Color(0xFF166534) else Color(0xFF92400E)
            )
          }
        }
      }
    }

    // 1. Initial Screen Metrics: Today's Jobs, Today's Earnings, Availability
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text("Today's Jobs", fontSize = 12.sp, color = HomezyTextSecondary)
            Text("3 Completed", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = HomezyText)
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Today's Earnings", fontSize = 12.sp, color = HomezyTextSecondary)
            Text("₹1,420", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = HomezyPrimary)
          }

          Column(horizontalAlignment = Alignment.End) {
            Text("Availability", fontSize = 12.sp, color = HomezyTextSecondary)
            Switch(
              checked = isAvailable,
              onCheckedChange = { isAvailable = it },
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = HomezyPrimary
              ),
              modifier = Modifier.testTag("worker_availability_toggle")
            )
          }
        }
      }
    }

    // Insta Help Emergency Requests (if any pending)
    if (instaHelpRequests.isNotEmpty()) {
      item {
        Text(
          text = "Urgent Insta Help Requests",
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFFDC2626)
        )
      }

      items(instaHelpRequests) { req ->
        HomezyCard(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("worker_insta_help_card_${req.id}"),
          borderColor = Color(0xFFFCA5A5)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFFFEE2E2)
            ) {
              Text(
                text = "INSTA HELP REQUEST",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDC2626),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
            Text(
              text = req.urgency,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFFDC2626)
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = req.service,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = HomezyText
              )
              Text(
                text = "${req.workerDistanceKm} km • ${req.location}",
                fontSize = 13.sp,
                color = HomezyTextSecondary
              )
            }
            Text(
              text = "₹${req.estimatedEarnings}",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyPrimary
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedButton(
              onClick = { onDeclineInstaHelp(req) },
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text("Decline", fontSize = 12.sp, color = HomezyTextSecondary)
            }
            Button(
              onClick = { onAcceptInstaHelp(req) },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
              modifier = Modifier
                .weight(1f)
                .testTag("accept_insta_help_${req.id}")
            ) {
              Text("Accept Now", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
          }
        }
      }
    }

    // 2. Next Job Section (Clean, essential info only, details on demand)
    item {
      Text(
        text = "Next Job",
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
    }

    if (nextJob != null) {
      item {
        var showJobDetails by remember { mutableStateOf(false) }

        HomezyCard(modifier = Modifier.fillMaxWidth().testTag("next_job_card")) {
          // Essential Info: Service, Customer area, Time, Distance, Price
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = nextJob.service,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = HomezyText
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Area: ${nextJob.customerArea}",
                fontSize = 13.sp,
                color = HomezyTextSecondary
              )
              Text(
                text = "Time: ${nextJob.scheduledTime} • ${nextJob.distance}",
                fontSize = 13.sp,
                color = HomezyTextSecondary
              )
            }

            Text(
              text = "₹${nextJob.workerNet}",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyPrimary
            )
          }

          // Expandable: Additional information goes inside "View Details"
          AnimatedVisibility(visible = showJobDetails) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(HomezySurfaceVariant)
                .padding(10.dp),
              verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Text("• Customer: ${nextJob.customerName}", fontSize = 12.sp, color = HomezyText)
              Text("• Problem: ${nextJob.problem}", fontSize = 12.sp, color = HomezyText)
              Text("• Co-op Rate Guarantee: Full insurance coverage active", fontSize = 12.sp, color = HomezySecondary)
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Buttons: [ Accept ] and [ View Details ]
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedButton(
              onClick = { showJobDetails = !showJobDetails },
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1f).testTag("view_job_details_btn"),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = HomezyPrimary)
            ) {
              Text(
                text = if (showJobDetails) "Hide Details" else "View Details",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }

            Button(
              onClick = { onAcceptJob(nextJob) },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = HomezyPrimary),
              modifier = Modifier.weight(1f).testTag("accept_job_btn")
            ) {
              Text(
                text = "Accept",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }
        }
      }
    } else {
      item {
        HomezyCard(modifier = Modifier.fillMaxWidth()) {
          Text(
            text = "No pending jobs right now. You are set to receive upcoming cooperative dispatches.",
            fontSize = 13.sp,
            color = HomezyTextSecondary
          )
        }
      }
    }
  }
}
