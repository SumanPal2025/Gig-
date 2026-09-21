package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookingItem
import com.example.data.BookingStatus
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun CustomerBookingsScreen(
  bookings: List<BookingItem>,
  onChatWithWorker: (BookingItem) -> Unit,
  onExploreServices: () -> Unit,
  onUpdateBooking: (BookingItem) -> Unit = {},
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(0) } // 0: Active, 1: Past
  var selectedBookingForInvoice by remember { mutableStateOf<BookingItem?>(null) }
  var ratingBooking by remember { mutableStateOf<BookingItem?>(null) }
  var issueBooking by remember { mutableStateOf<BookingItem?>(null) }
  var issueReasonText by remember { mutableStateOf("") }

  // Rating modal state
  var ratingStars by remember { mutableStateOf(5) }
  var ratingCommentText by remember { mutableStateOf("Great service, highly professional and on-time!") }

  val activeBookings = bookings.filter { it.status != BookingStatus.COMPLETED && it.status != BookingStatus.CANCELLED }
  val pastBookings = bookings.filter { it.status == BookingStatus.COMPLETED || it.status == BookingStatus.CANCELLED }

  val displayedBookings = if (selectedTab == 0) activeBookings else pastBookings

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("customer_bookings_screen")
  ) {
    LazyColumn(
      contentPadding = PaddingValues(bottom = 24.dp)
    ) {
      // Header
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(HomezyCard)
            .padding(16.dp)
        ) {
          Text(
            text = "My Service Bookings",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Track appointments, verified workers, and cooperative warranties",
            fontSize = 13.sp,
            color = HomezyTextSecondary
          )
        }
      }

      // Tab switcher
      item {
        TabRow(
          selectedTabIndex = selectedTab,
          containerColor = HomezyCard,
          contentColor = HomezyPrimary,
          indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
              modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
              color = HomezyPrimary,
              height = 3.dp
            )
          }
        ) {
          Tab(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            text = {
              Text(
                text = "Active (${activeBookings.size})",
                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                fontSize = 14.sp
              )
            }
          )
          Tab(
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            text = {
              Text(
                text = "Completed (${pastBookings.size})",
                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                fontSize = 14.sp
              )
            }
          )
        }
      }

      if (displayedBookings.isEmpty()) {
        item {
          EmptyState(
            title = if (selectedTab == 0) "No active bookings" else "No past history",
            description = if (selectedTab == 0) "You do not have any ongoing home services right now. Need something fixed?" else "Completed bookings will appear here.",
            actionText = if (selectedTab == 0) "Explore Services" else null,
            onAction = onExploreServices,
            modifier = Modifier.padding(top = 40.dp)
          )
        }
      } else {
        items(displayedBookings) { booking ->
          Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            BookingCard(
              booking = booking,
              onChatClick = { onChatWithWorker(booking) },
              onInvoiceClick = { selectedBookingForInvoice = booking },
              onWorkerMarkCompleted = {
                onUpdateBooking(
                  booking.copy(
                    isWorkCompletedByWorker = true
                  )
                )
              },
              onCustomerVerifyCompleted = {
                val verified = booking.copy(
                  isCompletionVerified = true
                )
                onUpdateBooking(verified)
                ratingBooking = verified
              },
              onReportIssueClick = {
                issueBooking = booking
                issueReasonText = ""
              }
            )
          }
        }
      }
    }

    // Invoice Modal
    selectedBookingForInvoice?.let { booking ->
      val wEarnings = if (booking.workerEarnings > 0) booking.workerEarnings else (booking.price * 0.88).toInt().coerceAtLeast(100)
      val coopFee = if (booking.cooperativeContribution > 0) booking.cooperativeContribution else (booking.price * 0.08).toInt().coerceAtLeast(20)
      val platFee = if (booking.platformFee > 0) booking.platformFee else (booking.price - wEarnings - coopFee).coerceAtLeast(10)

      HomezyModal(
        visible = true,
        onDismissRequest = { selectedBookingForInvoice = null },
        title = "Co-op Service Invoice"
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Booking Ref", fontSize = 13.sp, color = HomezyTextSecondary)
            Text(booking.id, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = HomezyText)
          }
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Service", fontSize = 13.sp, color = HomezyTextSecondary)
            Text(booking.serviceName, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = HomezyText)
          }
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Worker Partner", fontSize = 13.sp, color = HomezyTextSecondary)
            Text(booking.workerName, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = HomezyPrimary)
          }
          Spacer(modifier = Modifier.height(12.dp))
          Divider(color = HomezyBorder)
          Spacer(modifier = Modifier.height(12.dp))

          // Transparent fee breakdown without fixed real-world percentages
          Text("Transparent Price Breakdown", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = HomezyText)
          Spacer(modifier = Modifier.height(8.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Service Price", fontSize = 12.sp, color = HomezyTextSecondary)
            Text("₹${booking.price}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
          }
          Spacer(modifier = Modifier.height(4.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Worker Earnings", fontSize = 12.sp, color = HomezyTextSecondary)
            Text("₹$wEarnings", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
          }
          Spacer(modifier = Modifier.height(4.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Cooperative Contribution", fontSize = 12.sp, color = HomezyTextSecondary)
            Text("₹$coopFee", fontSize = 12.sp, fontWeight = FontWeight.Medium)
          }
          Spacer(modifier = Modifier.height(4.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Platform/Service Fee", fontSize = 12.sp, color = HomezyTextSecondary)
            Text("₹$platFee", fontSize = 12.sp, fontWeight = FontWeight.Medium)
          }
          Spacer(modifier = Modifier.height(12.dp))
          Divider(color = HomezyBorder)
          Spacer(modifier = Modifier.height(12.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Total Paid", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = HomezyText)
            Text("₹${booking.price}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = HomezyPrimary)
          }
          Spacer(modifier = Modifier.height(18.dp))
          HomezyButton(
            text = "Done",
            onClick = { selectedBookingForInvoice = null },
            variant = ButtonVariant.PRIMARY,
            fullWidth = true
          )
        }
      }
    }

    // Rating Modal (False-Rating Mitigation: only triggered after verified completion)
    ratingBooking?.let { b ->
      HomezyModal(
        visible = true,
        onDismissRequest = { ratingBooking = null },
        title = "Verified Service Rating"
      ) {
        Column {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = HomezyPrimaryContainer,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = HomezyPrimary, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Protected by False-Rating Mitigation: Verified review for ${b.workerName}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = HomezyPrimary
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text("Rate Experience (1 to 5 Stars):", fontSize = 13.sp, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
          ) {
            (1..5).forEach { star ->
              IconButton(onClick = { ratingStars = star }) {
                Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = "$star stars",
                  tint = if (star <= ratingStars) Color(0xFFF59E0B) else Color(0xFFE2E8F0),
                  modifier = Modifier.size(34.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = ratingCommentText,
            onValueChange = { ratingCommentText = it },
            label = { Text("Comment") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
          )

          Spacer(modifier = Modifier.height(16.dp))
          HomezyButton(
            text = "Submit Verified Rating",
            onClick = {
              onUpdateBooking(
                b.copy(
                  status = BookingStatus.COMPLETED,
                  isWorkCompletedByWorker = true,
                  isCompletionVerified = true,
                  rating = ratingStars,
                  ratingComment = ratingCommentText
                )
              )
              ratingBooking = null
            },
            variant = ButtonVariant.PRIMARY,
            fullWidth = true
          )
        }
      }
    }

    // Report Issue Dialog
    issueBooking?.let { b ->
      AlertDialog(
        onDismissRequest = { issueBooking = null },
        title = { Text("Report Service Issue", fontWeight = FontWeight.Bold) },
        text = {
          Column {
            Text("Enter details for cooperative dispute committee review:", fontSize = 13.sp, color = HomezyTextSecondary)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
              value = issueReasonText,
              onValueChange = { issueReasonText = it },
              placeholder = { Text("e.g. Work left unfinished or leak still persists") },
              modifier = Modifier.fillMaxWidth()
            )
          }
        },
        confirmButton = {
          Button(
            onClick = {
              onUpdateBooking(
                b.copy(
                  isIssueReported = true,
                  reportedIssueText = issueReasonText
                )
              )
              issueBooking = null
            },
            colors = ButtonDefaults.buttonColors(containerColor = HomezyError)
          ) {
            Text("Submit Grievance")
          }
        },
        dismissButton = {
          TextButton(onClick = { issueBooking = null }) {
            Text("Cancel")
          }
        }
      )
    }
  }
}

@Composable
private fun BookingCard(
  booking: BookingItem,
  onChatClick: () -> Unit,
  onInvoiceClick: () -> Unit,
  onWorkerMarkCompleted: () -> Unit = {},
  onCustomerVerifyCompleted: () -> Unit = {},
  onReportIssueClick: () -> Unit = {}
) {
  HomezyCard(
    modifier = Modifier.fillMaxWidth(),
    elevation = 1.dp
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = booking.id,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyTextSecondary
      )
      StatusBadge(status = booking.status)
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = booking.serviceName,
      fontSize = 16.sp,
      fontWeight = FontWeight.Bold,
      color = HomezyText
    )

    Spacer(modifier = Modifier.height(4.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = Icons.Default.CalendarToday,
        contentDescription = null,
        tint = HomezyTextSecondary,
        modifier = Modifier.size(14.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = "${booking.date} • ${booking.timeSlot}",
        fontSize = 12.sp,
        color = HomezyTextSecondary
      )
    }

    Spacer(modifier = Modifier.height(4.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = Icons.Default.LocationOn,
        contentDescription = null,
        tint = HomezyTextSecondary,
        modifier = Modifier.size(14.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = booking.customerAddress,
        fontSize = 12.sp,
        color = HomezyTextSecondary,
        maxLines = 1
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Assigned worker banner
    Surface(
      shape = RoundedCornerShape(10.dp),
      color = HomezySurfaceVariant,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(HomezyPrimaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Person,
              contentDescription = null,
              tint = HomezyPrimary,
              modifier = Modifier.size(18.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = booking.workerName,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyText
            )
            Text(
              text = "Co-op Certified Partner",
              fontSize = 11.sp,
              color = HomezyPrimary
            )
          }
        }

        Text(
          text = "₹${booking.price}",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
      }
    }

    // WORK COMPLETION & VERIFICATION SECTION (Active Bookings)
    if (booking.status != BookingStatus.COMPLETED && booking.status != BookingStatus.CANCELLED) {
      Spacer(modifier = Modifier.height(12.dp))

      // If worker hasn't marked complete yet
      if (!booking.isWorkCompletedByWorker) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = HomezyPrimaryContainer.copy(alpha = 0.5f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Text(
              text = "Worker On-Site Simulation",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Simulate worker finishing the task to test customer completion verification:",
              fontSize = 11.sp,
              color = HomezyTextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            HomezyButton(
              text = "Worker Action: Mark Work as Completed",
              onClick = onWorkerMarkCompleted,
              variant = ButtonVariant.OUTLINE,
              fullWidth = true
            )
          }
        }
      } else {
        // Worker marked complete! Customer sees: "Was the work completed successfully?"
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFFF0FDF4),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Text(
              text = "Was the work completed successfully?",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF166534)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Worker has marked the service complete. Only after successful verification can rating be unlocked.",
              fontSize = 11.sp,
              color = HomezyTextSecondary
            )

            if (booking.isIssueReported) {
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "Issue reported: Ombudsman reviewing case. Rating disabled.",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = HomezyError
              )
            } else {
              Spacer(modifier = Modifier.height(8.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                HomezyButton(
                  text = "Report an Issue",
                  onClick = onReportIssueClick,
                  variant = ButtonVariant.OUTLINE,
                  modifier = Modifier.weight(1f)
                )
                HomezyButton(
                  text = "Yes, Work Completed",
                  onClick = onCustomerVerifyCompleted,
                  variant = ButtonVariant.PRIMARY,
                  modifier = Modifier.weight(1.3f)
                )
              }
            }
          }
        }
      }
    } else if (booking.rating != null) {
      // Completed & Rated
      Spacer(modifier = Modifier.height(8.dp))
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = HomezySurfaceVariant,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Verified Rating: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = HomezyText)
          Text("★ ${booking.rating}/5", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF59E0B))
          booking.ratingComment?.let { comment ->
            Text(" • \"$comment\"", fontSize = 11.sp, color = HomezyTextSecondary, maxLines = 1)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      HomezyButton(
        text = "View Invoice",
        onClick = onInvoiceClick,
        variant = ButtonVariant.OUTLINE,
        modifier = Modifier.weight(1f)
      )
      HomezyButton(
        text = "Message",
        onClick = onChatClick,
        variant = ButtonVariant.PRIMARY,
        icon = Icons.Default.Chat,
        modifier = Modifier.weight(1f)
      )
    }
  }
}
