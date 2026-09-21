package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookingStatus
import com.example.data.HomezyRepository
import com.example.ui.components.*
import com.example.ui.theme.*

data class AdminBookingRecord(
  val id: String,
  val customerName: String,
  val customerAddress: String,
  val workerName: String,
  val serviceName: String,
  val date: String,
  val timeSlot: String,
  val status: BookingStatus,
  val grossAmount: Int,
  val workerShare: Int
)

@Composable
fun AdminBookingsScreen(
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedFilter by remember { mutableStateOf<BookingStatus?>(null) }

  val allRecords = remember(HomezyRepository.bookings) {
    HomezyRepository.bookings.map { b ->
      AdminBookingRecord(
        id = b.id,
        customerName = b.customerName,
        customerAddress = b.customerAddress,
        workerName = b.workerName,
        serviceName = b.serviceName,
        date = b.date,
        timeSlot = b.timeSlot,
        status = b.status,
        grossAmount = b.price,
        workerShare = if (b.workerEarnings > 0) b.workerEarnings else (b.price * 0.95).toInt()
      )
    }
  }

  var bookingsList by remember(allRecords) { mutableStateOf(allRecords) }

  val filteredBookings = bookingsList.filter { booking ->
    val matchesFilter = selectedFilter == null || booking.status == selectedFilter
    val matchesSearch = searchQuery.isBlank() ||
      booking.id.contains(searchQuery, true) ||
      booking.customerName.contains(searchQuery, true) ||
      booking.workerName.contains(searchQuery, true) ||
      booking.serviceName.contains(searchQuery, true)

    matchesFilter && matchesSearch
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("admin_bookings_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header
    item {
      Column {
        Text(
          text = "Federation Booking Management",
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Text(
          text = "Real-time dispatch audit, escrow lifecycle, and SLA monitoring",
          fontSize = 13.sp,
          color = HomezyTextSecondary
        )
        Spacer(modifier = Modifier.height(12.dp))
        HomezySearchBar(
          query = searchQuery,
          onQueryChange = { searchQuery = it },
          placeholder = "Search booking ID, customer, worker, or service..."
        )
      }
    }

    // Status Filter Chips
    item {
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        item {
          FilterChip(
            selected = selectedFilter == null,
            onClick = { selectedFilter = null },
            label = { Text("All (${bookingsList.size})", fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = HomezyPrimary,
              selectedLabelColor = Color.White
            )
          )
        }

        items(
          listOf(
            BookingStatus.PENDING,
            BookingStatus.ACCEPTED,
            BookingStatus.IN_PROGRESS,
            BookingStatus.COMPLETED,
            BookingStatus.CANCELLED
          )
        ) { status ->
          val count = bookingsList.count { it.status == status }
          FilterChip(
            selected = selectedFilter == status,
            onClick = { selectedFilter = status },
            label = { Text("${status.label} ($count)", fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = HomezyPrimary,
              selectedLabelColor = Color.White
            )
          )
        }
      }
    }

    // Booking Item Cards
    items(filteredBookings) { booking ->
      HomezyCard(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("admin_booking_card_${booking.id}")
      ) {
        // Row 1: Booking ID & Status
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = HomezyPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = booking.id,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = HomezyText
            )
          }

          StatusBadge(status = booking.status)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Service Name
        Text(
          text = booking.serviceName,
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = HomezyText
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Customer Details
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Person, contentDescription = null, tint = HomezyTextSecondary, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Customer: ${booking.customerName} (${booking.customerAddress})",
            fontSize = 12.sp,
            color = HomezyTextSecondary
          )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Assigned Worker
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Engineering, contentDescription = null, tint = HomezyPrimary, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Worker: ${booking.workerName}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (booking.workerName.contains("Unassigned")) Color(0xFFDC2626) else HomezyPrimary
          )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = HomezyBorder, thickness = 0.8.dp)
        Spacer(modifier = Modifier.height(8.dp))

        // Row: Date & Pricing Breakdown
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Schedule, contentDescription = null, tint = HomezyTextSecondary, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${booking.date} • ${booking.timeSlot}",
              fontSize = 11.sp,
              color = HomezyTextSecondary
            )
          }

          Column(horizontalAlignment = Alignment.End) {
            Text(
              text = "Total Amount: ₹${booking.grossAmount}",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyText
            )
            if (booking.status != BookingStatus.CANCELLED) {
              Text(
                text = "Member Share: ₹${booking.workerShare} (95%)",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF15803D)
              )
            }
          }
        }
      }
    }
  }
}
