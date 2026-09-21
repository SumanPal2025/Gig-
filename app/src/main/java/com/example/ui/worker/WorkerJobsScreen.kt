package com.example.ui.worker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.data.BookingStatus
import com.example.data.HomezyRepository
import com.example.data.SampleData
import com.example.data.WorkerActiveJob
import com.example.data.WorkerJobStatus
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun WorkerJobsScreen(
  modifier: Modifier = Modifier
) {
  // Derive worker active jobs from HomezyRepository.bookings
  val repositoryBookings = HomezyRepository.bookings
  val dynamicJobs = remember(repositoryBookings) {
    val existingJobIds = SampleData.initialWorkerActiveJobs.map { it.id }.toSet()
    val fromBookings = repositoryBookings.filter { it.id !in existingJobIds }.map { b ->
      val wStatus = when (b.status) {
        BookingStatus.COMPLETED, BookingStatus.CANCELLED -> WorkerJobStatus.COMPLETED
        BookingStatus.IN_PROGRESS -> WorkerJobStatus.IN_PROGRESS
        else -> WorkerJobStatus.ACCEPTED
      }
      WorkerActiveJob(
        id = b.id,
        service = b.serviceName,
        customerName = b.customerName,
        customerPhone = b.workerPhone,
        customerArea = b.customerAddress.split(",").firstOrNull()?.trim() ?: "Bengaluru",
        address = b.customerAddress,
        distance = "1.5 km",
        scheduledDate = b.date,
        scheduledTime = b.timeSlot,
        problemDescription = b.problemDescription,
        agreedPrice = b.price,
        workerEarnings = if (b.workerEarnings > 0) b.workerEarnings else (b.price * 0.95).toInt(),
        cooperativeContribution = if (b.cooperativeContribution > 0) b.cooperativeContribution else (b.price * 0.03).toInt(),
        platformFee = if (b.platformFee > 0) b.platformFee else (b.price * 0.02).toInt(),
        paymentStatus = b.paymentStatus,
        status = wStatus
      )
    }
    SampleData.initialWorkerActiveJobs + fromBookings
  }

  var jobsList by remember(dynamicJobs) { mutableStateOf(dynamicJobs) }
  var selectedTab by remember { mutableStateOf(0) } // 0: Active & Upcoming, 1: Completed
  var selectedJobId by remember { mutableStateOf<String?>(jobsList.firstOrNull()?.id) }
  var completionBannerJobId by remember { mutableStateOf<String?>(null) }

  val activeJobs = jobsList.filter { it.status != WorkerJobStatus.COMPLETED }
  val completedJobs = jobsList.filter { it.status == WorkerJobStatus.COMPLETED }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("worker_jobs_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header
    item {
      Column {
        Text(
          text = "Jobs Management",
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Text(
          text = "Track active assignments, advance job status, and manage checklists",
          fontSize = 13.sp,
          color = HomezyTextSecondary
        )
      }
    }

    // Tab Selector: Active vs Completed
    item {
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = HomezySurfaceVariant,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(modifier = Modifier.padding(4.dp)) {
          TabButton(
            text = "Active & Upcoming (${activeJobs.size})",
            isSelected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            modifier = Modifier.weight(1f)
          )
          TabButton(
            text = "Completed (${completedJobs.size})",
            isSelected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    if (selectedTab == 0) {
      if (activeJobs.isEmpty()) {
        item {
          EmptyState(
            title = "No active jobs right now",
            description = "You don't have any ongoing or scheduled jobs. Turn your availability ON in Dashboard to receive new assignments.",
            icon = Icons.Default.WorkOutline,
            modifier = Modifier.padding(vertical = 24.dp)
          )
        }
      } else {
        items(activeJobs) { job ->
          WorkerJobDetailCard(
            job = job,
            onAdvanceStatus = { nextStatus ->
              jobsList = jobsList.map {
                if (it.id == job.id) it.copy(status = nextStatus) else it
              }
              if (nextStatus == WorkerJobStatus.COMPLETED) {
                completionBannerJobId = job.id
              }
              // Sync with HomezyRepository
              val existing = HomezyRepository.bookings.find { it.id == job.id }
              if (existing != null) {
                val bStatus = when (nextStatus) {
                  WorkerJobStatus.COMPLETED -> BookingStatus.COMPLETED
                  WorkerJobStatus.IN_PROGRESS, WorkerJobStatus.ON_THE_WAY, WorkerJobStatus.ARRIVED -> BookingStatus.IN_PROGRESS
                  else -> BookingStatus.ACCEPTED
                }
                HomezyRepository.updateBooking(existing.copy(status = bStatus))
              }
            },
            onToggleChecklist = { index, isChecked ->
              jobsList = jobsList.map {
                if (it.id == job.id) {
                  val updated = if (isChecked) {
                    it.completedChecklistIndices + index
                  } else {
                    it.completedChecklistIndices - index
                  }
                  it.copy(completedChecklistIndices = updated)
                } else it
              }
            }
          )
        }
      }
    } else {
      if (completedJobs.isEmpty()) {
        item {
          EmptyState(
            title = "No completed jobs yet today",
            description = "Complete active jobs to view your job history and instant earnings transfers here.",
            icon = Icons.Default.CheckCircleOutline,
            modifier = Modifier.padding(vertical = 24.dp)
          )
        }
      } else {
        items(completedJobs) { job ->
          CompletedJobCard(job = job)
        }
      }
    }
  }
}

@Composable
private fun TabButton(
  text: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val bgColor by animateColorAsState(
    targetValue = if (isSelected) HomezyCard else Color.Transparent,
    animationSpec = tween(150),
    label = "tab_bg"
  )
  val textColor by animateColorAsState(
    targetValue = if (isSelected) HomezyPrimary else HomezyTextSecondary,
    animationSpec = tween(150),
    label = "tab_text"
  )

  Surface(
    shape = RoundedCornerShape(9.dp),
    color = bgColor,
    modifier = modifier
      .clip(RoundedCornerShape(9.dp))
      .clickable { onClick() }
  ) {
    Box(
      modifier = Modifier.padding(vertical = 9.dp),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        color = textColor
      )
    }
  }
}

@Composable
fun WorkerJobDetailCard(
  job: WorkerActiveJob,
  onAdvanceStatus: (WorkerJobStatus) -> Unit,
  onToggleChecklist: (Int, Boolean) -> Unit
) {
  HomezyCard(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("worker_job_card_${job.id}"),
    elevation = 3.dp,
    borderColor = if (job.status == WorkerJobStatus.IN_PROGRESS) HomezyPrimary else HomezyBorder
  ) {
    // Top Bar: Status Badge & Job ID
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      HomezyBadge(
        text = job.status.label.uppercase(),
        containerColor = when (job.status) {
          WorkerJobStatus.ACCEPTED -> Color(0xFFEFF6FF)
          WorkerJobStatus.ON_THE_WAY -> Color(0xFFFEF3C7)
          WorkerJobStatus.ARRIVED -> Color(0xFFEDE9FE)
          WorkerJobStatus.IN_PROGRESS -> HomezyPrimaryContainer
          WorkerJobStatus.COMPLETED -> Color(0xFFDCFCE7)
        },
        contentColor = when (job.status) {
          WorkerJobStatus.ACCEPTED -> Color(0xFF1D4ED8)
          WorkerJobStatus.ON_THE_WAY -> Color(0xFFB45309)
          WorkerJobStatus.ARRIVED -> Color(0xFF6D28D9)
          WorkerJobStatus.IN_PROGRESS -> HomezyPrimary
          WorkerJobStatus.COMPLETED -> Color(0xFF15803D)
        },
        icon = when (job.status) {
          WorkerJobStatus.ACCEPTED -> Icons.Default.Check
          WorkerJobStatus.ON_THE_WAY -> Icons.Default.DirectionsCar
          WorkerJobStatus.ARRIVED -> Icons.Default.Place
          WorkerJobStatus.IN_PROGRESS -> Icons.Default.Build
          WorkerJobStatus.COMPLETED -> Icons.Default.CheckCircle
        }
      )

      Text(
        text = "Job #${job.id}",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyTextSecondary
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Service Name
    Text(
      text = job.service,
      fontSize = 18.sp,
      fontWeight = FontWeight.Bold,
      color = HomezyText
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Customer & Phone
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = null,
          tint = HomezyTextSecondary,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = job.customerName,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
      }

      Surface(
        shape = RoundedCornerShape(8.dp),
        color = HomezySurfaceVariant
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Phone,
            contentDescription = null,
            tint = HomezyPrimary,
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = job.customerPhone,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = HomezyPrimary
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(6.dp))

    // Address
    Row(verticalAlignment = Alignment.Top) {
      Icon(
        imageVector = Icons.Default.LocationOn,
        contentDescription = null,
        tint = HomezyTextSecondary,
        modifier = Modifier
          .size(16.dp)
          .padding(top = 2.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Column {
        Text(
          text = job.address,
          fontSize = 13.sp,
          color = HomezyText,
          lineHeight = 16.sp
        )
        Text(
          text = "${job.distance} away • ${job.scheduledDate}, ${job.scheduledTime}",
          fontSize = 11.sp,
          color = HomezyTextSecondary
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Problem Description
    Surface(
      shape = RoundedCornerShape(8.dp),
      color = HomezySurfaceVariant,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(10.dp)) {
        Text(
          text = "Problem Reported:",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyTextSecondary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = job.problemDescription,
          fontSize = 12.sp,
          color = HomezyText,
          lineHeight = 16.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Price & Payment Status
    Surface(
      shape = RoundedCornerShape(8.dp),
      color = Color(0xFFF0FDF4),
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text("Agreed Price", fontSize = 11.sp, color = HomezyTextSecondary)
          Text(
            text = "₹${job.agreedPrice}",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = HomezyText
          )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text("Payment Status", fontSize = 11.sp, color = HomezyTextSecondary)
          Text(
            text = job.paymentStatus,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF15803D)
          )
        }
        Column(horizontalAlignment = Alignment.End) {
          Text("Your Net (95%)", fontSize = 11.sp, color = HomezyTextSecondary)
          Text(
            text = "₹${job.workerEarnings}",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF15803D)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // 5-STEP JOB STATUS PROGRESSION TRACKER
    Text(
      text = "Job Status Progression:",
      fontSize = 12.sp,
      fontWeight = FontWeight.Bold,
      color = HomezyText
    )
    Spacer(modifier = Modifier.height(8.dp))

    JobStatusStepper(currentStatus = job.status)

    Spacer(modifier = Modifier.height(14.dp))

    // Interactive Checklist
    if (job.checklist.isNotEmpty()) {
      Text(
        text = "Quality Checklist (${job.completedChecklistIndices.size}/${job.checklist.size}):",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
      Spacer(modifier = Modifier.height(6.dp))

      job.checklist.forEachIndexed { index, item ->
        val isDone = job.completedChecklistIndices.contains(index)
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleChecklist(index, !isDone) }
            .padding(vertical = 3.dp)
        ) {
          Checkbox(
            checked = isDone,
            onCheckedChange = { onToggleChecklist(index, it) },
            colors = CheckboxDefaults.colors(checkedColor = HomezyPrimary)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = item,
            fontSize = 12.sp,
            color = if (isDone) HomezyTextSecondary else HomezyText,
            fontWeight = if (isDone) FontWeight.Normal else FontWeight.Medium
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // OBVIOUS NEXT ACTION BUTTON
    when (job.status) {
      WorkerJobStatus.ACCEPTED -> {
        HomezyButton(
          text = "Start Journey (On the Way)",
          onClick = { onAdvanceStatus(WorkerJobStatus.ON_THE_WAY) },
          variant = ButtonVariant.PRIMARY,
          icon = Icons.Default.DirectionsCar,
          fullWidth = true,
          testTag = "btn_start_journey_${job.id}"
        )
      }
      WorkerJobStatus.ON_THE_WAY -> {
        HomezyButton(
          text = "I Have Arrived at Location",
          onClick = { onAdvanceStatus(WorkerJobStatus.ARRIVED) },
          variant = ButtonVariant.PRIMARY,
          icon = Icons.Default.Place,
          fullWidth = true,
          testTag = "btn_arrived_${job.id}"
        )
      }
      WorkerJobStatus.ARRIVED -> {
        HomezyButton(
          text = "Start Service (In Progress)",
          onClick = { onAdvanceStatus(WorkerJobStatus.IN_PROGRESS) },
          variant = ButtonVariant.PRIMARY,
          icon = Icons.Default.PlayArrow,
          fullWidth = true,
          testTag = "btn_start_service_${job.id}"
        )
      }
      WorkerJobStatus.IN_PROGRESS -> {
        HomezyButton(
          text = "Complete Job & Collect Payment",
          onClick = { onAdvanceStatus(WorkerJobStatus.COMPLETED) },
          variant = ButtonVariant.PRIMARY,
          icon = Icons.Default.CheckCircle,
          fullWidth = true,
          testTag = "btn_complete_job_${job.id}"
        )
      }
      WorkerJobStatus.COMPLETED -> {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFFDCFCE7),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF15803D), modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Job Completed! ₹${job.workerEarnings} transferred to your Co-op Wallet.",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF15803D)
            )
          }
        }
      }
    }
  }
}

@Composable
fun JobStatusStepper(
  currentStatus: WorkerJobStatus
) {
  val steps = WorkerJobStatus.values()
  val currentIndex = currentStatus.stepIndex

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    steps.forEachIndexed { index, step ->
      val isPastOrCurrent = index <= currentIndex
      val isCurrent = index == currentIndex

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
      ) {
        Box(
          modifier = Modifier
            .size(26.dp)
            .clip(CircleShape)
            .background(
              if (isCurrent) HomezyPrimary
              else if (isPastOrCurrent) HomezySecondary
              else HomezyBorder
            ),
          contentAlignment = Alignment.Center
        ) {
          if (index < currentIndex) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(14.dp)
            )
          } else {
            Text(
              text = "${index + 1}",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = if (isPastOrCurrent) Color.White else HomezyTextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = step.label,
          fontSize = 9.sp,
          fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
          color = if (isCurrent) HomezyPrimary else if (isPastOrCurrent) HomezyText else HomezyTextSecondary,
          maxLines = 1
        )
      }

      if (index < steps.size - 1) {
        Box(
          modifier = Modifier
            .weight(0.5f)
            .height(2.dp)
            .background(if (index < currentIndex) HomezySecondary else HomezyBorder)
        )
      }
    }
  }
}

@Composable
fun CompletedJobCard(job: WorkerActiveJob) {
  HomezyCard(
    modifier = Modifier.fillMaxWidth(),
    elevation = 1.dp
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = job.service,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Text(
          text = "Customer: ${job.customerName} • ${job.customerArea}",
          fontSize = 12.sp,
          color = HomezyTextSecondary
        )
      }

      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = "+₹${job.workerEarnings}",
          fontSize = 16.sp,
          fontWeight = FontWeight.ExtraBold,
          color = Color(0xFF15803D)
        )
        Text(
          text = "Settled ✓",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = HomezySecondary
        )
      }
    }

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = "${job.scheduledDate}, ${job.scheduledTime} • ${job.address}",
      fontSize = 11.sp,
      color = HomezyTextSecondary
    )
  }
}
