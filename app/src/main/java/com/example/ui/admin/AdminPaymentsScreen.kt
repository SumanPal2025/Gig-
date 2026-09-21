package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

data class SettlementTransaction(
  val id: String,
  val bookingId: String,
  val workerName: String,
  val serviceName: String,
  val grossAmount: Int,
  val workerShare: Int,
  val welfareContribution: Int,
  val platformFee: Int,
  val timestamp: String,
  val status: String // "Settled ✓", "In Escrow ⏳", "Processing"
)

@Composable
fun AdminPaymentsScreen(
  modifier: Modifier = Modifier
) {
  var showBatchSettleDialog by remember { mutableStateOf(false) }
  var isBatchSettling by remember { mutableStateOf(false) }

  val derivedTransactions = remember(HomezyRepository.bookings) {
    HomezyRepository.bookings.mapIndexed { idx, b ->
      SettlementTransaction(
        id = "TXN-${9021 + idx}",
        bookingId = b.id,
        workerName = b.workerName,
        serviceName = b.serviceName,
        grossAmount = b.price,
        workerShare = if (b.workerEarnings > 0) b.workerEarnings else (b.price * 0.95).toInt(),
        welfareContribution = if (b.cooperativeContribution > 0) b.cooperativeContribution else (b.price * 0.03).toInt(),
        platformFee = if (b.platformFee > 0) b.platformFee else (b.price * 0.02).toInt(),
        timestamp = "${b.date}, ${b.timeSlot}",
        status = if (b.status == BookingStatus.COMPLETED) "Settled ✓" else "In Escrow ⏳"
      )
    }
  }

  var transactions by remember(derivedTransactions) { mutableStateOf(derivedTransactions) }
  val totalEscrow = remember(transactions) { transactions.filter { it.status != "Settled ✓" }.sumOf { it.grossAmount } }
  var pendingEscrowAmount by remember(totalEscrow) { mutableStateOf(totalEscrow) }
  val settledCountVal = remember(transactions) { transactions.count { it.status == "Settled ✓" } }
  var settledCount by remember(settledCountVal) { mutableStateOf(settledCountVal) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("admin_payments_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header
    item {
      Column {
        Text(
          text = "Transparent Cooperative Payments",
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Text(
          text = "Cooperative escrow settlements: 95% directly to workers, 3% welfare reserve, 2% platform fee",
          fontSize = 13.sp,
          color = HomezyTextSecondary
        )
      }
    }

    // Payout Summary Card
    item {
      HomezyCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = HomezyCard,
        elevation = 2.dp
      ) {
        Text(
          text = "Cooperative Settlement Split (Strict 95/3/2 Rule)",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyPrimary
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFDCFCE7),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC))
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Text("Worker Share (95%)", fontSize = 11.sp, color = Color(0xFF166534), fontWeight = FontWeight.SemiBold)
              Text("₹61,066", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF14532D))
              Text("Instant UPI settlement", fontSize = 10.sp, color = Color(0xFF15803D))
            }
          }

          Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFEFF6FF),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Text("Welfare Pool (3%)", fontSize = 11.sp, color = Color(0xFF1E40AF), fontWeight = FontWeight.SemiBold)
              Text("₹1,928", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
              Text("Health & emergency", fontSize = 10.sp, color = Color(0xFF1D4ED8))
            }
          }

          Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFF3F4F6),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Text("Platform Fee (2%)", fontSize = 11.sp, color = Color(0xFF374151), fontWeight = FontWeight.SemiBold)
              Text("₹1,286", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
              Text("Server & ops cap", fontSize = 10.sp, color = Color(0xFF4B5563))
            }
          }
        }
      }
    }

    // Escrow & Instant Batch Release Action Card
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Pending Escrow Balance",
              fontSize = 12.sp,
              color = HomezyTextSecondary
            )
            Text(
              text = "₹$pendingEscrowAmount",
              fontSize = 20.sp,
              fontWeight = FontWeight.ExtraBold,
              color = HomezyText
            )
            Text(
              text = "Customer payments held safely until job verification",
              fontSize = 11.sp,
              color = HomezyTextSecondary
            )
          }

          HomezyButton(
            text = if (isBatchSettling) "Releasing..." else "Release Escrow",
            onClick = {
              showBatchSettleDialog = true
            },
            variant = ButtonVariant.PRIMARY,
            icon = Icons.Default.CheckCircle,
            testTag = "admin_batch_settle_btn"
          )
        }
      }
    }

    // Transactions Table
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Live Escrow & Settlement Ledger",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp,
          color = HomezyText
        )
        HomezyBadge(
          text = "${transactions.size} Records",
          containerColor = HomezyPrimaryContainer,
          contentColor = HomezyPrimary
        )
      }
    }

    items(transactions) { txn ->
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = txn.id,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = HomezyTextSecondary
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "• Booking ${txn.bookingId}",
                fontSize = 12.sp,
                color = HomezyTextSecondary
              )
            }
            Text(
              text = "${txn.workerName} • ${txn.serviceName}",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = HomezyText
            )
          }

          HomezyBadge(
            text = txn.status,
            containerColor = if (txn.status.contains("Settled")) Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
            contentColor = if (txn.status.contains("Settled")) Color(0xFF15803D) else Color(0xFF92400E)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = HomezyBorder, thickness = 0.8.dp)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text("Gross Collected: ₹${txn.grossAmount}", fontSize = 12.sp, color = HomezyTextSecondary)
            Text(txn.timestamp, fontSize = 11.sp, color = HomezyTextSecondary)
          }

          Column(horizontalAlignment = Alignment.End) {
            Text(
              text = "Worker Payout: ₹${txn.workerShare}",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF15803D)
            )
            Text(
              text = "Welfare: ₹${txn.welfareContribution} | Fee: ₹${txn.platformFee}",
              fontSize = 11.sp,
              color = HomezyTextSecondary
            )
          }
        }
      }
    }
  }

  // Batch Settle Confirmation Dialog
  if (showBatchSettleDialog) {
    AlertDialog(
      onDismissRequest = { showBatchSettleDialog = false },
      title = {
        Text("Release Escrow Payouts", fontWeight = FontWeight.Bold)
      },
      text = {
        Text("Are you sure you want to release ₹$pendingEscrowAmount directly to the verified members via Instant Cooperative UPI? All 95% earnings will be deposited instantly without middleman deductions.")
      },
      confirmButton = {
        HomezyButton(
          text = "Confirm & Release",
          onClick = {
            transactions = transactions.map { it.copy(status = "Settled ✓") }
            pendingEscrowAmount = 0
            settledCount += 2
            showBatchSettleDialog = false
          },
          variant = ButtonVariant.PRIMARY
        )
      },
      dismissButton = {
        HomezyButton(
          text = "Cancel",
          onClick = { showBatchSettleDialog = false },
          variant = ButtonVariant.TEXT
        )
      }
    )
  }
}
