package com.example.ui.worker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.example.data.InstaHelpStatus
import com.example.data.JobOffer
import com.example.data.JobOfferStatus
import com.example.ui.components.*
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

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("worker_dashboard_screen"),
    contentPadding = PaddingValues(bottom = 24.dp)
  ) {
    // Top Greeting Area
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(HomezyCard)
          .padding(16.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(HomezyPrimary),
              contentAlignment = Alignment.Center
            ) {
              Text("RS", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Good morning, Rahul 👋",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = HomezyText
              )
              Text(
                text = "Co-op Member #0429 • Electrician",
                fontSize = 12.sp,
                color = HomezyTextSecondary
              )
            }
          }

          HomezyBadge(
            text = "COOPERATIVE MEMBER",
            containerColor = HomezyPrimaryContainer,
            contentColor = HomezyPrimary
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Availability Toggle
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = if (isAvailable) HomezyPrimaryContainer else HomezySurfaceVariant,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(10.dp)
                  .clip(CircleShape)
                  .background(if (isAvailable) HomezySecondary else Color.Gray)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Available for work",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = HomezyText
                )
                Text(
                  text = if (isAvailable) "Receiving incoming cooperative leads" else "Lead dispatch paused",
                  fontSize = 11.sp,
                  color = HomezyTextSecondary
                )
              }
            }

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

    // 4 Key Worker Statistics: Jobs Today, Today's Earnings, Rating, Hours Worked
    item {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "Today's Performance",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          StatCard(
            title = "Jobs Today",
            value = "3",
            subtext = "2 Completed • 1 Active",
            icon = Icons.Default.Work,
            modifier = Modifier.weight(1f)
          )
          StatCard(
            title = "Today's Earnings",
            value = "₹1,420",
            badgeText = "95% Net",
            isPositive = true,
            icon = Icons.Default.AccountBalanceWallet,
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          StatCard(
            title = "Rating",
            value = "4.92 ★",
            subtext = "642 reviews",
            icon = Icons.Default.Star,
            modifier = Modifier.weight(1f)
          )
          StatCard(
            title = "Hours Worked",
            value = "5.5 hrs",
            subtext = "Safe rest limit 8h",
            icon = Icons.Default.AccessTime,
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    // Fair Work Allocation Banner (Phase 5 Feature)
    item {
      Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        Card(
          onClick = onNavigateToFairAllocation,
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("worker_fair_allocation_banner")
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Balance,
                  contentDescription = null,
                  tint = Color(0xFF15803D),
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Fair Work Allocation",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF166534)
                )
              }
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFDCFCE7)
              ) {
                Text(
                  text = "PROTOTYPE ALGORITHM",
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF15803D),
                  modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Exact required explanation
            Text(
              text = "HOMEZY considers workload and availability so qualified workers receive fair opportunities.",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF14532D),
              lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 5 Key metrics in a compact preview row
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color.White,
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text("Jobs", fontSize = 10.sp, color = HomezyTextSecondary)
                  Text("6", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = HomezyText)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text("Hours", fontSize = 10.sp, color = HomezyTextSecondary)
                  Text("18.5h", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = HomezyText)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text("Workload", fontSize = 10.sp, color = HomezyTextSecondary)
                  Text("Low", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text("Idle", fontSize = 10.sp, color = HomezyTextSecondary)
                  Text("3.5h", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = HomezyText)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text("Earnings", fontSize = 10.sp, color = HomezyTextSecondary)
                  Text("₹4,250", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF059669))
                }
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.End,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "View Allocation Details",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF15803D)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color(0xFF15803D),
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }
      }
    }

    // Fair Price Negotiation Assistant Banner (Phase 6 Feature)
    item {
      Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToFairPrice() }
            .testTag("worker_fair_price_banner")
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF2563EB)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Handshake,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "Fair Price Assistant",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1E3A8A)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = Color(0xFFDBEAFE)
                ) {
                  Text(
                    text = "AI-ASSISTED",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D4ED8),
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                  )
                }
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Review customer offers, see suggested fair range, and send counter-offers.",
                fontSize = 11.sp,
                color = Color(0xFF1E40AF),
                lineHeight = 15.sp
              )
            }
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              tint = Color(0xFF1D4ED8),
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }

    // Phase 10: INSTA HELP Emergency Requests Section
    val pendingInstaHelp = instaHelpRequests.filter { it.status == InstaHelpStatus.PENDING }
    if (pendingInstaHelp.isNotEmpty()) {
      item {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = Color(0xFFDC2626),
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "INSTA HELP REQUESTS",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = Color(0xFFDC2626)
              )
            }
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = Color(0xFFFEE2E2)
            ) {
              Text(
                text = "${pendingInstaHelp.size} Urgent",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDC2626),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }
        }
      }

      items(pendingInstaHelp) { request ->
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
          WorkerInstaHelpCard(
            request = request,
            onAccept = { onAcceptInstaHelp(request) },
            onDecline = { onDeclineInstaHelp(request) }
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(10.dp))
      }
    }

    // Incoming Job Cards
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "Incoming Job Offers",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = HomezyText
          )
          Spacer(modifier = Modifier.width(8.dp))
          Surface(
            shape = CircleShape,
            color = HomezyAccent
          ) {
            Text(
              text = "${incomingJobs.count { it.status == JobOfferStatus.NEW }}",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
            )
          }
        }
        Text(
          text = "Equitable Dispatch",
          fontSize = 12.sp,
          color = HomezySecondary,
          fontWeight = FontWeight.SemiBold
        )
      }
    }

    val newJobs = incomingJobs.filter { it.status == JobOfferStatus.NEW }

    if (newJobs.isEmpty()) {
      item {
        EmptyState(
          title = "All caught up!",
          description = "No new incoming job alerts right now. Keep your availability switch ON to receive nearby requests.",
          icon = Icons.Default.DoneAll,
          modifier = Modifier.padding(vertical = 24.dp)
        )
      }
    } else {
      items(newJobs) { job ->
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
          IncomingJobCard(
            job = job,
            onAccept = { onAcceptJob(job) },
            onDecline = { onDeclineJob(job) }
          )
        }
      }
    }
  }
}

@Composable
fun IncomingJobCard(
  job: JobOffer,
  onAccept: () -> Unit,
  onDecline: () -> Unit
) {
  HomezyCard(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("incoming_job_card_${job.id}"),
    elevation = 3.dp,
    borderColor = HomezyPrimary.copy(alpha = 0.35f)
  ) {
    // Top Row: Service name & AI Match Badge
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = job.service,
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Text(
          text = "Customer: ${job.customerName} • ${job.customerArea}",
          fontSize = 12.sp,
          color = HomezyTextSecondary
        )
      }

      // AI Match Score pill
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFEFF6FF),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = Color(0xFF2563EB),
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "AI Match: ${job.aiMatchScore}%",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D4ED8)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Job Details Row: Distance, Date & Time, Price
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Distance
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = HomezySurfaceVariant,
        modifier = Modifier.weight(1f)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.NearMe,
            contentDescription = null,
            tint = HomezyTextSecondary,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = job.distance,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = HomezyText
          )
        }
      }

      // Date & Time (e.g. Today — 5:30 PM)
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = HomezySurfaceVariant,
        modifier = Modifier.weight(1.3f)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = null,
            tint = HomezyTextSecondary,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "${job.date} — ${job.scheduledTime}",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = HomezyText
          )
        }
      }

      // Price (e.g. ₹650)
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = HomezyPrimaryContainer,
        modifier = Modifier.weight(1f)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text(
            text = "₹${job.price}",
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = HomezyPrimary
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Address & Problem Note
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = Icons.Default.LocationOn,
        contentDescription = null,
        tint = HomezyTextSecondary,
        modifier = Modifier.size(14.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = job.address,
        fontSize = 12.sp,
        color = HomezyTextSecondary,
        maxLines = 1
      )
    }

    if (job.problem.isNotEmpty()) {
      Spacer(modifier = Modifier.height(4.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Info,
          contentDescription = null,
          tint = HomezyTextSecondary,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "Issue: ${job.problem}",
          fontSize = 11.sp,
          color = HomezyTextSecondary,
          maxLines = 1
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Payout Breakdown Preview
    Surface(
      shape = RoundedCornerShape(8.dp),
      color = HomezySurfaceVariant,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("Co-op Fee (5%): -₹${job.cooperativeFee}", fontSize = 11.sp, color = HomezyTextSecondary)
        Text(
          text = "Your Net: ₹${job.workerNet}",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyPrimary
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Action Buttons: Decline & Accept
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      HomezyButton(
        text = "Decline",
        onClick = onDecline,
        variant = ButtonVariant.OUTLINE,
        modifier = Modifier.weight(1f),
        testTag = "decline_job_${job.id}"
      )

      HomezyButton(
        text = "Accept",
        onClick = onAccept,
        variant = ButtonVariant.PRIMARY,
        icon = Icons.Default.Check,
        modifier = Modifier.weight(1f),
        testTag = "accept_job_${job.id}"
      )
    }
  }
}

@Composable
fun WorkerInstaHelpCard(
  request: InstaHelpRequest,
  onAccept: () -> Unit,
  onDecline: () -> Unit
) {
  HomezyCard(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("worker_insta_help_card_${request.id}"),
    elevation = 4.dp,
    borderColor = Color(0xFFDC2626),
    backgroundColor = Color(0xFFFFF5F5)
  ) {
    Column {
      // Header: "INSTA HELP REQUEST" & Urgency badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(Color(0xFFDC2626)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Bolt,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(18.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "INSTA HELP REQUEST",
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFDC2626)
          )
        }

        // Urgency Tag
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = Color(0xFFFEE2E2),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA))
        ) {
          Text(
            text = request.urgency,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFDC2626),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Service Title
      Text(
        text = request.service,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Key Metrics: Urgency, Distance, Estimated Earnings
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color.White, RoundedCornerShape(8.dp))
          .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Distance
        Column {
          Text("Distance", fontSize = 10.sp, color = HomezyTextSecondary)
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.NearMe, contentDescription = null, tint = HomezyPrimary, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = "${request.workerDistanceKm} km",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyText
            )
          }
        }

        Divider(modifier = Modifier.height(26.dp).width(1.dp), color = HomezyBorder)

        // Estimated Arrival
        Column {
          Text("ETA", fontSize = 10.sp, color = HomezyTextSecondary)
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Schedule, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = "${request.workerEtaMinutes} min",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1D4ED8)
            )
          }
        }

        Divider(modifier = Modifier.height(26.dp).width(1.dp), color = HomezyBorder)

        // Estimated Earnings
        Column {
          Text("Estimated Earnings", fontSize = 10.sp, color = HomezyTextSecondary)
          Text(
            text = "₹${request.estimatedEarnings}",
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF15803D)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Location & Customer Info
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = null,
          tint = Color(0xFFDC2626),
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "${request.location} • Customer: ${request.customerName}",
          fontSize = 12.sp,
          color = HomezyTextSecondary,
          maxLines = 1
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Buttons: Decline and Accept
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedButton(
          onClick = onDecline,
          modifier = Modifier
            .weight(1f)
            .height(42.dp)
            .testTag("decline_insta_help_${request.id}"),
          shape = RoundedCornerShape(8.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = HomezyTextSecondary)
        ) {
          Text("Decline", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }

        Button(
          onClick = onAccept,
          modifier = Modifier
            .weight(1.3f)
            .height(42.dp)
            .testTag("accept_insta_help_${request.id}"),
          shape = RoundedCornerShape(8.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D))
        ) {
          Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Accept", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
