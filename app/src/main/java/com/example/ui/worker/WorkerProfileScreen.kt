package com.example.ui.worker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AuthUser
import com.example.ui.components.HomezyCard
import com.example.ui.theme.*

@Composable
fun WorkerProfileScreen(
  currentUser: AuthUser,
  onLogout: () -> Unit = {},
  onSwitchToCustomer: () -> Unit = {},
  onSwitchToAdmin: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var showInfoDialog by remember { mutableStateOf<Pair<String, String>?>(null) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("worker_profile_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Worker Profile Header
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(HomezyPrimary),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = currentUser.avatarInitials.ifEmpty { "RS" },
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White
          )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = currentUser.name,
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              color = HomezyText
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.Default.Verified, contentDescription = "Verified", tint = HomezySecondary, modifier = Modifier.size(16.dp))
          }
          Text(
            text = "Co-op Member #0429 • Electrician",
            fontSize = 13.sp,
            color = HomezyTextSecondary
          )
        }
      }
    }

    // Grouped List Rows:
    // Personal Information, Skills, Certifications, Cooperative, Welfare, Payments, Settings
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Column {
          WorkerProfileRow(
            title = "Personal Information",
            subtitle = currentUser.phone,
            icon = Icons.Default.PersonOutline,
            onClick = {
              showInfoDialog = "Personal Information" to "Name: ${currentUser.name}\nPhone: ${currentUser.phone}\nEmail: ${currentUser.email}\nService Cluster: Indiranagar, Sector 2"
            }
          )
          HorizontalDivider(color = HomezyBorder.copy(alpha = 0.6f))

          WorkerProfileRow(
            title = "Skills",
            subtitle = "6 Verified Skills",
            icon = Icons.Default.Build,
            onClick = {
              showInfoDialog = "Verified Skills" to "• AC Deep Cleaning\n• Residential Wiring & Earthing\n• Inverter & Backup Systems\n• Switchboard Diagnostics\n• Motor Repair\n• Circuit Breaker Tripping"
            }
          )
          HorizontalDivider(color = HomezyBorder.copy(alpha = 0.6f))

          WorkerProfileRow(
            title = "Certifications",
            subtitle = "Govt. ITI & NSQF Level 4",
            icon = Icons.Default.Verified,
            onClick = {
              showInfoDialog = "Certifications" to "• ITI Electrician Diploma (DGT, Govt. of India)\n• NSQF Level 4 Master Wireman (Skill India)\n• State Electrical Safety Board License #KA-9812"
            }
          )
          HorizontalDivider(color = HomezyBorder.copy(alpha = 0.6f))

          WorkerProfileRow(
            title = "Cooperative",
            subtitle = "COOP-KA-0429 (Bengaluru)",
            icon = Icons.Default.Handshake,
            onClick = {
              showInfoDialog = "Cooperative Membership" to "• Society: Bengaluru Household Craftspersons Co-op Ltd.\n• Member Since: March 2023\n• Voting Status: Active Shareholder\n• Patronage Dividend: Active"
            }
          )
          HorizontalDivider(color = HomezyBorder.copy(alpha = 0.6f))

          WorkerProfileRow(
            title = "Welfare",
            subtitle = "Healthcare & Safety Pool Active",
            icon = Icons.Default.HealthAndSafety,
            onClick = {
              showInfoDialog = "Welfare & Safety Net" to "• Group Medical Insurance: ₹5,00,000\n• Accidental Disability: ₹10,00,000\n• Emergency Solidarity Reserve: Enrolled"
            }
          )
          HorizontalDivider(color = HomezyBorder.copy(alpha = 0.6f))

          WorkerProfileRow(
            title = "Payments",
            subtitle = "SBI A/c ending 4819 (Daily Payout)",
            icon = Icons.Default.AccountBalance,
            onClick = {
              showInfoDialog = "Payout Settlement" to "• Linked Bank: State Bank of India\n• A/c: •••• •••• 4819\n• IFSC: SBIN0004218\n• Payout Cycle: Automated daily settlement at 9:00 PM"
            }
          )
          HorizontalDivider(color = HomezyBorder.copy(alpha = 0.6f))

          WorkerProfileRow(
            title = "Settings",
            subtitle = "Preferences & Notifications",
            icon = Icons.Default.Settings,
            onClick = {
              showInfoDialog = "Settings" to "• Dispatch Alerts: Sound & Vibration ON\n• Language: English / Kannada\n• App Version: HOMEZY v2.4 (Build 42)"
            }
          )
        }
      }
    }

    // Logout
    item {
      Spacer(modifier = Modifier.height(6.dp))
      Button(
        onClick = onLogout,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFFFEE2E2),
          contentColor = Color(0xFFDC2626)
        ),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("worker_logout_btn")
      ) {
        Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Logout", fontSize = 14.sp, fontWeight = FontWeight.Bold)
      }
    }
  }

  // Info Dialog
  if (showInfoDialog != null) {
    AlertDialog(
      onDismissRequest = { showInfoDialog = null },
      title = { Text(showInfoDialog?.first ?: "Details", fontWeight = FontWeight.Bold, color = HomezyText) },
      text = { Text(showInfoDialog?.second ?: "", fontSize = 14.sp, color = HomezyText) },
      confirmButton = {
        TextButton(onClick = { showInfoDialog = null }) {
          Text("Close", fontWeight = FontWeight.Bold, color = HomezyPrimary)
        }
      }
    )
  }
}

@Composable
private fun WorkerProfileRow(
  title: String,
  subtitle: String? = null,
  icon: ImageVector,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(vertical = 12.dp, horizontal = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
      Icon(icon, contentDescription = null, tint = HomezyPrimary, modifier = Modifier.size(20.dp))
      Spacer(modifier = Modifier.width(12.dp))
      Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = HomezyText)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
      if (subtitle != null) {
        Text(subtitle, fontSize = 12.sp, color = HomezyTextSecondary)
        Spacer(modifier = Modifier.width(6.dp))
      }
      Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = HomezyTextSecondary, modifier = Modifier.size(14.dp))
    }
  }
}
